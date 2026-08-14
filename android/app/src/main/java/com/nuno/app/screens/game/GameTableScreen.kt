package com.nuno.app.screens.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.components.GameAvatar

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
    isMicMuted: Boolean = true,
    isSpeakerMuted: Boolean = true,
    onPlayCard: (GameCardData) -> Unit,
    onDrawCard: () -> Unit,
    onChat: () -> Unit,
    onQuickChat: () -> Unit,
    onVoice: () -> Unit,
    onSpeaker: () -> Unit = {},
    onEmotes: () -> Unit,
    onMenu: () -> Unit,
    onUnoCall: () -> Unit,
    showUnoButton: Boolean
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWide = maxWidth > maxHeight

        // Reference Screen 7/8: Green & Red table - use green for YOUR TURN
        if (isMyTurn) {
            GreenPremiumTableBackground()
        } else {
            RedPremiumTableBackground()
        }

        TopBar(
            roomCode = roomCode,
            gameMode = "Classic (${totalTurns}t)",
            pingMs = 52,
            isMicMuted = isMicMuted,
            isSpeakerMuted = isSpeakerMuted,
            onChat = onChat,
            onVoice = onVoice,
            onSpeaker = onSpeaker,
            onMenu = onMenu,
            onLeaveRoom = onMenu
        )

        // Top opponent - wider higher like reference B1
        opponents.getOrNull(0)?.let { top ->
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 56.dp)
                    .width(210.dp)
                    .height(58.dp)
                    .background(Color(0xFF12152E).copy(0.85f), RoundedCornerShape(12.dp))
                    .border(1.dp, if (top.isCurrentTurn) GameColors.Cyan else Color(0xFF2A325A), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    GameAvatar(username = top.username, size = 36.dp, borderColor = GameColors.Blue, showGlow = top.isCurrentTurn)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(top.username, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(top.cardCount.coerceAtMost(5)) {
                                Box(modifier = Modifier.size(12.dp).background(Color.Black, CircleShape).border(0.5.dp, Color.White, CircleShape))
                                Spacer(modifier = Modifier.width((-4).dp))
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("${top.cardCount}", color = Color(0xFF8A8FA8), fontSize = 9.sp)
                        }
                    }
                    if (top.isCurrentTurn) {
                        Box(modifier = Modifier.background(GameColors.Green.copy(0.18f), RoundedCornerShape(6.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text("TURN", color = GameColors.Green, fontSize = 7.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // Center table - draw + discard like reference
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 110.dp, bottom = 110.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (direction == "CLOCKWISE") "↻" else "↺",
                color = Color.White.copy(alpha = 0.08f),
                fontSize = 100.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.align(Alignment.Center)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(32.dp), verticalAlignment = Alignment.CenterVertically) {
                // Draw pile
                Box(modifier = Modifier.clickable(enabled = isMyTurn) { onDrawCard() }) {
                    Box(modifier = Modifier.offset(x = 6.dp, y = 4.dp)) {
                        UnoCardBack(size = CardSize.LARGE)
                    }
                    UnoCardBack(size = CardSize.LARGE)
                    if (drawPileCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 8.dp, y = (-8).dp)
                                .background(Color(0xFF12152E), RoundedCornerShape(8.dp))
                                .border(1.dp, GameColors.Gold, RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("$drawPileCount", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                topCard?.let { card ->
                    Box(modifier = Modifier.shadow(16.dp, RoundedCornerShape(12.dp))) {
                        UnoCard(card = card, size = CardSize.LARGE)
                    }
                }
            }

            // Current color pill - like reference  B shows GREEN with dot
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 90.dp, y = 10.dp)
                    .background(Color(0xFF1A1A1A).copy(0.9f), RoundedCornerShape(10.dp))
                    .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Column {
                    Text("Current Color", color = Color(0xFF8A8FA8), fontSize = 7.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(
                            when (currentColor) {
                                "RED" -> Color.Red
                                "BLUE" -> Color(0xFF2196F3)
                                "GREEN" -> Color(0xFF4CAF50)
                                "YELLOW" -> Color(0xFFFFC107)
                                else -> GameColors.Purple
                            }, CircleShape
                        ))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(currentColor, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Left / Right opponents
        opponents.getOrNull(1)?.let { left ->
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 12.dp)
                    .background(Color(0xFF12152E).copy(0.7f), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFF2A325A), RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GameAvatar(username = left.username, size = 32.dp, borderColor = Color(0xFF4CAF50))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(left.username, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Row { repeat(left.cardCount.coerceAtMost(4)) { Box(modifier = Modifier.size(10.dp).background(Color.Black, CircleShape).border(0.5.dp, Color.White, CircleShape)) } }
                }
            }
        }

        opponents.getOrNull(2)?.let { right ->
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp)
                    .background(Color(0xFF12152E).copy(0.7f), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFF2A325A), RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GameAvatar(username = right.username, size = 32.dp, borderColor = Color(0xFFFFC107))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(right.username, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Row { repeat(right.cardCount.coerceAtMost(4)) { Box(modifier = Modifier.size(10.dp).background(Color.Black, CircleShape).border(0.5.dp, Color.White, CircleShape)) } }
                }
            }
        }

        // Bottom - You + hand + timer like reference
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF12152E), CircleShape)
                    .border(1.dp, Color(0xFF2A325A), CircleShape)
                    .clickable { onEmotes() },
                contentAlignment = Alignment.Center
            ) { Text("😀", fontSize = 16.sp) }
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF12152E), CircleShape)
                    .border(1.dp, Color(0xFF2A325A), CircleShape)
                    .clickable { onQuickChat() },
                contentAlignment = Alignment.Center
            ) { Text("💬", fontSize = 14.sp) }

            Box(
                modifier = Modifier
                    .background(Color(0xFF12152E).copy(0.85f), RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFF2A325A), RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    GameAvatar(username = "You", size = 24.dp, borderColor = Color.Red)
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text("You", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text("🏆 1500", color = GameColors.Gold, fontSize = 8.sp)
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy((-14).dp)) {
                myHand.forEach { card ->
                    val isPlayable = isMyTurn && (card.type == "WILD" || card.color == currentColor || card.value == topCard?.value)
                    UnoCardView(card = card, size = CardSize.MEDIUM, isPlayable = isPlayable, onClick = { onPlayCard(card) })
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomEnd).padding(end = 16.dp, bottom = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (showUnoButton || myHand.size <= 2) {
                    Button(
                        onClick = onUnoCall,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3D00)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(40.dp)
                    ) { Text("UNO!", color = Color.White, fontWeight = FontWeight.Black, fontSize = 12.sp) }
                }
                Timer(remainingTime = remainingTime)
            }
        }
    }
}

@Composable
private fun GreenPremiumTableBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Generated premium green table image
        Image(
            painter = painterResource(id = com.nuno.app.R.drawable.bg_game_table_green),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Overlay for depth
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.radialGradient(listOf(Color.Transparent, Color.Black.copy(0.35f)), radius = 900f))
        )
    }
}

@Composable
private fun RedPremiumTableBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Generated premium red table image
        Image(
            painter = painterResource(id = com.nuno.app.R.drawable.bg_game_table_red),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().background(Brush.radialGradient(listOf(Color.Transparent, Color.Black.copy(0.45f)), radius = 900f)))
    }
}
