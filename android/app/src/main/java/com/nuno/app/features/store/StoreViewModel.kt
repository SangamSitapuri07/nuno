package com.nuno.app.features.store

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
class StoreViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _storeState = MutableStateFlow<UiState<List<StoreItem>>>(UiState.Idle)
    val storeState: StateFlow<UiState<List<StoreItem>>> = _storeState.asStateFlow()

    private val _balanceState = MutableStateFlow(0)
    val balanceState: StateFlow<Int> = _balanceState.asStateFlow()

    private val _purchaseMessage = MutableStateFlow<String?>(null)
    val purchaseMessage: StateFlow<String?> = _purchaseMessage.asStateFlow()

    init {
        loadStore()
        loadBalance()
    }

    fun loadStore() {
        viewModelScope.launch {
            _storeState.value = UiState.Loading
            try {
                val response = apiService.getStore()
                if (response.success && response.data != null) {
                    _storeState.value = UiState.Success(response.data)
                } else {
                    _storeState.value = UiState.Error(response.error?.message ?: "Failed to load store")
                }
            } catch (e: Exception) {
                _storeState.value = UiState.Error(e.message ?: "Network error")
            }
        }
    }

    fun loadBalance() {
        viewModelScope.launch {
            try {
                val response = apiService.getBalance()
                if (response.success && response.data != null) {
                    _balanceState.value = response.data.coins
                }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    fun purchaseItem(item: StoreItem) {
        viewModelScope.launch {
            try {
                val response = apiService.purchaseItem(
                    PurchaseRequest(
                        itemId = item.itemId,
                        cosmeticType = item.type,
                        currency = item.currency,
                        price = item.price
                    )
                )
                if (response.success) {
                    _purchaseMessage.value = "Purchased ${item.name}!"
                    loadBalance()
                } else {
                    _purchaseMessage.value = response.error?.message ?: "Purchase failed"
                }
            } catch (e: Exception) {
                _purchaseMessage.value = "Network error"
            }
        }
    }

    fun clearMessage() {
        _purchaseMessage.value = null
    }
}