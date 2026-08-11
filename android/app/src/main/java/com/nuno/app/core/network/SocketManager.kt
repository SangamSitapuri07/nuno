package com.nuno.app.core.network

import android.util.Log
import com.nuno.app.BuildConfig
import com.nuno.app.core.utils.Constants
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

enum class SocketState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    AUTHENTICATED,
    ERROR
}

data class SocketEvent(
    val event: String,
    val data: JSONObject?
)

@Singleton
class SocketManager @Inject constructor(
    private val tokenManager: TokenManager
) {

    private var socket: Socket? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _connectionState = MutableStateFlow(SocketState.DISCONNECTED)
    val connectionState: StateFlow<SocketState> = _connectionState.asStateFlow()

    private val _events = MutableSharedFlow<SocketEvent>(extraBufferCapacity = 100)
    val events: SharedFlow<SocketEvent> = _events.asSharedFlow()

    companion object {
        private const val TAG = "SocketManager"
    }

    // ─────────────────────────────────────────
    // CONNECT
    // ─────────────────────────────────────────

    fun connect() {
        if (socket?.connected() == true) {
            Log.d(TAG, "Socket already connected")
            return
        }

        _connectionState.value = SocketState.CONNECTING

        try {
            val options = IO.Options().apply {
                reconnection = true
                reconnectionAttempts = Int.MAX_VALUE
                reconnectionDelay = 2000
                reconnectionDelayMax = 5000
                timeout = 20000
                transports = arrayOf("polling", "websocket")
                forceNew = false
            }

            socket = IO.socket(BuildConfig.SOCKET_URL, options)
            setupListeners()
            socket?.connect()

            Log.d(TAG, "Socket connecting to ${BuildConfig.SOCKET_URL}")

        } catch (e: Exception) {
            Log.e(TAG, "Socket connection error", e)
            _connectionState.value = SocketState.ERROR
        }
    }

    // ─────────────────────────────────────────
    // SETUP LISTENERS
    // ─────────────────────────────────────────

    private fun setupListeners() {
        socket?.on(Socket.EVENT_CONNECT) {
            Log.d(TAG, "Socket connected")
            _connectionState.value = SocketState.CONNECTED
            authenticate()
        }

        socket?.on(Socket.EVENT_DISCONNECT) {
            Log.d(TAG, "Socket disconnected")
            _connectionState.value = SocketState.DISCONNECTED
        }

        socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
            Log.e(TAG, "Socket error: ${args.getOrNull(0)}")
            _connectionState.value = SocketState.ERROR
        }

        // Authentication response
        socket?.on(Constants.EVENT_AUTHENTICATED) { args ->
            Log.d(TAG, "Socket authenticated")
            _connectionState.value = SocketState.AUTHENTICATED
            emitEvent(Constants.EVENT_AUTHENTICATED, args.getOrNull(0) as? JSONObject)
        }

        // Register all game events for listening
        registerGameEvents()
    }

    // ─────────────────────────────────────────
    // REGISTER GAME EVENTS
    // ─────────────────────────────────────────

    private fun registerGameEvents() {
        val gameEvents = listOf(
            // Matchmaking
            Constants.EVENT_QUEUE_JOINED,
            Constants.EVENT_QUEUE_LEFT,
            Constants.EVENT_MATCH_FOUND,
            "invite.received",
            "invite.sent",
            "friend.statusUpdated",
            "friend.requestAccepted",
            "friend.requestReceived",


            // Rooms
            Constants.EVENT_ROOM_CREATED,
            Constants.EVENT_ROOM_JOINED,
            Constants.EVENT_ROOM_LEFT,
            Constants.EVENT_ROOM_UPDATED,
            Constants.EVENT_ROOM_HOST_CHANGED,
            Constants.EVENT_ROOM_COUNTDOWN,
            Constants.EVENT_ROOM_COUNTDOWN_CANCELLED,
            Constants.EVENT_ROOM_KICKED,

            // Game
            Constants.EVENT_GAME_STARTED,
            Constants.EVENT_GAME_INITIAL_STATE,
            Constants.EVENT_GAME_FINISHED,
            Constants.EVENT_GAME_SYNC_STATE,
            Constants.EVENT_CARD_ACCEPTED,
            Constants.EVENT_TURN_CHANGED,
            Constants.EVENT_PLAYER_PLAYED_CARD,
            Constants.EVENT_PLAYER_DREW_CARD,
            Constants.EVENT_DIRECTION_CHANGED,

            // Surrender / Rematch
            Constants.EVENT_PLAYER_SURRENDERED,
            Constants.EVENT_REMATCH_REQUEST,
            Constants.EVENT_REMATCH_ACCEPT,
            Constants.EVENT_REMATCH_DECLINE,
            Constants.EVENT_REMATCH_STARTED,

            // Chat & Emotes
            Constants.EVENT_CHAT_RECEIVED,
            Constants.EVENT_QUICK_CHAT,
            Constants.EVENT_EMOTE_RECEIVED,
            "dm.received",
            "dm.sent",

            // Voice - make sure ALL these are here
            "voice.joined",
            "voice.userJoined",
            Constants.EVENT_VOICE_OFFER,
            Constants.EVENT_VOICE_ANSWER,
            Constants.EVENT_VOICE_ICE,
            // Spectator
            Constants.EVENT_SPECTATE_STATE,

            // System
            Constants.EVENT_ERROR
        )

        gameEvents.forEach { eventName ->
            socket?.on(eventName) { args ->
                val data = args.getOrNull(0) as? JSONObject
                Log.d(TAG, "Received event: $eventName")
                if (eventName == Constants.EVENT_ERROR) {
                    val code = data?.optString("code")
                    if (code == "TOKEN_EXPIRED" || code == "AUTH_FAILED") {
                        Log.w(TAG, "Socket token error ($code), reconnecting with fresh token...")
                        reconnectWithFreshToken()
                    }
                }
                emitEvent(eventName, data)
            }
        }
    }

    // ─────────────────────────────────────────
    // AUTHENTICATE
    // ─────────────────────────────────────────

    private fun authenticate() {
        scope.launch {
            val token = tokenManager.getAccessToken()

            if (token.isNullOrEmpty()) {
                Log.w(TAG, "No token available for authentication")
                return@launch
            }

            val data = JSONObject().apply {
                put("token", token)
            }
            socket?.emit(Constants.EVENT_AUTHENTICATE, data)
            Log.d(TAG, "Sent authentication request")
        }
    }

    // ─────────────────────────────────────────
    // EMIT EVENT
    // ─────────────────────────────────────────

    fun emit(event: String, data: JSONObject? = null) {
        if (socket?.connected() == true) {
            if (data != null) {
                socket?.emit(event, data)
            } else {
                socket?.emit(event)
            }
            Log.d(TAG, "Emitted event: $event")
        } else {
            Log.w(TAG, "Cannot emit event $event - socket not connected")
        }
    }

    // ─────────────────────────────────────────
    // EMIT EVENT (INTERNAL)
    // ─────────────────────────────────────────

    private fun emitEvent(event: String, data: JSONObject?) {
        scope.launch {
            _events.emit(SocketEvent(event, data))
        }
    }

    // ─────────────────────────────────────────
    // DISCONNECT
    // ─────────────────────────────────────────

    fun disconnect() {
        socket?.disconnect()
        socket?.off()
        socket = null
        _connectionState.value = SocketState.DISCONNECTED
        Log.d(TAG, "Socket disconnected manually")
    }

    // ─────────────────────────────────────────
    // RECONNECT WITH FRESH TOKEN
    // ─────────────────────────────────────────

    fun reconnectWithFreshToken() {
        disconnect()
        scope.launch {
            kotlinx.coroutines.delay(500)
            connect()
        }
    }

    // ─────────────────────────────────────────
    // IS CONNECTED
    // ─────────────────────────────────────────

    fun isConnected(): Boolean = socket?.connected() == true
}