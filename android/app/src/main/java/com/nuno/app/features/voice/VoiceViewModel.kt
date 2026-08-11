package com.nuno.app.features.voice

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuno.app.core.network.SocketManager
import com.nuno.app.core.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription
import javax.inject.Inject

data class VoiceUiState(
    val isConnected: Boolean = false,
    val isMicMuted: Boolean = true,
    val isSpeakerMuted: Boolean = true,
    val mutedPlayers: Set<String> = emptySet(),
    val connectedPeers: Set<String> = emptySet()
)

@HiltViewModel
class VoiceViewModel @Inject constructor(
    private val webRTCManager: WebRTCManager,
    private val socketManager: SocketManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(VoiceUiState())
    val uiState: StateFlow<VoiceUiState> = _uiState.asStateFlow()

    private var hasJoinedRoom = false

    companion object {
        private const val TAG = "VoiceViewModel"
    }

    init {
        observeSocketEvents()
        setupWebRTCCallbacks()
    }

    fun joinVoiceRoom(roomId: String) {
        if (hasJoinedRoom) {
            Log.d(TAG, "Already joined voice room")
            return
        }

        if (!socketManager.isConnected()) {
            Log.e(TAG, "Socket not connected")
            return
        }

        Log.d(TAG, "Joining voice room (default mic/speaker OFF): $roomId")

        // Initialize WebRTC when joining and start with mic & speaker MUTED by default
        webRTCManager.initialize()
        webRTCManager.setMuted(true)
        webRTCManager.setSpeakerMuted(true)

        val data = JSONObject().apply { put("roomId", roomId) }
        socketManager.emit(Constants.EVENT_VOICE_JOIN, data)
        hasJoinedRoom = true
        _uiState.value = _uiState.value.copy(
            isConnected = true,
            isMicMuted = true,
            isSpeakerMuted = true
        )
    }

    fun leaveVoiceRoom() {
        Log.d(TAG, "Leaving voice room")

        try { socketManager.emit(Constants.EVENT_VOICE_LEAVE) } catch (e: Exception) {}
        try { webRTCManager.closeAll() } catch (e: Exception) {}

        hasJoinedRoom = false
        _uiState.value = VoiceUiState()
    }

    fun toggleMic() {
        val newState = !_uiState.value.isMicMuted
        webRTCManager.setMuted(newState)
        _uiState.value = _uiState.value.copy(isMicMuted = newState)
        Log.d(TAG, "Mic muted toggled: $newState")
    }

    fun toggleSpeaker() {
        val newState = !_uiState.value.isSpeakerMuted
        webRTCManager.setSpeakerMuted(newState)
        _uiState.value = _uiState.value.copy(isSpeakerMuted = newState)
        Log.d(TAG, "Speaker muted toggled: $newState")
    }

    fun togglePlayerMute(playerId: String) {
        val current = _uiState.value.mutedPlayers
        if (current.contains(playerId)) {
            webRTCManager.unmutePlayer(playerId)
            _uiState.value = _uiState.value.copy(mutedPlayers = current - playerId)
        } else {
            webRTCManager.mutePlayer(playerId)
            _uiState.value = _uiState.value.copy(mutedPlayers = current + playerId)
        }
    }

    fun isPlayerMuted(playerId: String): Boolean = _uiState.value.mutedPlayers.contains(playerId)

    private fun observeSocketEvents() {
        viewModelScope.launch {
            socketManager.events.collect { event ->
                when (event.event) {
                    "voice.joined" -> handleVoiceJoined(event.data)
                    "voice.userJoined" -> handleUserJoined(event.data)
                    Constants.EVENT_VOICE_OFFER -> handleOffer(event.data)
                    Constants.EVENT_VOICE_ANSWER -> handleAnswer(event.data)
                    Constants.EVENT_VOICE_ICE -> handleIceCandidate(event.data)
                    "voice.left" -> handleUserLeft(event.data)
                }
            }
        }
    }

    private fun handleVoiceJoined(data: JSONObject?) {
        if (data == null) return
        val participants = data.optJSONArray("existingParticipants") ?: return
        Log.d(TAG, "Joined voice. ${participants.length()} existing participants")

        for (i in 0 until participants.length()) {
            val p = participants.getJSONObject(i)
            val userId = p.optString("userId")
            if (userId.isNotEmpty()) {
                viewModelScope.launch {
                    delay(200L * (i + 1))
                    initiateConnection(userId)
                }
            }
        }
    }

    private fun handleUserJoined(data: JSONObject?) {
        if (data == null) return
        val userId = data.optString("userId")
        if (userId.isEmpty()) return
        Log.d(TAG, "User joined voice room: $userId")
    }

    private fun handleUserLeft(data: JSONObject?) {
        if (data == null) return
        val userId = data.optString("userId")
        if (userId.isEmpty()) return
        Log.d(TAG, "User left voice: $userId")

        webRTCManager.closeConnection(userId)
        _uiState.value = _uiState.value.copy(
            connectedPeers = _uiState.value.connectedPeers - userId
        )
    }

    private fun initiateConnection(targetUserId: String) {
        Log.d(TAG, "Initiating voice connection to $targetUserId")

        val pc = webRTCManager.getOrCreatePeerConnection(targetUserId) ?: return

        webRTCManager.createOffer(targetUserId) { offer ->
            val data = JSONObject().apply {
                put("targetUserId", targetUserId)
                put("sdp", offer.description)
            }
            socketManager.emit(Constants.EVENT_VOICE_OFFER, data)
            Log.d(TAG, "Offer sent to $targetUserId")
        }
    }

    private fun handleOffer(data: JSONObject?) {
        if (data == null) return
        val fromUserId = data.optString("fromUserId")
        val sdp = data.optString("sdp")
        if (fromUserId.isEmpty() || sdp.isEmpty()) return

        Log.d(TAG, "Received offer from $fromUserId")

        webRTCManager.getOrCreatePeerConnection(fromUserId)
        webRTCManager.setRemoteDescription(fromUserId, SessionDescription(SessionDescription.Type.OFFER, sdp))

        webRTCManager.createAnswer(fromUserId) { answer ->
            val answerData = JSONObject().apply {
                put("targetUserId", fromUserId)
                put("sdp", answer.description)
            }
            socketManager.emit(Constants.EVENT_VOICE_ANSWER, answerData)
            Log.d(TAG, "Answer sent to $fromUserId")
        }
    }

    private fun handleAnswer(data: JSONObject?) {
        if (data == null) return
        val fromUserId = data.optString("fromUserId")
        val sdp = data.optString("sdp")
        if (fromUserId.isEmpty() || sdp.isEmpty()) return

        Log.d(TAG, "Received answer from $fromUserId")
        webRTCManager.setRemoteDescription(fromUserId, SessionDescription(SessionDescription.Type.ANSWER, sdp))
    }

    private fun handleIceCandidate(data: JSONObject?) {
        if (data == null) return
        val fromUserId = data.optString("fromUserId")
        val candidate = data.optString("candidate")
        val sdpMid = data.optString("sdpMid")
        val sdpMLineIndex = data.optInt("sdpMLineIndex", 0)
        if (fromUserId.isEmpty()) return

        webRTCManager.addIceCandidate(fromUserId, IceCandidate(sdpMid, sdpMLineIndex, candidate))
    }

    private fun setupWebRTCCallbacks() {
        webRTCManager.setOnIceCandidateCallback { userId, candidate ->
            val data = JSONObject().apply {
                put("targetUserId", userId)
                put("candidate", candidate.sdp)
                put("sdpMid", candidate.sdpMid)
                put("sdpMLineIndex", candidate.sdpMLineIndex)
            }
            socketManager.emit(Constants.EVENT_VOICE_ICE, data)
        }

        webRTCManager.setOnRemoteStreamCallback { userId, _ ->
            Log.d(TAG, "VOICE CONNECTED with $userId!")
            _uiState.value = _uiState.value.copy(
                connectedPeers = _uiState.value.connectedPeers + userId
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        try { webRTCManager.closeAll() } catch (e: Exception) {}
    }
}