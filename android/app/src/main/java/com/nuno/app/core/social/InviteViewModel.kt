package com.nuno.app.core.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuno.app.core.network.SocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.json.JSONObject
import javax.inject.Inject

data class IncomingInvite(
    val fromUserId: String,
    val fromUsername: String,
    val roomCode: String
)

@HiltViewModel
class InviteViewModel @Inject constructor(
    private val socketManager: SocketManager
) : ViewModel() {

    private val _incomingInvite = MutableStateFlow<IncomingInvite?>(null)
    val incomingInvite: StateFlow<IncomingInvite?> = _incomingInvite.asStateFlow()

    init {
        socketManager.events
            .onEach { event ->
                if (event.event == "invite.received") {
                    val data = event.data ?: return@onEach
                    _incomingInvite.value = IncomingInvite(
                        fromUserId = data.optString("fromUserId"),
                        fromUsername = data.optString("fromUsername"),
                        roomCode = data.optString("roomCode")
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun sendInvite(targetUserId: String, roomCode: String) {
        val data = JSONObject().apply {
            put("targetUserId", targetUserId)
            put("roomCode", roomCode)
        }
        socketManager.emit("invite.send", data)
    }

    fun acceptInvite() {
        val invite = _incomingInvite.value ?: return
        val data = JSONObject().apply {
            put("roomCode", invite.roomCode)
        }
        socketManager.emit("invite.accept", data)
        _incomingInvite.value = null
    }

    fun dismissInvite() {
        _incomingInvite.value = null
    }
}