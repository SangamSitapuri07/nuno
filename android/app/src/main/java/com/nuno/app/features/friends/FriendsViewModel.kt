package com.nuno.app.features.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuno.app.core.common.Resource
import com.nuno.app.core.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

data class DirectMessage(
    val fromUserId: String,
    val fromUsername: String,
    val message: String,
    val timestamp: Long,
    val isMe: Boolean
)

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val friendsRepository: FriendsRepository,
    private val socketManager: com.nuno.app.core.network.SocketManager
) : ViewModel() {

    private val _friendsState = MutableStateFlow<UiState<List<Friend>>>(UiState.Idle)
    val friendsState: StateFlow<UiState<List<Friend>>> = _friendsState.asStateFlow()

    private val _requestsState = MutableStateFlow<UiState<List<FriendRequest>>>(UiState.Idle)
    val requestsState: StateFlow<UiState<List<FriendRequest>>> = _requestsState.asStateFlow()

    private val _searchState = MutableStateFlow<UiState<List<PlayerSearchResult>>>(UiState.Idle)
    val searchState: StateFlow<UiState<List<PlayerSearchResult>>> = _searchState.asStateFlow()

    private val _dmsState = MutableStateFlow<Map<String, List<DirectMessage>>>(emptyMap())
    val dmsState: StateFlow<Map<String, List<DirectMessage>>> = _dmsState.asStateFlow()

    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()

    init {
        loadFriends()
        loadRequests()
        observeFriendEvents()
        startPeriodicRefresh()
    }

    private fun startPeriodicRefresh() {
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(10_000)
                if (socketManager.isConnected()) {
                    loadFriends()
                }
            }
        }
    }

    fun loadFriends() {
        friendsRepository.getFriends()
            .onEach { result ->
                _friendsState.value = when (result) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(result.data)
                    is Resource.Error -> UiState.Error(result.message)
                    is Resource.Idle -> UiState.Idle
                }
            }
            .launchIn(viewModelScope)
    }

    fun loadRequests() {
        friendsRepository.getFriendRequests()
            .onEach { result ->
                _requestsState.value = when (result) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(result.data)
                    is Resource.Error -> UiState.Error(result.message)
                    is Resource.Idle -> UiState.Idle
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeFriendEvents() {
        viewModelScope.launch {
            socketManager.events.collect { event ->
                when (event.event) {
                    com.nuno.app.core.utils.Constants.EVENT_AUTHENTICATED -> {
                        loadFriends()
                        loadRequests()
                    }
                    "friend.statusUpdated" -> {
                        val data = event.data ?: return@collect
                        val userId = data.optString("userId")
                        val status = data.optString("status")
                        val roomCode = if (data.has("roomCode")) data.optString("roomCode") else null
                        _friendsState.update { current ->
                            if (current is UiState.Success) {
                                UiState.Success(current.data.map { f ->
                                    if (f.userId == userId) {
                                        f.copy(
                                            status = status,
                                            roomCode = roomCode ?: f.roomCode
                                        )
                                    } else f
                                })
                            } else current
                        }
                    }
                    "friend.requestReceived" -> {
                        loadRequests()
                        _actionMessage.value = "New friend request received!"
                    }
                    "friend.requestAccepted" -> {
                        loadFriends()
                        loadRequests()
                        _actionMessage.value = "Friend request accepted!"
                    }
                    "dm.received" -> {
                        val data = event.data ?: return@collect
                        val fromUserId = data.optString("fromUserId")
                        val fromUsername = data.optString("fromUsername")
                        val message = data.optString("message")
                        val timestamp = data.optLong("timestamp", System.currentTimeMillis())
                        if (fromUserId.isNotEmpty() && message.isNotEmpty()) {
                            val dm = DirectMessage(fromUserId, fromUsername, message, timestamp, isMe = false)
                            _dmsState.update { current ->
                                val list = current[fromUserId] ?: emptyList()
                                current + (fromUserId to (list + dm))
                            }
                        }
                    }
                }
            }
        }
    }

    fun searchPlayers(query: String) {
        if (query.length < 2) {
            _searchState.value = UiState.Idle
            return
        }

        friendsRepository.searchPlayers(query)
            .onEach { result ->
                _searchState.value = when (result) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(result.data)
                    is Resource.Error -> UiState.Error(result.message)
                    is Resource.Idle -> UiState.Idle
                }
            }
            .launchIn(viewModelScope)
    }

    fun sendRequest(playerId: String) {
        friendsRepository.sendFriendRequest(playerId)
            .onEach { result ->
                if (result is Resource.Success) {
                    _actionMessage.value = "Friend request sent!"
                } else if (result is Resource.Error) {
                    _actionMessage.value = result.message
                }
            }
            .launchIn(viewModelScope)
    }

    fun acceptRequest(requestId: String) {
        friendsRepository.acceptRequest(requestId)
            .onEach { result ->
                if (result is Resource.Success) {
                    _actionMessage.value = "Friend added!"
                    loadFriends()
                    loadRequests()

                    // Notify the other user via socket
                    val data = org.json.JSONObject().apply {
                        put("targetUserId", requestId)
                    }
                    socketManager.emit("friend.requestAccepted", data)
                }
            }
            .launchIn(viewModelScope)
    }

    fun rejectRequest(requestId: String) {
        friendsRepository.rejectRequest(requestId)
            .onEach { result ->
                if (result is Resource.Success) {
                    _actionMessage.value = "Request rejected."
                    loadRequests()
                }
            }
            .launchIn(viewModelScope)
    }

    fun removeFriend(friendId: String) {
        friendsRepository.removeFriend(friendId)
            .onEach { result ->
                if (result is Resource.Success) {
                    _actionMessage.value = "Friend removed."
                    loadFriends()
                }
            }
            .launchIn(viewModelScope)
    }

    fun sendDm(targetUserId: String, messageText: String) {
        val text = messageText.trim()
        if (targetUserId.isEmpty() || text.isEmpty()) return

        val dm = DirectMessage("me", "Me", text, System.currentTimeMillis(), isMe = true)
        _dmsState.update { current ->
            val list = current[targetUserId] ?: emptyList()
            current + (targetUserId to (list + dm))
        }

        val data = org.json.JSONObject().apply {
            put("targetUserId", targetUserId)
            put("message", text)
        }
        socketManager.emit("dm.send", data)
    }

    fun clearMessage() {
        _actionMessage.value = null
    }
}