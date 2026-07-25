package com.nuno.app.features.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuno.app.core.network.ApiService
import com.nuno.app.features.history.models.HistoryFilter
import com.nuno.app.features.history.models.MatchRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HistoryUiState(
    val matches: List<MatchRecord> = emptyList(),
    val selectedFilter: HistoryFilter = HistoryFilter.ALL,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                // Placeholder data - real backend endpoint would go here
                _uiState.value = _uiState.value.copy(
                    matches = getSampleMatches(),
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

    fun setFilter(filter: HistoryFilter) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun getFilteredMatches(): List<MatchRecord> {
        val state = _uiState.value
        var filtered = state.matches

        filtered = when (state.selectedFilter) {
            HistoryFilter.ALL -> filtered
            HistoryFilter.WINS -> filtered.filter { it.isWinner }
            HistoryFilter.LOSSES -> filtered.filter { !it.isWinner }
            HistoryFilter.CASUAL -> filtered.filter { it.gameMode.equals("CASUAL", true) }
            HistoryFilter.RANKED -> filtered.filter { it.gameMode.equals("RANKED", true) }
            HistoryFilter.PRIVATE -> filtered.filter { it.gameMode.equals("PRIVATE", true) }
        }

        if (state.searchQuery.isNotBlank()) {
            filtered = filtered.filter {
                it.opponentName.contains(state.searchQuery, ignoreCase = true)
            }
        }

        return filtered
    }

    fun getStats(): Triple<Int, Int, Int> {
        val matches = _uiState.value.matches
        val total = matches.size
        val wins = matches.count { it.isWinner }
        val losses = total - wins
        return Triple(total, wins, losses)
    }

    private fun getSampleMatches(): List<MatchRecord> {
        return listOf(
            MatchRecord("1", "CASUAL", "Alex", null, 420, 25, true, 0, 20, 75, "2 hours ago"),
            MatchRecord("2", "RANKED", "Nova", null, 380, 22, false, -18, 10, 50, "5 hours ago"),
            MatchRecord("3", "CASUAL", "Emma", null, 510, 30, true, 0, 20, 75, "Yesterday"),
            MatchRecord("4", "RANKED", "Sangam", null, 290, 18, true, 24, 30, 100, "2 days ago"),
            MatchRecord("5", "PRIVATE", "Friend", null, 600, 35, false, 0, 5, 50, "3 days ago")
        )
    }
}