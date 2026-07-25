package com.nuno.app.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuno.app.core.common.Resource
import com.nuno.app.core.common.UiState
import com.nuno.app.core.network.ApiService
import com.nuno.app.core.network.MatchHistoryData
import com.nuno.app.core.network.UserProfile
import com.nuno.app.features.profile.models.Achievement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val apiService: ApiService
) : ViewModel() {

    private val _profileState = MutableStateFlow<UiState<UserProfile>>(UiState.Idle)
    val profileState: StateFlow<UiState<UserProfile>> = _profileState.asStateFlow()

    private val _historyState = MutableStateFlow<UiState<List<MatchHistoryData>>>(UiState.Idle)
    val historyState: StateFlow<UiState<List<MatchHistoryData>>> = _historyState.asStateFlow()

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    init {
        loadProfile()
        loadMatchHistory()
    }

    fun loadProfile() {
        profileRepository.getProfile()
            .onEach { result ->
                _profileState.value = when (result) {
                    is Resource.Loading -> UiState.Loading
                    is Resource.Success -> UiState.Success(result.data)
                    is Resource.Error -> UiState.Error(result.message)
                    is Resource.Idle -> UiState.Idle
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadMatchHistory() {
        viewModelScope.launch {
            _historyState.value = UiState.Loading
            try {
                val response = apiService.getMatchHistory()
                if (response.success && response.data != null) {
                    _historyState.value = UiState.Success(response.data)
                } else {
                    _historyState.value = UiState.Success(emptyList())
                }
            } catch (_: Exception) {
                _historyState.value = UiState.Success(emptyList())
            }
        }
    }
}