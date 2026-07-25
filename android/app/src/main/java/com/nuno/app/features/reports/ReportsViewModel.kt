package com.nuno.app.features.reports

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
class ReportsViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun reportPlayer(playerId: String, reason: String, matchId: String? = null) {
        viewModelScope.launch {
            try {
                val response = apiService.reportPlayer(
                    ReportRequest(playerId, reason, matchId)
                )
                if (response.success) {
                    _message.value = "Report submitted. Thank you for keeping NUNO safe."
                } else {
                    _message.value = response.error?.message ?: "Failed to submit report"
                }
            } catch (e: Exception) {
                _message.value = "Network error"
            }
        }
    }

    fun blockPlayer(playerId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.blockPlayer(BlockRequest(playerId))
                if (response.success) {
                    _message.value = "Player blocked"
                } else {
                    _message.value = response.error?.message ?: "Failed to block"
                }
            } catch (e: Exception) {
                _message.value = "Network error"
            }
        }
    }

    fun unblockPlayer(playerId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.unblockPlayer(playerId)
                if (response.success) {
                    _message.value = "Player unblocked"
                }
            } catch (e: Exception) {
                _message.value = "Network error"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}