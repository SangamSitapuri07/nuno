package com.nuno.app.features.notifications

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
class NotificationsViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _notificationsState = MutableStateFlow<UiState<List<Notification>>>(UiState.Idle)
    val notificationsState: StateFlow<UiState<List<Notification>>> = _notificationsState.asStateFlow()

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _notificationsState.value = UiState.Loading
            try {
                val response = apiService.getNotifications()
                if (response.success && response.data != null) {
                    _notificationsState.value = UiState.Success(response.data)
                } else {
                    _notificationsState.value = UiState.Error(
                        response.error?.message ?: "Failed to load notifications"
                    )
                }
            } catch (e: Exception) {
                _notificationsState.value = UiState.Error(e.message ?: "Network error")
            }
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            try {
                apiService.markNotificationsRead()
                loadNotifications()
            } catch (e: Exception) {
                // Ignore
            }
        }
    }
}