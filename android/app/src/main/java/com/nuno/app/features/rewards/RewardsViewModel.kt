package com.nuno.app.features.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuno.app.core.network.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RewardsViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun claimDailyReward() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.claimDailyReward()
                if (response.success) {
                    _message.value = "🎉 Daily reward claimed! +100 coins, +50 XP"
                } else {
                    _message.value = response.error?.message ?: "Already claimed today"
                }
            } catch (e: Exception) {
                _message.value = "Network error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}