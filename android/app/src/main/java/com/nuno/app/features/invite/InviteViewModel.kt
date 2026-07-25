package com.nuno.app.features.invite

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuno.app.core.network.SocketManager
import com.nuno.app.core.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

data class InviteData(
    val fromUserId: String,
    val fromUsername: String,
    val roomCode: String,
    val timestamp: Long
)

@HiltViewModel
class InviteViewModel @Inject constructor(
    private val socketManager: SocketManager
) : ViewModel() {

    private val _pendingInvite = MutableStateFlow<InviteData?>(null)
    val pendingInvite: StateFlow<InviteData?> = _pendingInvite.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    companion object {
        private const val TAG = "InviteViewModel"
    }

    init {
        observeInvites()
    }

    private fun observeInvites() {
        viewModelScope.launch {
            socketManager.events.collect { event ->
                when (event.event) {
                    "invite.received" -> {
                        val data = event.data ?: return@collect
                        val invite = InviteData(
                            fromUserId = data.optString("fromUserId"),
                            fromUsername = data.optString("fromUsername"),
                            roomCode = data.optString("roomCode"),
                            timestamp = data.optLong("timestamp", System.currentTimeMillis())
                        )
                        _pendingInvite.value = invite
                        Log.d(TAG, "Invite received from ${invite.fromUsername}")
                    }

                    "invite.sent" -> {
                        _message.value = "Invite sent!"
                    }
                }
            }
        }
    }

    fun sendInvite(targetUserId: String, roomCode: String) {
        val data = JSONObject().apply {
            put("targetUserId", targetUserId)
            put("roomCode", roomCode)
        }
        socketManager.emit(Constants.EVENT_INVITE_SEND, data)
        Log.d(TAG, "Sending invite to $targetUserId with code $roomCode")
    }

    fun acceptInvite() {
        val invite = _pendingInvite.value ?: return
        val data = JSONObject().apply {
            put("roomCode", invite.roomCode)
        }
        socketManager.emit(Constants.EVENT_INVITE_ACCEPT, data)
        _pendingInvite.value = null
        Log.d(TAG, "Accepted invite for room ${invite.roomCode}")
    }

    fun declineInvite() {
        _pendingInvite.value = null
    }

    fun clearMessage() {
        _message.value = null
    }
}