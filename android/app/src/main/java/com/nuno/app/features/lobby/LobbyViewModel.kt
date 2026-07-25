package com.nuno.app.features.lobby

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuno.app.core.network.SocketManager
import com.nuno.app.core.network.TokenManager
import com.nuno.app.core.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONObject
import javax.inject.Inject

data class LobbyUiState(
    val room: LobbyRoom? = null,
    val countdown: Int = -1,
    val chatMessages: List<ChatMessage> = emptyList(),
    val gameStarted: Boolean = false,
    val matchId: String? = null,
    val currentUserId: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class LobbyViewModel @Inject constructor(
    private val socketManager: SocketManager,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LobbyUiState())
    val uiState: StateFlow<LobbyUiState> = _uiState.asStateFlow()

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    companion object {
        private const val TAG = "LobbyViewModel"
    }

    init {
        loadCurrentUser()
        observeSocketEvents()
        ensureSocketConnected()
    }

    // ─────────────────────────────────────────
    // ENSURE SOCKET CONNECTED
    // ─────────────────────────────────────────

    private fun ensureSocketConnected() {
        if (!socketManager.isConnected()) {
            Log.d(TAG, "Socket not connected, connecting...")
            socketManager.connect()
        } else {
            Log.d(TAG, "Socket already connected")
        }
    }

    // ─────────────────────────────────────────
    // WAIT FOR CONNECTION
    // ─────────────────────────────────────────

    private suspend fun waitForConnection(): Boolean {
        var attempts = 0
        while (!socketManager.isConnected() && attempts < 15) {
            Log.d(TAG, "Waiting for socket connection... attempt $attempts")
            delay(500)
            attempts++
        }

        if (!socketManager.isConnected()) {
            Log.e(TAG, "Socket failed to connect after $attempts attempts")
            return false
        }

        // Wait for authentication
        delay(1500)
        return true
    }

    // ─────────────────────────────────────────
    // LOAD CURRENT USER
    // ─────────────────────────────────────────

    private fun loadCurrentUser() {
        viewModelScope.launch {
            tokenManager.userId.collect { userId ->
                _uiState.value = _uiState.value.copy(currentUserId = userId)
            }
        }
    }

    // ─────────────────────────────────────────
    // OBSERVE SOCKET EVENTS
    // ─────────────────────────────────────────

    private fun observeSocketEvents() {
        viewModelScope.launch {
            socketManager.events.collect { event ->
                Log.d(TAG, "Event: ${event.event}, data: ${event.data}")

                when (event.event) {
                    Constants.EVENT_ROOM_CREATED -> {
                        Log.d(TAG, "Room created event")
                        parseRoom(event.data)
                    }

                    Constants.EVENT_ROOM_JOINED -> {
                        Log.d(TAG, "Room joined event")
                        parseRoom(event.data)
                    }

                    Constants.EVENT_ROOM_UPDATED -> {
                        Log.d(TAG, "Room updated event")
                        parseRoom(event.data)
                    }

                    Constants.EVENT_ROOM_COUNTDOWN -> {
                        val count = event.data?.optInt("count") ?: 0
                        _uiState.value = _uiState.value.copy(countdown = count)
                    }

                    Constants.EVENT_ROOM_COUNTDOWN_CANCELLED -> {
                        _uiState.value = _uiState.value.copy(countdown = -1)
                    }

                    Constants.EVENT_GAME_STARTED -> {
                        val matchId = event.data?.optString("matchId")
                        _uiState.value = _uiState.value.copy(
                            gameStarted = true,
                            matchId = matchId
                        )
                    }

                    Constants.EVENT_CHAT_RECEIVED -> {
                        parseChatMessage(event.data)
                    }

                    Constants.EVENT_ERROR -> {
                        val message = event.data?.optString("message") ?: "Unknown error"
                        Log.e(TAG, "Error event: $message")
                        _uiState.value = _uiState.value.copy(
                            errorMessage = message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
    fun createRoomWithPlayerCount(playerCount: Int) {
        Log.d(TAG, "Creating room with $playerCount players")
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            val connected = waitForConnection()

            if (!connected) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Cannot connect to server."
                )
                return@launch
            }

            val data = JSONObject().apply {
                put("gameMode", Constants.MODE_PRIVATE)
                put("maxPlayers", playerCount)
                put("voiceEnabled", true)
            }
            socketManager.emit(Constants.EVENT_ROOM_CREATE, data)
            Log.d(TAG, "Emitted room.create with maxPlayers=$playerCount")
        }
    }
    // ─────────────────────────────────────────
    // CREATE ROOM
    // ─────────────────────────────────────────

    fun createRoom() {
        Log.d(TAG, "Creating room...")
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            val connected = waitForConnection()

            if (!connected) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Cannot connect to server. Please try again."
                )
                return@launch
            }

            val data = JSONObject().apply {
                put("gameMode", Constants.MODE_PRIVATE)
                put("maxPlayers", 10)
                put("voiceEnabled", true)
            }
            socketManager.emit(Constants.EVENT_ROOM_CREATE, data)
            Log.d(TAG, "Emitted room.create event")
        }
    }

    // ─────────────────────────────────────────
    // JOIN ROOM BY CODE
    // ─────────────────────────────────────────

    fun joinRoomByCode(roomCode: String) {
        Log.d(TAG, "Joining room with code: $roomCode")
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            val connected = waitForConnection()

            if (!connected) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Cannot connect to server. Please try again."
                )
                return@launch
            }

            val data = JSONObject().apply {
                put("roomCode", roomCode.uppercase())
            }
            socketManager.emit(Constants.EVENT_ROOM_JOIN, data)
            Log.d(TAG, "Emitted room.join event")
        }
    }

    // ─────────────────────────────────────────
    // LEAVE ROOM
    // ─────────────────────────────────────────

    fun leaveRoom() {
        socketManager.emit(Constants.EVENT_ROOM_LEAVE)
        _uiState.value = LobbyUiState(currentUserId = _uiState.value.currentUserId)
    }

    // ─────────────────────────────────────────
    // TOGGLE READY
    // ─────────────────────────────────────────

    fun toggleReady() {
        val currentUserId = _uiState.value.currentUserId ?: return
        val room = _uiState.value.room ?: return
        val currentPlayer = room.players.find { it.userId == currentUserId }
        val newReadyState = !(currentPlayer?.isReady ?: false)

        val data = JSONObject().apply {
            put("isReady", newReadyState)
        }
        socketManager.emit(Constants.EVENT_ROOM_READY, data)
    }

    // ─────────────────────────────────────────
    // SEND CHAT
    // ─────────────────────────────────────────

    fun sendChat(message: String) {
        if (message.isBlank()) return

        val data = JSONObject().apply {
            put("message", message.trim())
        }
        socketManager.emit(Constants.EVENT_CHAT_SEND, data)
    }

    // ─────────────────────────────────────────
    // KICK PLAYER
    // ─────────────────────────────────────────

    fun kickPlayer(targetUserId: String) {
        val data = JSONObject().apply {
            put("targetUserId", targetUserId)
        }
        socketManager.emit(Constants.EVENT_ROOM_KICK, data)
    }

    // ─────────────────────────────────────────
    // PARSE ROOM
    // ─────────────────────────────────────────

    private fun parseRoom(data: JSONObject?) {
        if (data == null) {
            Log.e(TAG, "Received null data for room")
            return
        }

        try {
            val roomJson = data.optJSONObject("room") ?: data
            Log.d(TAG, "Parsing room JSON: $roomJson")

            val room = json.decodeFromString<LobbyRoom>(roomJson.toString())
            _uiState.value = _uiState.value.copy(
                room = room,
                isLoading = false
            )
            Log.d(TAG, "Room parsed successfully: ${room.roomCode}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse room: ${e.message}", e)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "Failed to load room data"
            )
        }
    }

    // ─────────────────────────────────────────
    // PARSE CHAT
    // ─────────────────────────────────────────

    private fun parseChatMessage(data: JSONObject?) {
        if (data == null) return

        try {
            val message = ChatMessage(
                userId = data.optString("userId"),
                username = data.optString("username"),
                message = data.optString("message"),
                timestamp = data.optLong("timestamp", System.currentTimeMillis())
            )
            val currentMessages = _uiState.value.chatMessages
            _uiState.value = _uiState.value.copy(
                chatMessages = currentMessages + message
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse chat", e)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}