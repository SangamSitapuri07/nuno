package com.nuno.app.features.gameplay

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.components.GameAvatar
import com.nuno.app.core.designsystem.components.GameButton
import com.nuno.app.core.designsystem.components.GamePanel
import com.nuno.app.core.designsystem.components.ButtonStyle
import com.nuno.app.core.utils.showToast
import com.nuno.app.features.gameplay.components.*
import com.nuno.app.features.reports.ReportsViewModel
import com.nuno.app.features.voice.VoiceViewModel
import kotlinx.coroutines.delay
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp

enum class AnimationPhase {
    SHUFFLING,
    DEALING,
    PLAYING
}

@Composable
fun GameplayScreen(
    matchId: String,
    onGameEnd: (winner: String) -> Unit,
    viewModel: GameViewModel = hiltViewModel(),
    reportsViewModel: ReportsViewModel = hiltViewModel(),
    voiceViewModel: VoiceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showChat by remember { mutableStateOf(false) }
    var showQuickChat by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    var animationPhase by remember { mutableStateOf(AnimationPhase.SHUFFLING) }
    var hasPlayedIntro by remember { mutableStateOf(false) }

    // Intro animation sequence
    LaunchedEffect(uiState.gameState) {
        if (uiState.gameState != null && !hasPlayedIntro) {
            hasPlayedIntro = true
            delay(2000)
            animationPhase = AnimationPhase.DEALING
            delay(3000)
            animationPhase = AnimationPhase.PLAYING
        }
    }

    // Voice auto-join
    LaunchedEffect(uiState.gameState?.roomId) {
        uiState.gameState?.roomId?.let { roomId ->
            delay(2000)
            try { voiceViewModel.joinVoiceRoom(roomId) } catch (e: Exception) {}
        }
    }

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            context.showToast(it)
            viewModel.clearMessage()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // ANIMATED COSMIC BACKGROUND
        GameBackground()

        when {
            uiState.isLoading || uiState.gameState == null -> {
                // Loading
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.85f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = GameColors.Cyan, modifier = Modifier.size(80.dp), strokeWidth = 6.dp)
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("LOADING GAME...", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(onClick = { viewModel.requestSync() }) { Text("RETRY LOAD") }
                    }
                }
            }

            animationPhase == AnimationPhase.SHUFFLING -> {
                ShuffleAnimation()
            }

            animationPhase == AnimationPhase.DEALING -> {
                DealingAnimation(playerCount = uiState.gameState!!.playerCardCounts.size)
            }

            else -> {
                // MAIN GAME UI - Delegate directly to GameTableScreen
                val gameState = uiState.gameState!!
                val isMyTurn = gameState.currentTurn == uiState.currentUserId
                val opponentsList = gameState.playerCardCounts.filter { it.key != uiState.currentUserId }
                    .map { (uid, count) ->
                        val pInfo = gameState.playerNames[uid]
                        com.nuno.app.screens.game.OpponentData(
                            userId = uid,
                            username = pInfo?.username ?: "Player",
                            cardCount = count,
                            isCurrentTurn = uid == gameState.currentTurn
                        )
                    }

                val myHandData = viewModel.getSortedHand().map { c ->
                    com.nuno.app.screens.game.GameCardData(
                        cardId = c.cardId,
                        color = c.color,
                        value = c.value,
                        type = c.type
                    )
                }

                val topCardData = gameState.topCard?.let { c ->
                    com.nuno.app.screens.game.GameCardData(
                        cardId = c.cardId,
                        color = c.color,
                        value = c.value,
                        type = c.type
                    )
                }

                val voiceState by voiceViewModel.uiState.collectAsState()

                com.nuno.app.screens.game.GameTableScreen(
                    opponents = opponentsList,
                    myHand = myHandData,
                    topCard = topCardData,
                    currentColor = gameState.currentColor,
                    drawPileCount = gameState.drawPileCount,
                    isMyTurn = isMyTurn,
                    remainingTime = uiState.remainingTime,
                    direction = gameState.direction,
                    totalTurns = gameState.totalTurns,
                    roomCode = gameState.roomId.take(7).uppercase(),
                    isMicMuted = voiceState.isMicMuted,
                    isSpeakerMuted = voiceState.isSpeakerMuted,
                    onPlayCard = { cardData ->
                        val card = gameState.myHand.find { it.cardId == cardData.cardId }
                        if (card != null) viewModel.playCard(card)
                    },
                    onDrawCard = { viewModel.drawCard() },
                    onChat = { showChat = true },
                    onQuickChat = { showQuickChat = true },
                    onVoice = { voiceViewModel.toggleMic() },
                    onSpeaker = { voiceViewModel.toggleSpeaker() },
                    onEmotes = { showQuickChat = true },
                    onMenu = { showMenu = true },
                    onUnoCall = { viewModel.callUno() },
                    showUnoButton = myHandData.size <= 2
                )
            }
        }
        // ═══ FLOATING CHAT BUBBLES ═══
        if (uiState.gameState != null) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 70.dp, top = 100.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                uiState.recentQuickChats.takeLast(3).forEach { chat ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = GameColors.SurfaceCard.copy(alpha = 0.9f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "${chat.username}: ${chat.message}",
                            color = GameColors.TextWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Floating emotes
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 70.dp, top = 200.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                uiState.recentEmotes.takeLast(2).forEach { emote ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = GameColors.SurfaceCard.copy(alpha = 0.9f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = emote.emote, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = emote.username, color = GameColors.Cyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // ═══ SPECIAL EFFECTS ═══
        uiState.activeEffect?.let { effect ->
            when (effect) {
                "SKIP" -> SkipEffect(onComplete = {})
                "REVERSE" -> ReverseEffect(onComplete = {})
                "DRAW_TWO" -> { DrawTwoEffect(onComplete = {}); DrawWaveEffect() }
                "WILD" -> WildEffect(onComplete = {})
                "WILD_DRAW_FOUR" -> { WildDrawFourEffect(onComplete = {}); DrawWaveEffect(waveColor = GameColors.Gold) }
            }
        }

        // Card play animation
        uiState.flyingPlayCard?.let { card ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                PlayCardAnimation(card = com.nuno.app.features.gameplay.GameCard(card.cardId, card.type, card.color, card.value), startX = 0f, startY = 300f, onComplete = {})
            }
        }

        // Draw card animation
        if (uiState.flyingDrawCard) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                DrawCardAnimation(endX = -200f, endY = 200f, onComplete = {})
            }
        }

        // Turn change glow
        if (uiState.turnChangeAnimation) {
            TurnChangeGlow(isMyTurn = uiState.gameState?.currentTurn == uiState.currentUserId)
        }

        // ═══ DIALOGS ═══

        // Color Picker
        if (uiState.showColorPicker) {
            Dialog(onDismissRequest = { viewModel.cancelColorPicker() }) {
                GamePanel(modifier = Modifier.width(280.dp), borderColor = GameColors.Gold) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("CHOOSE COLOR", color = GameColors.TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            listOf("RED" to GameColors.CardRed, "BLUE" to GameColors.CardBlue, "GREEN" to GameColors.CardGreen, "YELLOW" to GameColors.CardYellow).forEach { (name, color) ->
                                Box(
                                    modifier = Modifier.size(50.dp).shadow(8.dp, CircleShape, spotColor = color).background(color, CircleShape).clickable { viewModel.playWildCard(name) },
                                    contentAlignment = Alignment.Center
                                ) {}
                            }
                        }
                    }
                }
            }
        }

        // Chat
        if (showChat) {
            ChatOverlayInline(messages = uiState.recentQuickChats, onSend = { viewModel.sendChat(it) }, onClose = { showChat = false })
        }

        // Quick Chat
        if (showQuickChat) {
            QuickChatOverlayInline(
                onSendQuick = { viewModel.sendQuickChat(it); showQuickChat = false },
                onSendEmote = { viewModel.sendEmote(it); showQuickChat = false },
                onClose = { showQuickChat = false }
            )
        }

        // Menu
        if (showMenu) {
            Dialog(onDismissRequest = { showMenu = false }) {
                GamePanel(modifier = Modifier.width(300.dp), borderColor = GameColors.Red) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⚠️", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("EXIT GAME?", color = GameColors.TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            GameButton("CANCEL", onClick = { showMenu = false }, style = ButtonStyle.SECONDARY, modifier = Modifier.weight(1f))
                            GameButton("EXIT", onClick = { viewModel.surrender(); showMenu = false }, style = ButtonStyle.DANGER, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // Game Over with confetti
        if (uiState.gameFinished && uiState.result != null) {
            val isWinner = uiState.currentUserId != null && uiState.result!!.winner == uiState.currentUserId

            if (isWinner) {
                ConfettiEffect()
                CoinsFlyingEffect(coinCount = 20)
            }

            Dialog(onDismissRequest = {}) {
                GamePanel(modifier = Modifier.width(400.dp), borderColor = if (isWinner) GameColors.Gold else GameColors.Red) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(if (isWinner) "🏆" else "💔", fontSize = 60.sp)
                        Text(if (isWinner) "VICTORY!" else "DEFEAT", color = if (isWinner) GameColors.Gold else GameColors.Red, fontSize = 32.sp, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                            SmallStat("Duration", "${uiState.result!!.duration}s")
                            SmallStat("Turns", "${uiState.result!!.totalTurns}")
                            SmallStat("XP", if (isWinner) "+125" else "+50")
                            SmallStat("Coins", if (isWinner) "+50" else "+20")
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            GameButton("HOME", onClick = {
                                try { voiceViewModel.leaveVoiceRoom() } catch (e: Exception) {}
                                onGameEnd(uiState.result!!.winner)
                            }, style = ButtonStyle.SECONDARY, modifier = Modifier.weight(1f))
                            GameButton("PLAY AGAIN", onClick = {
                                try { voiceViewModel.leaveVoiceRoom() } catch (e: Exception) {}
                                onGameEnd(uiState.result!!.winner)
                            }, style = ButtonStyle.PRIMARY, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

// ═══ HELPER COMPOSABLES ═══

@Composable
private fun SideBtn(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(40.dp).background(GameColors.Surface, CircleShape).clickable { onClick() }, contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        }
        Text(label, color = GameColors.TextGray, fontSize = 8.sp)
    }
}

@Composable
private fun SmallStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = GameColors.TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Black)
        Text(label, color = GameColors.TextGray, fontSize = 9.sp)
    }
}

@Composable
private fun ChatOverlayInline(messages: List<QuickChatDisplay>, onSend: (String) -> Unit, onClose: () -> Unit) {
    var msg by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)), contentAlignment = Alignment.CenterStart) {
        Card(modifier = Modifier.width(300.dp).fillMaxHeight().padding(8.dp), colors = CardDefaults.cardColors(containerColor = GameColors.SurfaceCard)) {
            Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("CHAT", color = GameColors.TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Black)
                    IconButton(onClick = onClose, modifier = Modifier.size(28.dp)) { Icon(Icons.Default.Close, null, tint = GameColors.TextGray) }
                }
                androidx.compose.foundation.lazy.LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(messages.size) { i -> Text("${messages[i].username}: ${messages[i].message}", color = GameColors.TextWhite, fontSize = 12.sp) }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(value = msg, onValueChange = { msg = it }, modifier = Modifier.weight(1f).height(40.dp), singleLine = true,
                        shape = RoundedCornerShape(20.dp), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = GameColors.TextWhite, unfocusedTextColor = GameColors.TextWhite, focusedBorderColor = GameColors.Blue, unfocusedBorderColor = GameColors.BorderPurple),
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp))
                    IconButton(onClick = { if (msg.isNotBlank()) { onSend(msg); msg = "" } }) { Icon(Icons.Default.Send, null, tint = GameColors.Cyan) }
                }
            }
        }
    }
}

@Composable
private fun QuickChatOverlayInline(onSendQuick: (String) -> Unit, onSendEmote: (String) -> Unit, onClose: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
        Card(modifier = Modifier.width(300.dp), colors = CardDefaults.cardColors(containerColor = GameColors.SurfaceCard)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("QUICK CHAT", color = GameColors.TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Black)
                    IconButton(onClick = onClose, modifier = Modifier.size(28.dp)) { Icon(Icons.Default.Close, null, tint = GameColors.TextGray) }
                }
                Spacer(modifier = Modifier.height(8.dp))
                listOf("Hello!", "Good luck!", "Well played!", "Thanks!", "Oops!", "Nice move!").chunked(2).forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        row.forEach { m -> Box(modifier = Modifier.weight(1f).background(GameColors.Surface, RoundedCornerShape(8.dp)).clickable { onSendQuick(m) }.padding(12.dp), contentAlignment = Alignment.Center) { Text(m, color = GameColors.TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold) } }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("😀", "😂", "😎", "😢", "😡", "👍", "❤️", "🔥").forEach { e ->
                        Box(modifier = Modifier.weight(1f).aspectRatio(1f).background(GameColors.Surface, RoundedCornerShape(6.dp)).clickable { onSendEmote(e) }, contentAlignment = Alignment.Center) { Text(e, fontSize = 20.sp) }
                    }
                }
            }
        }
    }
}

private fun getCardColor(color: String): Color = when (color) {
    "RED" -> GameColors.CardRed
    "BLUE" -> GameColors.CardBlue
    "GREEN" -> GameColors.CardGreen
    "YELLOW" -> GameColors.CardYellow
    else -> GameColors.CardBlack
}

private fun getCardText(value: String): String = when (value) {
    "SKIP" -> "⊘"; "REVERSE" -> "⇄"; "DRAW_TWO" -> "+2"
    "WILD" -> "★"; "WILD_DRAW_FOUR" -> "+4"; else -> value
}