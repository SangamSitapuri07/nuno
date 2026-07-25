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

    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()

    init {
        loadFriends()
        loadRequests()
        observeFriendEvents()
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
                        _friendsState.update { current ->
                            if (current is UiState.Success) {
                                UiState.Success(current.data.map { f ->
                                    if (f.userId == userId) f.copy(status = status) else f
                                })
                            } else current
                        }
                    }
                    "friend.requestAccepted" -> {
                        loadFriends()
                        loadRequests()
                        _actionMessage.value = "Friend request accepted!"
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

    fun clearMessage() {
        _actionMessage.value = null
    }
}