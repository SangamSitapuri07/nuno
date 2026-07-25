package com.nuno.app.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.GameButton
import com.nuno.app.core.designsystem.components.ButtonStyle
import androidx.compose.foundation.clickable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.foundation.border
//import androidx.compose.ui.graphics.Color

enum class MatchmakingStatus {
    IDLE,
    CONNECTING,
    SEARCHING,
    MATCH_FOUND,
    GAME_STARTING,
    ERROR
}

@Composable
fun NewMatchmakingScreen(
    status: MatchmakingStatus,
    elapsedSeconds: Int,
    errorMessage: String?,
    onStartSearch: (String, Int) -> Unit,
    onCancelSearch: () -> Unit,
    onBack: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mm")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
        label = "rot"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF1A0B3D), GameColors.Background, GameColors.BackgroundDark)
                )
            )
    ) {
        // Back button
        IconButton(
            onClick = {
                onCancelSearch()
                onBack()
            },
            modifier = Modifier.padding(GameDimens.paddingLg)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GameColors.TextWhite)
        }

        when (status) {
            MatchmakingStatus.IDLE, MatchmakingStatus.CONNECTING -> {
                var selectedPlayerCount by remember { mutableIntStateOf(2) }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "FIND A MATCH",
                        color = GameColors.TextWhite,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 3.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Player count selection
                    Text(
                        text = "PLAYERS",
                        color = GameColors.TextGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(2, 3, 4, 5, 6, 7).forEach { count ->
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(
                                        if (selectedPlayerCount == count) GameColors.Gold else GameColors.Surface,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        width = if (selectedPlayerCount == count) 2.dp else 1.dp,
                                        color = if (selectedPlayerCount == count) GameColors.Gold else GameColors.BorderPurple.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedPlayerCount = count },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = count.toString(),
                                    color = if (selectedPlayerCount == count) Color.Black else GameColors.TextWhite,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Mode cards
                    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        ModeCard(
                            title = "CASUAL",
                            subtitle = "Play for fun\nNo rating impact",
                            gradient = listOf(GameColors.Cyan, GameColors.Blue),
                            onClick = { onStartSearch("CASUAL", selectedPlayerCount) }
                        )
                        ModeCard(
                            title = "RANKED",
                            subtitle = "Competitive\nAffects rating",
                            gradient = listOf(Color(0xFFFF3D8B), GameColors.Purple),
                            onClick = { onStartSearch("RANKED", selectedPlayerCount) }
                        )
                    }

                    if (status == MatchmakingStatus.CONNECTING) {
                        Spacer(modifier = Modifier.height(24.dp))
                        CircularProgressIndicator(color = GameColors.Cyan, modifier = Modifier.size(32.dp))
                        Text("Connecting...", color = GameColors.TextGray, fontSize = 12.sp)
                    }
                }
            }

            MatchmakingStatus.SEARCHING -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "FINDING MATCH",
                        color = GameColors.TextWhite,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 3.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Radar animation
                    Box(
                        modifier = Modifier.size(220.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(220.dp)
                                .border(2.dp, GameColors.Purple.copy(alpha = 0.3f), CircleShape)
                                .rotate(rotation)
                        )
                        Box(
                            modifier = Modifier
                                .size(160.dp)
                                .border(2.dp, GameColors.Purple.copy(alpha = 0.5f), CircleShape)
                                .rotate(-rotation)
                        )
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .border(3.dp, GameColors.Cyan, CircleShape)
                                .scale(pulse)
                        )
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(
                                    Brush.radialGradient(listOf(GameColors.Purple, Color(0xFF3A0CA3))),
                                    CircleShape
                                )
                                .scale(pulse),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎮", fontSize = 28.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = formatTime(elapsedSeconds),
                        color = GameColors.TextWhite,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text("Searching for opponent...", color = GameColors.TextGray, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(24.dp))

                    GameButton(
                        text = "CANCEL",
                        onClick = onCancelSearch,
                        style = ButtonStyle.DANGER,
                        modifier = Modifier.width(200.dp)
                    )
                }
            }

            MatchmakingStatus.MATCH_FOUND, MatchmakingStatus.GAME_STARTING -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("🎉", fontSize = 80.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "MATCH FOUND!",
                        color = GameColors.Green,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 3.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Starting game...", color = GameColors.TextGray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(color = GameColors.Green)
                }
            }

            MatchmakingStatus.ERROR -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("⚠️", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMessage ?: "Error occurred",
                        color = GameColors.Red,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    GameButton(
                        text = "GO BACK",
                        onClick = onBack,
                        style = ButtonStyle.PRIMARY,
                        modifier = Modifier.width(200.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ModeCard(
    title: String,
    subtitle: String,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(140.dp)
            .background(
                Brush.linearGradient(gradient),
                RoundedCornerShape(GameDimens.radiusLg)
            )
            .border(2.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(GameDimens.radiusLg))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

private fun formatTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return "%02d:%02d".format(min, sec)
}

