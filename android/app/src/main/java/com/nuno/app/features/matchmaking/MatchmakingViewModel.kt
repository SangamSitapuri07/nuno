package com.nuno.app.features.matchmaking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuno.app.core.network.SocketManager
import com.nuno.app.core.network.SocketState
import com.nuno.app.core.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

enum class QueueStatus {
    IDLE,
    CONNECTING,
    SEARCHING,
    MATCH_FOUND,
    GAME_STARTING,
    ERROR
}

data class MatchmakingUiState(
    val status: QueueStatus = QueueStatus.IDLE,
    val elapsedSeconds: Int = 0,
    val matchId: String? = null,
    val roomId: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class MatchmakingViewModel @Inject constructor(
    private val socketManager: SocketManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchmakingUiState())
    val uiState: StateFlow<MatchmakingUiState> = _uiState.asStateFlow()

    companion object {
        private const val TAG = "MatchmakingViewModel"
    }

    init {
        connectSocket()
        observeSocketEvents()
    }

    private fun connectSocket() {
        socketManager.connect()

        viewModelScope.launch {
            socketManager.connectionState.collect { state ->
                Log.d(TAG, "Socket state: $state")
                when (state) {
                    SocketState.CONNECTING -> {
                        _uiState.value = _uiState.value.copy(status = QueueStatus.CONNECTING)
                    }
                    SocketState.AUTHENTICATED -> {
                        if (_uiState.value.status == QueueStatus.CONNECTING) {
                            _uiState.value = _uiState.value.copy(status = QueueStatus.IDLE)
                        }
                    }
                    SocketState.ERROR -> {
                        _uiState.value = _uiState.value.copy(
                            status = QueueStatus.ERROR,
                            errorMessage = "Connection error. Please try again."
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    private fun observeSocketEvents() {
        viewModelScope.launch {
            socketManager.events.collect { event ->
                Log.d(TAG, "Event: ${event.event}")

                when (event.event) {
                    Constants.EVENT_QUEUE_JOINED -> {
                        _uiState.value = _uiState.value.copy(status = QueueStatus.SEARCHING)
                        startTimer()
                    }

                    Constants.EVENT_QUEUE_LEFT -> {
                        _uiState.value = _uiState.value.copy(
                            status = QueueStatus.IDLE,
                            elapsedSeconds = 0
                        )
                    }

                    Constants.EVENT_MATCH_FOUND -> {
                        val matchId = event.data?.optString("matchId")
                        val roomId = event.data?.optString("roomId")
                        Log.d(TAG, "Match found! matchId=$matchId, roomId=$roomId")
                        _uiState.value = _uiState.value.copy(
                            status = QueueStatus.MATCH_FOUND,
                            matchId = matchId,
                            roomId = roomId
                        )
                    }

                    Constants.EVENT_GAME_STARTED -> {
                        val matchId = event.data?.optString("matchId")
                        Log.d(TAG, "Game started! matchId=$matchId")
                        _uiState.value = _uiState.value.copy(
                            status = QueueStatus.GAME_STARTING,
                            matchId = matchId
                        )
                    }

                    Constants.EVENT_ERROR -> {
                        val message = event.data?.optString("message") ?: "Unknown error"
                        _uiState.value = _uiState.value.copy(
                            status = QueueStatus.ERROR,
                            errorMessage = message
                        )
                    }
                }
            }
        }
    }

    fun joinQueue(mode: String = Constants.MODE_CASUAL, requiredPlayers: Int = 2) {
        val data = JSONObject().apply {
            put("mode", mode)
            put("region", "AUTO")
            put("requiredPlayers", requiredPlayers)
        }
        socketManager.emit(Constants.EVENT_QUEUE_JOIN, data)
        _uiState.value = _uiState.value.copy(status = QueueStatus.SEARCHING)
    }

    fun leaveQueue() {
        socketManager.emit(Constants.EVENT_QUEUE_LEAVE)
        _uiState.value = _uiState.value.copy(
            status = QueueStatus.IDLE,
            elapsedSeconds = 0
        )
    }

    private fun startTimer() {
        viewModelScope.launch {
            var seconds = 0
            while (_uiState.value.status == QueueStatus.SEARCHING) {
                kotlinx.coroutines.delay(1000)
                seconds++
                _uiState.value = _uiState.value.copy(elapsedSeconds = seconds)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (_uiState.value.status == QueueStatus.SEARCHING) {
            leaveQueue()
        }
    }
}