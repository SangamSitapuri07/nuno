package com.nuno.app.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.ButtonStyle
import com.nuno.app.core.designsystem.components.GameButton

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
    val infiniteTransition = rememberInfiniteTransition(label = "mmPremium")
    val rotation by infiniteTransition.animateFloat(0f, 360f, infiniteRepeatable(tween(3000, easing = LinearEasing)), label = "rot")
    val pulse by infiniteTransition.animateFloat(0.85f, 1.18f, infiniteRepeatable(tween(1400), RepeatMode.Reverse), label = "pulse")
    val glow by infiniteTransition.animateFloat(0.4f, 0.85f, infiniteRepeatable(tween(1500), RepeatMode.Reverse), label = "glow")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Background)
    ) {
        PremiumGameTableBackground()
        PremiumCosmicParticles()

        // Back button premium
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
                .size(40.dp)
                .background(Color.White.copy(0.08f), RoundedCornerShape(12.dp))
                .border(1.dp, Color.White.copy(0.12f), RoundedCornerShape(12.dp))
                .clickable {
                    onCancelSearch()
                    onBack()
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(20.dp))
        }

        when (status) {
            MatchmakingStatus.IDLE, MatchmakingStatus.CONNECTING -> {
                var selectedPlayerCount by remember { mutableIntStateOf(4) }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "FIND A MATCH",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 4.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Choose players & mode", color = Color(0xFF8B92C0), fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Player count
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(0.06f), RoundedCornerShape(16.dp))
                            .border(1.dp, Color.White.copy(0.08f), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("PLAYERS", color = Color(0xFF8B92C0), fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf(2, 3, 4, 5, 6, 7).forEach { count ->
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .shadow(
                                                if (selectedPlayerCount == count) 12.dp else 0.dp,
                                                RoundedCornerShape(12.dp),
                                                spotColor = GameColors.Gold.copy(0.5f)
                                            )
                                            .background(
                                                if (selectedPlayerCount == count) Brush.linearGradient(listOf(Color(0xFFFFD23F), Color(0xFFFF9A00)))
                                                else Brush.linearGradient(listOf(Color(0xFF1E2249), Color(0xFF131636))),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .border(
                                                1.dp,
                                                if (selectedPlayerCount == count) Color.White.copy(0.4f) else Color.White.copy(0.06f),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .clickable { selectedPlayerCount = count },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            count.toString(),
                                            color = if (selectedPlayerCount == count) Color.Black else Color.White,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        PremiumModeCard(
                            title = "CASUAL",
                            subtitle = "Play for fun\nNo rating risk",
                            badge = "FUN",
                            gradient = listOf(Color(0xFF00D9FF), Color(0xFF3B6BFF)),
                            onClick = { onStartSearch("CASUAL", selectedPlayerCount) }
                        )
                        PremiumModeCard(
                            title = "RANKED",
                            subtitle = "Competitive\nClimb leaderboard",
                            badge = "PRO",
                            gradient = listOf(Color(0xFFFF3D8B), Color(0xFF7B5CFF)),
                            onClick = { onStartSearch("RANKED", selectedPlayerCount) }
                        )
                    }

                    if (status == MatchmakingStatus.CONNECTING) {
                        Spacer(modifier = Modifier.height(28.dp))
                        CircularProgressIndicator(color = GameColors.Cyan, modifier = Modifier.size(28.dp), strokeWidth = 3.dp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Connecting to servers...", color = Color(0xFF8B92C0), fontSize = 11.sp)
                    }
                }
            }

            MatchmakingStatus.SEARCHING -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("FINDING OPPONENTS", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Matching you with players of similar skill", color = Color(0xFF8B92C0), fontSize = 11.sp)

                    Spacer(modifier = Modifier.height(36.dp))

                    Box(modifier = Modifier.size(240.dp), contentAlignment = Alignment.Center) {
                        // Outer glow rings
                        Box(
                            modifier = Modifier
                                .size(240.dp)
                                .background(
                                    Brush.radialGradient(
                                        listOf(GameColors.Purple.copy(0.15f * glow), Color.Transparent)
                                    ),
                                    CircleShape
                                )
                                .blur(8.dp)
                        )

                        Box(
                            modifier = Modifier
                                .size(220.dp)
                                .border(1.dp, GameColors.Purple.copy(0.15f), CircleShape)
                                .rotate(rotation)
                        )
                        Box(
                            modifier = Modifier
                                .size(170.dp)
                                .border(1.5.dp, GameColors.Purple.copy(0.35f), CircleShape)
                                .rotate(-rotation * 1.3f)
                        )
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .border(2.dp, GameColors.Cyan, CircleShape)
                                .shadow(12.dp, CircleShape, spotColor = GameColors.Cyan.copy(glow))
                                .scale(pulse)
                        )
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .shadow(20.dp, CircleShape, spotColor = GameColors.Purple.copy(0.6f))
                                .background(
                                    Brush.radialGradient(listOf(Color(0xFF7B5CFF), Color(0xFF3A2A8A))),
                                    CircleShape
                                )
                                .border(2.dp, Color.White.copy(0.4f), CircleShape)
                                .scale(pulse),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎮", fontSize = 30.sp)
                        }

                        // Orbit dots
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .offset(y = (-85).dp)
                                .rotate(rotation * 2)
                                .background(GameColors.Cyan, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(0.06f), RoundedCornerShape(16.dp))
                            .border(1.dp, Color.White.copy(0.08f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(formatTime(elapsedSeconds), color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                            Text("Searching...", color = GameColors.Cyan, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    GameButton(
                        text = "CANCEL SEARCH",
                        onClick = onCancelSearch,
                        style = ButtonStyle.DANGER,
                        modifier = Modifier.width(220.dp).height(48.dp)
                    )
                }
            }

            MatchmakingStatus.MATCH_FOUND, MatchmakingStatus.GAME_STARTING -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .shadow(24.dp, CircleShape, spotColor = GameColors.Green.copy(0.5f))
                            .background(Brush.linearGradient(listOf(Color(0xFF00E676), Color(0xFF00B248))), CircleShape)
                            .border(3.dp, Color.White.copy(0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✓", fontSize = 58.sp, color = Color.White, fontWeight = FontWeight.Black)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("MATCH FOUND!", color = GameColors.Green, fontSize = 30.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Preparing your game table...", color = Color(0xFF8B92C0), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    CircularProgressIndicator(color = GameColors.Green, modifier = Modifier.size(28.dp))
                }
            }

            MatchmakingStatus.ERROR -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .background(Brush.linearGradient(listOf(Color(0xFFFF3B5C), Color(0xFFD5002B))), CircleShape)
                            .border(2.dp, Color.White.copy(0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("!", fontSize = 48.sp, color = Color.White, fontWeight = FontWeight.Black)
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(errorMessage ?: "Something went wrong", color = GameColors.Red, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Please try again", color = Color(0xFF8B92C0), fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(28.dp))
                    GameButton(text = "GO BACK", onClick = onBack, style = ButtonStyle.PRIMARY, modifier = Modifier.width(200.dp).height(48.dp))
                }
            }
        }
    }
}

@Composable
private fun PremiumModeCard(title: String, subtitle: String, badge: String, gradient: List<Color>, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(150.dp)
            .shadow(20.dp, RoundedCornerShape(22.dp), spotColor = gradient[0].copy(0.45f))
            .background(Brush.linearGradient(gradient), RoundedCornerShape(22.dp))
            .border(1.5.dp, Color.White.copy(0.25f), RoundedCornerShape(22.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(0.2f), RoundedCornerShape(8.dp))
                        .border(1.dp, Color.White.copy(0.2f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(badge, color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                }
                Text("→", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Column {
                Text(title, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtitle, color = Color.White.copy(0.8f), fontSize = 11.sp, fontWeight = FontWeight.Medium, lineHeight = 13.sp)
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return "%02d:%02d".format(min, sec)
}
