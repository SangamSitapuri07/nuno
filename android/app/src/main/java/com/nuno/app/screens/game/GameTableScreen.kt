package com.nuno.app.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.GameAvatar
import com.nuno.app.core.designsystem.components.GamePanel

data class GameCardData(
    val cardId: String,
    val color: String,
    val value: String,
    val type: String
)

data class OpponentData(
    val userId: String,
    val username: String,
    val cardCount: Int,
    val isCurrentTurn: Boolean
)

@Composable
fun GameTableScreen(
    opponents: List<OpponentData>,
    myHand: List<GameCardData>,
    topCard: GameCardData?,
    currentColor: String,
    drawPileCount: Int,
    isMyTurn: Boolean,
    remainingTime: Int,
    direction: String,
    totalTurns: Int,
    roomCode: String,
    onPlayCard: (GameCardData) -> Unit,
    onDrawCard: () -> Unit,
    onChat: () -> Unit,
    onQuickChat: () -> Unit,
    onVoice: () -> Unit,
    onEmotes: () -> Unit,
    onMenu: () -> Unit,
    onUnoCall: () -> Unit,
    showUnoButton: Boolean
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        GameTableBackground()

        Row(modifier = Modifier.fillMaxSize()) {
            // LEFT SIDEBAR
            LeftSidebar(
                onChat = onChat,
                onQuickChat = onQuickChat,
                onVoice = onVoice,
                onEmotes = onEmotes,
                onMenu = onMenu,
                onUnoCall = onUnoCall,
                showUnoButton = showUnoButton
            )

            // MAIN GAME AREA
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(GameDimens.paddingSm)
            ) {
                // TOP - Opponents + Room Info
                TopSection(
                    opponents = opponents,
                    roomCode = roomCode,
                    remainingTime = remainingTime
                )

                // CENTER - Draw + Discard Piles
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CenterPiles(
                        topCard = topCard,
                        currentColor = currentColor,
                        drawPileCount = drawPileCount,
                        direction = direction,
                        isMyTurn = isMyTurn,
                        onDrawCard = onDrawCard
                    )

                    // YOUR TURN indicator
                    if (isMyTurn) {
                        Text(
                            text = "YOUR TURN",
                            color = GameColors.Green,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 4.dp)
                                .background(GameColors.Green.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }

                // BOTTOM - My Hand
                MyHandSection(
                    cards = myHand,
                    currentColor = currentColor,
                    currentValue = topCard?.value ?: "",
                    isMyTurn = isMyTurn,
                    onPlayCard = onPlayCard
                )
            }

            // RIGHT SIDEBAR
            RightSidebar(
                remainingTime = remainingTime,
                isMyTurn = isMyTurn,
                direction = direction,
                totalTurns = totalTurns
            )
        }
    }
}

// ─────────────────────────────────────────
// BACKGROUND
// ─────────────────────────────────────────

@Composable
private fun GameTableBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1A0B3D),
                        GameColors.Background,
                        GameColors.BackgroundDark
                    )
                )
            )
    )
}

// ─────────────────────────────────────────
// LEFT SIDEBAR
// ─────────────────────────────────────────

@Composable
private fun LeftSidebar(
    onChat: () -> Unit,
    onQuickChat: () -> Unit,
    onVoice: () -> Unit,
    onEmotes: () -> Unit,
    onMenu: () -> Unit,
    onUnoCall: () -> Unit,
    showUnoButton: Boolean
) {
    Column(
        modifier = Modifier
            .width(60.dp)
            .fillMaxHeight()
            .background(GameColors.BackgroundDark.copy(alpha = 0.7f))
            .padding(vertical = GameDimens.paddingSm),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        SideButton(icon = Icons.Default.Mic, label = "Voice", onClick = onVoice, color = GameColors.Cyan)
        SideButton(icon = Icons.Default.Chat, label = "Chat", onClick = onChat, color = GameColors.Blue)
        SideButton(icon = Icons.Default.Message, label = "Quick", onClick = onQuickChat, color = GameColors.Green)
        SideButton(icon = Icons.Default.EmojiEmotions, label = "Emotes", onClick = onEmotes, color = GameColors.Gold)

        Spacer(modifier = Modifier.weight(1f))

        if (showUnoButton) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .shadow(8.dp, CircleShape, spotColor = GameColors.Red)
                    .background(GameColors.Red, CircleShape)
                    .clickable { onUnoCall() },
                contentAlignment = Alignment.Center
            ) {
                Text("UNO!", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
            }
        }

        SideButton(icon = Icons.Default.Menu, label = "Menu", onClick = onMenu, color = GameColors.TextGray)
    }
}

@Composable
private fun SideButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(GameColors.Surface, CircleShape)
                .border(1.dp, color.copy(alpha = 0.3f), CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        }
        Text(text = label, color = GameColors.TextGray, fontSize = 8.sp)
    }
}

// ─────────────────────────────────────────
// TOP SECTION - Opponents
// ─────────────────────────────────────────

@Composable
private fun TopSection(
    opponents: List<OpponentData>,
    roomCode: String,
    remainingTime: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = GameDimens.paddingSm),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top
    ) {
        opponents.take(6).forEach { opponent ->
            OpponentSeat(opponent = opponent)
        }
    }
}

@Composable
private fun OpponentSeat(opponent: OpponentData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            GameAvatar(
                username = opponent.username,
                size = 44.dp,
                borderColor = if (opponent.isCurrentTurn) GameColors.Gold else GameColors.Blue
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = opponent.username,
            color = GameColors.TextWhite,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(GameColors.Surface, RoundedCornerShape(8.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(text = "🎴", fontSize = 10.sp)
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = opponent.cardCount.toString(),
                color = GameColors.Cyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

// ─────────────────────────────────────────
// CENTER - Draw + Discard Piles
// ─────────────────────────────────────────

@Composable
private fun CenterPiles(
    topCard: GameCardData?,
    currentColor: String,
    drawPileCount: Int,
    direction: String,
    isMyTurn: Boolean,
    onDrawCard: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Draw Pile
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(width = 90.dp, height = 130.dp)
                    .shadow(12.dp, RoundedCornerShape(10.dp), spotColor = GameColors.Purple)
                    .background(
                        brush = Brush.linearGradient(listOf(GameColors.Blue, GameColors.Purple)),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(2.dp, GameColors.Gold, RoundedCornerShape(10.dp))
                    .clickable(enabled = isMyTurn) { onDrawCard() },
                contentAlignment = Alignment.Center
            ) {
                Text("NUNO", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "$drawPileCount", color = GameColors.TextGray, fontSize = 10.sp)
        }

        // Direction arrow
        Text(
            text = if (direction == "CLOCKWISE") "→" else "←",
            color = GameColors.Gold,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black
        )

        // Discard Pile
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (topCard != null) {
                UnoCardView(card = topCard, size = CardSize.LARGE)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = currentColor,
                color = getUnoColor(currentColor),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ─────────────────────────────────────────
// BOTTOM - My Hand
// ─────────────────────────────────────────

@Composable
private fun MyHandSection(
    cards: List<GameCardData>,
    currentColor: String,
    currentValue: String,
    isMyTurn: Boolean,
    onPlayCard: (GameCardData) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = GameDimens.paddingSm)) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(horizontal = GameDimens.paddingSm),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(cards) { card ->
                val isPlayable = isMyTurn && isCardPlayable(card, currentColor, currentValue)
                UnoCardView(
                    card = card,
                    size = CardSize.MEDIUM,
                    isPlayable = isPlayable,
                    onClick = { onPlayCard(card) }
                )
            }
        }
    }
}

// ─────────────────────────────────────────
// RIGHT SIDEBAR
// ─────────────────────────────────────────

@Composable
private fun RightSidebar(
    remainingTime: Int,
    isMyTurn: Boolean,
    direction: String,
    totalTurns: Int
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .fillMaxHeight()
            .padding(GameDimens.paddingSm),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Timer
        GamePanel(borderColor = if (isMyTurn) GameColors.Green else GameColors.BorderPurple.copy(alpha = 0.3f)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(GameDimens.paddingSm),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isMyTurn) "YOUR TURN" else "OPPONENT",
                    color = if (isMyTurn) GameColors.Green else GameColors.TextGray,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${remainingTime}s",
                    color = GameColors.TextWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Direction
        GamePanel {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(GameDimens.paddingSm),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("DIRECTION", color = GameColors.TextGray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = if (direction == "CLOCKWISE") "↻" else "↺",
                    color = GameColors.Cyan,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Turns
        GamePanel {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(GameDimens.paddingSm),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("TURNS", color = GameColors.TextGray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = totalTurns.toString(),
                    color = GameColors.TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

// ─────────────────────────────────────────
// HELPERS
// ─────────────────────────────────────────

private fun isCardPlayable(card: GameCardData, currentColor: String, currentValue: String): Boolean {
    if (card.type == "WILD") return true
    if (card.color == currentColor) return true
    if (card.value == currentValue) return true
    return false
}

private fun getUnoColor(color: String): Color = when (color) {
    "RED" -> GameColors.CardRed
    "BLUE" -> GameColors.CardBlue
    "GREEN" -> GameColors.CardGreen
    "YELLOW" -> GameColors.CardYellow
    else -> GameColors.TextWhite
}