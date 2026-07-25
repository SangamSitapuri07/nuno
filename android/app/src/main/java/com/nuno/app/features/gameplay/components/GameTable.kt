package com.nuno.app.features.gameplay.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.gameplay.GameCard
import com.nuno.app.features.gameplay.GameState
import com.nuno.app.features.gameplay.HandSortMode
import com.nuno.app.features.gameplay.QuickChatDisplay
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.scale

@Composable
fun GameTable(
    gameState: GameState,
    currentUserId: String?,
    remainingTime: Int,
    sortedHand: List<GameCard>,
    recentChats: List<QuickChatDisplay>,
    onCardClick: (GameCard) -> Unit,
    onDrawClick: () -> Unit,
    onChatClick: () -> Unit,
    onQuickChatClick: () -> Unit,
    onEmotesClick: () -> Unit,
    onMenuClick: () -> Unit,
    onSortClick: () -> Unit,
    onUnoClick: () -> Unit = {}
) {
    val isMyTurn = gameState.currentTurn == currentUserId
    val opponents = gameState.playerCardCounts.filter { it.key != currentUserId }.entries.toList()
    val myHandSize = sortedHand.size
    val showUnoButton = myHandSize == 2 && isMyTurn  // Show when about to play last card

    Row(modifier = Modifier.fillMaxSize()) {
        LeftActionBar(
            onChatClick = onChatClick,
            onQuickChatClick = onQuickChatClick,
            onEmotesClick = onEmotesClick,
            onMenuClick = onMenuClick,
            onUnoClick = onUnoClick,
            showUnoButton = showUnoButton
        )

        // MAIN GAME AREA
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            // TOP - Opponents row + chat overlay
            Box(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ) {
                    opponents.take(6).forEach { (uid, count) ->
                        val playerName = gameState.playerNames[uid]?.username ?: "Player"
                        PlayerSeat(
                            username = playerName,
                            cardCount = count,
                            isCurrentTurn = uid == gameState.currentTurn,
                            isMyself = false
                        )
                    }
                }

                // Chat bubbles on left
                Column(
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    recentChats.takeLast(3).forEach { chat ->
                        ChatBubble(username = chat.username, message = chat.message)
                    }
                }
            }

            // CENTER - Cards
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DrawPile(
                    drawCount = gameState.drawPileCount,
                    isMyTurn = isMyTurn,
                    onDrawClick = onDrawClick
                )
                Spacer(modifier = Modifier.width(40.dp))
                DiscardPile(
                    topCard = gameState.topCard,
                    currentColor = gameState.currentColor
                )
            }

            // BOTTOM - Player hand
            PlayerHandRow(
                cards = sortedHand,
                currentColor = gameState.currentColor,
                currentValue = gameState.currentValue,
                isMyTurn = isMyTurn,
                onCardClick = onCardClick,
                onSortClick = onSortClick
            )
        }

        // RIGHT SIDEBAR
        RightInfoPanel(
            gameState = gameState,
            remainingTime = remainingTime,
            isMyTurn = isMyTurn
        )
    }
}

@Composable
private fun LeftActionBar(
    onChatClick: () -> Unit,
    onQuickChatClick: () -> Unit,
    onEmotesClick: () -> Unit,
    onMenuClick: () -> Unit,
    onUnoClick: () -> Unit = {},
    showUnoButton: Boolean = false
) {
    Column(
        modifier = Modifier
            .width(72.dp)
            .fillMaxHeight()
            .background(SurfaceCard.copy(alpha = 0.5f))
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        com.nuno.app.features.voice.MicButton()
        com.nuno.app.features.voice.SpeakerButton()

        HorizontalDivider(color = BorderPurple.copy(alpha = 0.3f), modifier = Modifier.padding(horizontal = 12.dp))

        if (showUnoButton) {
            UnoButton(onClick = onUnoClick)
            HorizontalDivider(color = BorderPurple.copy(alpha = 0.3f), modifier = Modifier.padding(horizontal = 12.dp))
        }

        ActionButton(Icons.AutoMirrored.Filled.Chat, "CHAT", AccentCyan, onChatClick)
        ActionButton(Icons.Default.Message, "QUICK", SuccessGreen, onQuickChatClick)
        ActionButton(Icons.Default.EmojiEmotions, "EMOTES", AccentGold, onEmotesClick)

        Spacer(modifier = Modifier.weight(1f))

        ActionButton(Icons.Default.Menu, "MENU", TextSecondary, onMenuClick)
    }
}

@Composable
private fun UnoButton(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "uno")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .scale(pulse)
                .background(DangerRed, CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "UNO!",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
private fun ActionButton(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(44.dp).background(SurfaceDark, CircleShape)
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(22.dp))
        }
        Text(text = label, color = TextSecondary, fontSize = 8.sp, fontWeight = FontWeight.Bold)
    }
}