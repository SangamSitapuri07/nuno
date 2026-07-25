package com.nuno.app.features.gameplay

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nuno.app.core.audio.HapticManager
import com.nuno.app.core.audio.HapticPattern
import com.nuno.app.core.audio.SoundEffect
import com.nuno.app.core.audio.SoundManager
import com.nuno.app.core.network.SocketManager
import com.nuno.app.core.network.TokenManager
import com.nuno.app.core.utils.Constants
import com.nuno.app.features.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONObject
import javax.inject.Inject

data class QuickChatDisplay(
    val userId: String,
    val username: String,
    val message: String,
    val timestamp: Long
)

data class EmoteDisplay(
    val userId: String,
    val username: String,
    val emote: String,
    val timestamp: Long
)

data class GameplayUiState(
    val gameState: GameState? = null,
    val currentUserId: String? = null,
    val remainingTime: Int = 20,
    val showColorPicker: Boolean = false,
    val pendingWildCardId: String? = null,
    val gameFinished: Boolean = false,
    val result: GameResult? = null,
    val message: String? = null,
    val isLoading: Boolean = true,
    val recentQuickChats: List<QuickChatDisplay> = emptyList(),
    val recentEmotes: List<EmoteDisplay> = emptyList(),
    val rematchRequestedBy: Set<String> = emptySet(),
    val handSortMode: HandSortMode = HandSortMode.DEFAULT,
    val activeEffect: String? = null,
    val flyingPlayCard: GameCard? = null,
    val flyingDrawCard: Boolean = false,
    val turnChangeAnimation: Boolean = false,
    val hasCalledUno: Boolean = false,
    val canChallenge: Boolean = false
)

enum class HandSortMode {
    DEFAULT,
    BY_COLOR,
    BY_VALUE
}

@HiltViewModel
class GameViewModel @Inject constructor(
    private val socketManager: SocketManager,
    private val tokenManager: TokenManager,
    private val soundManager: SoundManager,
    private val hapticManager: HapticManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameplayUiState())
    val uiState: StateFlow<GameplayUiState> = _uiState.asStateFlow()

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    companion object {
        private const val TAG = "GameViewModel"
    }

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch {
            // Ensure user info is loaded from token if missing
            authRepository.ensureUserInfoLoaded()

            // Load userId
            var userId = tokenManager.getUserId()

            // If still null, wait for it to be saved
            var attempts = 0
            while (userId == null && attempts < 10) {
                delay(200)
                userId = tokenManager.getUserId()
                attempts++
            }

            Log.d(TAG, "Loaded userId: $userId (attempts: $attempts)")
            _uiState.value = _uiState.value.copy(currentUserId = userId)

            // THEN: Start observing events
            observeSocketEvents()

            // THEN: Connect and sync
            connectAndSync()
        }
    }

    private fun connectAndSync() {
        viewModelScope.launch {
            if (!socketManager.isConnected()) {
                Log.d(TAG, "Socket not connected, connecting...")
                socketManager.connect()

                var waitAttempts = 0
                while (!socketManager.isConnected() && waitAttempts < 20) {
                    delay(500)
                    waitAttempts++
                }
                delay(1500)
            }

            var attempts = 0
            while (_uiState.value.gameState == null && attempts < 30) {
                Log.d(TAG, "Requesting sync attempt ${attempts + 1}, myId=${_uiState.value.currentUserId}")
                requestSync()
                delay(500)
                attempts++
            }

            if (_uiState.value.gameState == null) {
                Log.e(TAG, "Failed to load game state")
                _uiState.value = _uiState.value.copy(
                    message = "Failed to load game. Tap RETRY LOAD."
                )
            } else {
                Log.d(TAG, "Game loaded! myId=${_uiState.value.currentUserId}, currentTurn=${_uiState.value.gameState?.currentTurn}")
            }
        }
    }

    private fun observeSocketEvents() {
        viewModelScope.launch {
            socketManager.events.collect { event ->
                when (event.event) {
                    Constants.EVENT_GAME_INITIAL_STATE,
                    Constants.EVENT_GAME_SYNC_STATE -> parseGameState(event.data)

                    Constants.EVENT_TURN_CHANGED -> {
                        val remainingTime = event.data?.optInt("remainingTime", 20) ?: 20
                        soundManager.playSound(SoundEffect.TURN_CHANGE)
                        _uiState.value = _uiState.value.copy(
                            remainingTime = remainingTime,
                            turnChangeAnimation = true
                        )
                        viewModelScope.launch {
                            delay(800)
                            _uiState.value = _uiState.value.copy(turnChangeAnimation = false)
                        }
                    }

                    Constants.EVENT_PLAYER_PLAYED_CARD -> {
                        val cardValue = event.data?.optString("currentValue")
                        when (cardValue) {
                            "SKIP" -> triggerEffect("SKIP")
                            "REVERSE" -> triggerEffect("REVERSE")
                            "DRAW_TWO" -> triggerEffect("DRAW_TWO")
                            "WILD" -> triggerEffect("WILD")
                            "WILD_DRAW_FOUR" -> triggerEffect("WILD_DRAW_FOUR")
                        }
                    }

                    Constants.EVENT_CARD_ACCEPTED -> {
                        _uiState.value = _uiState.value.copy(
                            showColorPicker = false,
                            pendingWildCardId = null
                        )
                    }

                    Constants.EVENT_GAME_FINISHED -> {
                        val winner = event.data?.optString("winner") ?: ""
                        val duration = event.data?.optInt("duration") ?: 0
                        val totalTurns = event.data?.optInt("totalTurns") ?: 0

                        if (winner == _uiState.value.currentUserId) {
                            soundManager.playSound(SoundEffect.WIN)
                            hapticManager.vibrate(HapticPattern.SUCCESS)
                        } else {
                            soundManager.playSound(SoundEffect.LOSE)
                            hapticManager.vibrate(HapticPattern.ERROR)
                        }

                        _uiState.value = _uiState.value.copy(
                            gameFinished = true,
                            result = GameResult(winner, duration, totalTurns)
                        )
                    }

                    Constants.EVENT_QUICK_CHAT -> parseQuickChat(event.data)
                    Constants.EVENT_EMOTE_RECEIVED -> parseEmote(event.data)
                    Constants.EVENT_CHAT_RECEIVED -> parseRegularChat(event.data)

                    Constants.EVENT_ERROR -> {
                        val message = event.data?.optString("message") ?: "Error"
                        _uiState.value = _uiState.value.copy(message = message)
                    }
                }
            }
        }
    }

    private fun triggerEffect(effect: String) {
        _uiState.value = _uiState.value.copy(activeEffect = effect)

        // Play sound for effect
        when (effect) {
            "SKIP" -> soundManager.playSound(SoundEffect.SKIP)
            "REVERSE" -> soundManager.playSound(SoundEffect.REVERSE)
            "DRAW_TWO" -> soundManager.playSound(SoundEffect.DRAW_TWO)
            "WILD" -> soundManager.playSound(SoundEffect.WILD)
            "WILD_DRAW_FOUR" -> soundManager.playSound(SoundEffect.WILD)
        }

        viewModelScope.launch {
            delay(1500)
            _uiState.value = _uiState.value.copy(activeEffect = null)
        }
    }

    fun requestSync() {
        socketManager.emit(Constants.EVENT_GAME_SYNC_REQUEST)
    }

    fun playCard(card: GameCard) {
        val currentUserId = _uiState.value.currentUserId
        val gameState = _uiState.value.gameState

        Log.d(TAG, "════════════════════════════")
        Log.d(TAG, "PLAY CARD: ${card.value} ${card.color}")
        Log.d(TAG, "My userId:    $currentUserId")
        Log.d(TAG, "Current turn: ${gameState?.currentTurn}")
        Log.d(TAG, "All players:  ${gameState?.players}")
        Log.d(TAG, "Match check:  ${gameState?.currentTurn == currentUserId}")
        Log.d(TAG, "════════════════════════════")

        if (currentUserId == null) {
            _uiState.value = _uiState.value.copy(message = "User not loaded. Restart app.")
            return
        }

        if (gameState == null) {
            _uiState.value = _uiState.value.copy(message = "Game not loaded.")
            return
        }

        if (gameState.currentTurn != currentUserId) {
            _uiState.value = _uiState.value.copy(
                message = "Not your turn! (Turn: ${gameState.currentTurn.take(6)})"
            )
            hapticManager.vibrate(HapticPattern.ERROR)
            return
        }

        val isPlayable = card.type == "WILD" ||
                card.color == gameState.currentColor ||
                card.value == gameState.currentValue

        if (!isPlayable) {
            _uiState.value = _uiState.value.copy(
                message = "Cannot play - need ${gameState.currentColor} or ${gameState.currentValue}"
            )
            hapticManager.vibrate(HapticPattern.ERROR)
            return
        }

        if (card.type == "WILD") {
            _uiState.value = _uiState.value.copy(
                showColorPicker = true,
                pendingWildCardId = card.cardId
            )
            return
        }

        _uiState.value = _uiState.value.copy(flyingPlayCard = card)
        soundManager.playSound(SoundEffect.CARD_PLAY)
        hapticManager.vibrate(HapticPattern.LIGHT)

        viewModelScope.launch {
            delay(1200)  // Was 600, now 1200 (slower)
            _uiState.value = _uiState.value.copy(flyingPlayCard = null)
        }

        val data = JSONObject().apply { put("cardId", card.cardId) }
        socketManager.emit(Constants.EVENT_CARD_PLAY, data)
        Log.d(TAG, "✓ Emitted card.play to backend")
    }

    fun playWildCard(color: String) {
        val cardId = _uiState.value.pendingWildCardId ?: return

        soundManager.playSound(SoundEffect.CARD_PLAY)
        hapticManager.vibrate(HapticPattern.MEDIUM)

        val data = JSONObject().apply {
            put("cardId", cardId)
            put("selectedColor", color)
        }
        socketManager.emit(Constants.EVENT_CARD_PLAY, data)

        _uiState.value = _uiState.value.copy(
            showColorPicker = false,
            pendingWildCardId = null
        )
    }

    fun drawCard() {
        val currentUserId = _uiState.value.currentUserId ?: return
        val gameState = _uiState.value.gameState ?: return

        if (gameState.currentTurn != currentUserId) {
            _uiState.value = _uiState.value.copy(message = "Not your turn!")
            hapticManager.vibrate(HapticPattern.ERROR)
            return
        }

        _uiState.value = _uiState.value.copy(flyingDrawCard = true)
        soundManager.playSound(SoundEffect.CARD_DRAW)
        hapticManager.vibrate(HapticPattern.LIGHT)

        viewModelScope.launch {
            delay(1000)  // Was 500, now 1000
            _uiState.value = _uiState.value.copy(flyingDrawCard = false)
        }

        socketManager.emit(Constants.EVENT_CARD_DRAW)
    }

    fun cancelColorPicker() {
        _uiState.value = _uiState.value.copy(
            showColorPicker = false,
            pendingWildCardId = null
        )
    }

    fun surrender() {
        socketManager.emit(Constants.EVENT_SURRENDER)
        hapticManager.vibrate(HapticPattern.HEAVY)
    }

    fun sendQuickChat(messageType: String) {
        // Send both as quick chat event AND as regular chat
        val data = JSONObject().apply { put("message", messageType) }
        socketManager.emit(Constants.EVENT_CHAT_SEND, data)
    }

    fun sendEmote(emote: String) {
        val data = JSONObject().apply { put("emote", emote) }
        socketManager.emit(Constants.EVENT_EMOTE_SEND, data)
    }

    fun sendChat(message: String) {
        if (message.isBlank()) return
        val data = JSONObject().apply { put("message", message.trim()) }
        socketManager.emit(Constants.EVENT_CHAT_SEND, data)
    }

    fun requestRematch() {
        socketManager.emit(Constants.EVENT_REMATCH_REQUEST)
    }

    fun acceptRematch() {
        socketManager.emit(Constants.EVENT_REMATCH_ACCEPT)
    }

    fun declineRematch() {
        socketManager.emit(Constants.EVENT_REMATCH_DECLINE)
    }

    fun callUno() {
        socketManager.emit(Constants.EVENT_UNO_CALL)
        soundManager.playSound(SoundEffect.UNO_CALL)
        hapticManager.vibrate(HapticPattern.SUCCESS)
        _uiState.value = _uiState.value.copy(hasCalledUno = true)
    }

    fun setHandSortMode(mode: HandSortMode) {
        _uiState.value = _uiState.value.copy(handSortMode = mode)
    }

    fun getSortedHand(): List<GameCard> {
        val hand = _uiState.value.gameState?.myHand ?: return emptyList()
        return when (_uiState.value.handSortMode) {
            HandSortMode.DEFAULT -> hand
            HandSortMode.BY_COLOR -> hand.sortedWith(compareBy({ it.color }, { it.value }))
            HandSortMode.BY_VALUE -> hand.sortedBy { it.value }
        }
    }

    private fun parseGameState(data: JSONObject?) {
        if (data == null) return
        try {
            val gameState = json.decodeFromString<GameState>(data.toString())
            _uiState.value = _uiState.value.copy(
                gameState = gameState,
                isLoading = false
            )

            val myId = _uiState.value.currentUserId
            Log.d(TAG, "════════ GAME STATE UPDATE ════════")
            Log.d(TAG, "My userId:      $myId")
            Log.d(TAG, "Current turn:   ${gameState.currentTurn}")
            Log.d(TAG, "All players:    ${gameState.players}")
            Log.d(TAG, "Is my turn?     ${gameState.currentTurn == myId}")
            Log.d(TAG, "Card count:     ${gameState.myHand.size}")
            Log.d(TAG, "Top card:       ${gameState.currentValue} ${gameState.currentColor}")
            Log.d(TAG, "════════════════════════════════════")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse game state: ${e.message}", e)
        }
    }

    private fun parseQuickChat(data: JSONObject?) {
        if (data == null) return
        try {
            val chat = QuickChatDisplay(
                userId = data.optString("userId"),
                username = data.optString("username"),
                message = Constants.QUICK_CHAT_MESSAGES[data.optString("messageType")] ?: "",
                timestamp = data.optLong("timestamp", System.currentTimeMillis())
            )
            _uiState.value = _uiState.value.copy(
                recentQuickChats = _uiState.value.recentQuickChats.takeLast(4) + chat
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse quick chat", e)
        }
    }

    private fun parseEmote(data: JSONObject?) {
        if (data == null) return
        try {
            val emote = EmoteDisplay(
                userId = data.optString("userId"),
                username = data.optString("username"),
                emote = data.optString("emote"),
                timestamp = data.optLong("timestamp", System.currentTimeMillis())
            )
            _uiState.value = _uiState.value.copy(
                recentEmotes = _uiState.value.recentEmotes.takeLast(4) + emote
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse emote", e)
        }
    }

    private fun parseRegularChat(data: JSONObject?) {
        if (data == null) return
        try {
            val chat = QuickChatDisplay(
                userId = data.optString("userId"),
                username = data.optString("username"),
                message = data.optString("message"),
                timestamp = data.optLong("timestamp", System.currentTimeMillis())
            )
            _uiState.value = _uiState.value.copy(
                recentQuickChats = _uiState.value.recentQuickChats.takeLast(4) + chat
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse chat", e)
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}