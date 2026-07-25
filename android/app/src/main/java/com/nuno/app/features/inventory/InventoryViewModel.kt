package com.nuno.app.features.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuno.app.core.network.ApiService
import com.nuno.app.features.inventory.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InventoryUiState(
    val items: List<InventoryItem> = emptyList(),
    val selectedTab: InventoryTab = InventoryTab.ALL,
    val selectedFilter: InventoryFilter = InventoryFilter.ALL,
    val selectedSort: InventorySort = InventorySort.NEWEST,
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null
)

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    init {
        loadInventory()
    }

    fun loadInventory() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                // Placeholder items - replace with backend call when endpoint is ready
                _uiState.value = _uiState.value.copy(
                    items = getSampleItems(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun setTab(tab: InventoryTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun setFilter(filter: InventoryFilter) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }

    fun setSort(sort: InventorySort) {
        _uiState.value = _uiState.value.copy(selectedSort = sort)
    }

    fun toggleFavorite(itemId: String) {
        val updated = _uiState.value.items.map {
            if (it.id == itemId) it.copy(isFavorite = !it.isFavorite) else it
        }
        _uiState.value = _uiState.value.copy(items = updated)
    }

    fun equipItem(itemId: String) {
        val updated = _uiState.value.items.map {
            when {
                it.id == itemId -> it.copy(isEquipped = true)
                it.cosmeticType == _uiState.value.items.find { i -> i.id == itemId }?.cosmeticType -> it.copy(isEquipped = false)
                else -> it
            }
        }
        _uiState.value = _uiState.value.copy(
            items = updated,
            message = "Item equipped!"
        )
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    fun getFilteredItems(): List<InventoryItem> {
        val state = _uiState.value
        var filtered = state.items

        if (state.selectedTab != InventoryTab.ALL) {
            filtered = filtered.filter { it.cosmeticType.contains(state.selectedTab.name.dropLast(1), ignoreCase = true) }
        }

        if (state.selectedFilter != InventoryFilter.ALL) {
            filtered = filtered.filter { it.rarity == state.selectedFilter.name }
        }

        filtered = when (state.selectedSort) {
            InventorySort.NEWEST -> filtered.sortedByDescending { it.unlockedAt }
            InventorySort.OLDEST -> filtered.sortedBy { it.unlockedAt }
            InventorySort.NAME -> filtered.sortedBy { it.name }
            InventorySort.RARITY -> filtered.sortedByDescending {
                when (it.rarity) {
                    "MYTHIC" -> 5
                    "LEGENDARY" -> 4
                    "EPIC" -> 3
                    "RARE" -> 2
                    else -> 1
                }
            }
        }

        return filtered
    }

    private fun getSampleItems(): List<InventoryItem> {
        return listOf(
            InventoryItem("1", "cardback_1", "CARD_BACK", "Classic Back", "COMMON", 1, false, true, "2026-01-01"),
            InventoryItem("2", "avatar_1", "AVATAR", "Default Avatar", "COMMON", 1, false, true, "2026-01-01"),
            InventoryItem("3", "emote_1", "EMOTE", "Happy", "COMMON", 1, true, false, "2026-01-15"),
            InventoryItem("4", "cardback_2", "CARD_BACK", "Galaxy Back", "EPIC", 1, false, false, "2026-02-01"),
            InventoryItem("5", "title_1", "TITLE", "Novice", "COMMON", 1, false, true, "2026-01-01"),
            InventoryItem("6", "frame_1", "FRAME", "Gold Frame", "LEGENDARY", 1, true, false, "2026-03-01")
        )
    }
}