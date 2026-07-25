package com.nuno.app.features.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuno.app.core.common.UiState
import com.nuno.app.core.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _globalState = MutableStateFlow<UiState<List<LeaderboardEntry>>>(UiState.Idle)
    val globalState: StateFlow<UiState<List<LeaderboardEntry>>> = _globalState.asStateFlow()

    private val _friendsState = MutableStateFlow<UiState<List<LeaderboardEntry>>>(UiState.Idle)
    val friendsState: StateFlow<UiState<List<LeaderboardEntry>>> = _friendsState.asStateFlow()

    init {
        loadGlobal()
        loadFriends()
    }

    fun loadGlobal() {
        viewModelScope.launch {
            _globalState.value = UiState.Loading
            try {
                val response = apiService.getGlobalLeaderboard()
                if (response.success && response.data != null) {
                    _globalState.value = UiState.Success(response.data)
                } else {
                    _globalState.value = UiState.Error(
                        response.error?.message ?: "Failed to load leaderboard."
                    )
                }
            } catch (e: Exception) {
                _globalState.value = UiState.Error(e.message ?: "Network error")
            }
        }
    }

    fun loadFriends() {
        viewModelScope.launch {
            _friendsState.value = UiState.Loading
            try {
                val response = apiService.getFriendsLeaderboard()
                if (response.success && response.data != null) {
                    _friendsState.value = UiState.Success(response.data)
                } else {
                    _friendsState.value = UiState.Error(
                        response.error?.message ?: "Failed to load."
                    )
                }
            } catch (e: Exception) {
                _friendsState.value = UiState.Error(e.message ?: "Network error")
            }
        }
    }
}