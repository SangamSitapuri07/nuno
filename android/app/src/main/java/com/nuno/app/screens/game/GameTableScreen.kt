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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.R
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
        val screenHeight = this.maxHeight

        // Game Table Background Image Asset (R.drawable.nuno_game_table)
        Image(
            painter = painterResource(id = R.drawable.nuno_game_table),
            contentDescription = "NUNO Game Table",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dynamic adaptive heights for complete phone compatibility
        val topBarHeight = 38.dp
        val topOpponentTop = topBarHeight + 2.dp
        val centerPaddingTop = (screenHeight * 0.30f).coerceIn(105.dp, 135.dp)
        val centerPaddingBottom = (screenHeight * 0.22f).coerceIn(65.dp, 85.dp)

        val centerCardSize = if (screenHeight < 360.dp) CardSize.MEDIUM else CardSize.LARGE
        val topCardSize = if (screenHeight < 360.dp) CardSize.XSMALL else CardSize.SMALL

        // TOP BAR
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

        // CENTER TABLE ARENA
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = centerPaddingTop, bottom = centerPaddingBottom),
            contentAlignment = Alignment.Center
        ) {
            // Direction Circular Arrows
            Text(
                text = if (direction == "CLOCKWISE") "↻" else "↺",
                color = Color(0xFFFFB300).copy(alpha = 0.25f),
                fontSize = if (screenHeight < 360.dp) 85.sp else 110.sp,
                fontWeight = FontWeight.Black
            )

            // Center Draw + Discard Piles
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Draw Pile (with drawPileCount badge)
                Box(modifier = Modifier.clickable(enabled = isMyTurn) { onDrawCard() }) {
                    UnoCardBack(size = centerCardSize)
                    if (drawPileCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 4.dp, y = (-4).dp)
                                .background(Color(0xFF0F142A), CircleShape)
                                .border(1.dp, Color(0xFFFFD700), CircleShape)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text("$drawPileCount", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Discard Pile
                topCard?.let { card ->
                    UnoCard(card = card, size = centerCardSize)
                }
            }

            // "Current Card" Pill
            CurrentCardPanel(
                topCard = topCard,
                currentColor = currentColor,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = if (screenHeight < 360.dp) 72.dp else 88.dp, y = 0.dp)
            )
        }

        // OPPONENTS (Top, Left, Right)
        OpponentsLayout(
            opponents = opponents,
            topOpponentTop = topOpponentTop,
            topCardSize = topCardSize
        )

        // BOTTOM LEFT - YOU, QUICK CHAT & EMOTE BUTTON
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 10.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IconButton(
                onClick = onEmotes,
                modifier = Modifier
                    .size(34.dp)
                    .background(Color(0xFF0F142A).copy(alpha = 0.85f), CircleShape)
            ) {
                Text("😀", fontSize = 18.sp)
            }

            IconButton(
                onClick = onQuickChat,
                modifier = Modifier
                    .size(34.dp)
                    .background(Color(0xFF0F142A).copy(alpha = 0.85f), CircleShape)
            ) {
                Text("💬", fontSize = 16.sp)
            }

            PlayerAvatarBadge(username = "You", level = 21, trophies = 1500, badgeColor = Color(0xFFE50914))
        }

        // BOTTOM - Player Hand Fan
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 4.dp)
        ) {
            MyHandSection(
                cards = myHand,
                currentColor = currentColor,
                currentValue = topCard?.value ?: "",
                isMyTurn = isMyTurn,
                onPlayCard = onPlayCard
            )
        }

        // BOTTOM RIGHT - Timer Ring & NUNO! Claim Button
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 10.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Timer(remainingTime = remainingTime)

            if (showUnoButton || myHand.size <= 2) {
                Button(
                    onClick = onUnoCall,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3D00)),
                    shape = RoundedCornerShape(18.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 2.dp),
                    modifier = Modifier
                        .height(38.dp)
                        .shadow(8.dp, RoundedCornerShape(18.dp), spotColor = Color(0xFFFF3D00))
                ) {
                    Text("UNO!", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                }
            }
        }
    }
}

@Composable
private fun OpponentsLayout(
    opponents: List<OpponentData>,
    topOpponentTop: Dp,
    topCardSize: CardSize
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // TOP OPPONENT
        opponents.getOrNull(0)?.let { top ->
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = topOpponentTop),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    PlayerAvatarBadge(username = top.username, level = 23, trophies = 1250, badgeColor = Color(0xFF1976D2))
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(horizontalArrangement = Arrangement.spacedBy((-12).dp)) {
                    repeat(top.cardCount.coerceAtMost(7)) {
                        UnoCardBack(size = topCardSize)
                    }
                }
            }
        }

        // LEFT OPPONENT
        opponents.getOrNull(1)?.let { left ->
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PlayerAvatarBadge(username = left.username, level = 19, trophies = 980, badgeColor = Color(0xFF388E3C))
                Spacer(modifier = Modifier.height(2.dp))
                Row(horizontalArrangement = Arrangement.spacedBy((-12).dp)) {
                    repeat(left.cardCount.coerceAtMost(6)) {
                        UnoCardBack(size = topCardSize)
                    }
                }
            }
        }

        // RIGHT OPPONENT
        opponents.getOrNull(2)?.let { right ->
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PlayerAvatarBadge(username = right.username, level = 18, trophies = 1110, badgeColor = Color(0xFFFBC02D))
                Spacer(modifier = Modifier.height(2.dp))
                Row(horizontalArrangement = Arrangement.spacedBy((-12).dp)) {
                    repeat(right.cardCount.coerceAtMost(6)) {
                        UnoCardBack(size = topCardSize)
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerAvatarBadge(username: String, level: Int, trophies: Int, badgeColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box {
            GameAvatar(username = username, size = 28.dp, borderColor = badgeColor)
            Box(
                modifier = Modifier
                    .size(13.dp)
                    .background(badgeColor, CircleShape)
                    .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
            ) {
                Text("$level", color = Color.White, fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
            }
        }
        Box(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(5.dp))
                .padding(horizontal = 5.dp, vertical = 2.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(username, color = Color.White, fontSize = 8.5.sp, fontWeight = FontWeight.Bold)
                Text("🏆 $trophies", color = Color(0xFFFFD700), fontSize = 7.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun MyHandSection(
    cards: List<GameCardData>,
    currentColor: String,
    currentValue: String,
    isMyTurn: Boolean,
    onPlayCard: (GameCardData) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(if (cards.size > 8) (-18).dp else (-12).dp),
        modifier = Modifier.padding(horizontal = 65.dp)
    ) {
        cards.forEachIndexed { index, card ->
            val isPlayable = isMyTurn && (card.type == "WILD" || card.color == currentColor || card.value == currentValue)
            UnoCardView(
                card = card,
                size = CardSize.MEDIUM,
                isPlayable = isPlayable,
                onClick = { onPlayCard(card) }
            )
        }
    }
}