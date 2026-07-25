package com.nuno.app.features.matchmaking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.matchmaking.components.GameModeCard
import com.nuno.app.features.matchmaking.models.GameMode
import com.nuno.app.features.matchmaking.models.GameModes
import androidx.compose.foundation.border

@Composable
fun GameModeSelectionScreen(
    onBack: () -> Unit,
    onStartMatch: (GameMode) -> Unit,
    onCreateRoom: () -> Unit,
    onJoinRoom: () -> Unit
) {
    var selectedMode by remember { mutableStateOf<GameMode?>(GameModes.all[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(BackgroundDark, BackgroundDarker)))
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextPrimary)
                }
                Text(
                    text = "SELECT GAME MODE",
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                )
            }
        }

        // Mode Carousel
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(GameModes.all) { mode ->
                GameModeCard(
                    mode = mode,
                    isSelected = selectedMode?.id == mode.id,
                    onClick = { selectedMode = mode }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Selected mode details
        selectedMode?.let { mode ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RewardCard(icon = "🪙", label = "COINS", value = "+${mode.coinReward}", modifier = Modifier.weight(1f))
                RewardCard(icon = "⭐", label = "XP", value = "+${mode.xpReward}", modifier = Modifier.weight(1f))
                RewardCard(icon = "⏱️", label = "TIME", value = mode.queueTime, modifier = Modifier.weight(1f))
                RewardCard(icon = "🎯", label = "RANK", value = mode.minRank, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (mode.id == "custom") {
                    Button(
                        onClick = onCreateRoom,
                        modifier = Modifier.weight(1f).height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("CREATE ROOM", fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                    }
                    Button(
                        onClick = onJoinRoom,
                        modifier = Modifier.weight(1f).height(60.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WarningOrange),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("JOIN WITH CODE", fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                    }
                } else {
                    Button(
                        onClick = { onStartMatch(mode) },
                        modifier = Modifier.fillMaxWidth().height(70.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(colors = mode.gradient),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .border(2.dp, AccentGold, RoundedCornerShape(20.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.PlayArrow, null, tint = TextPrimary, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "START MATCH",
                                    color = TextPrimary,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 3.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RewardCard(icon: String, label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderPurple.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = label, color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(text = value, color = AccentGold, fontSize = 14.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

private val Color: androidx.compose.ui.graphics.Color.Companion = androidx.compose.ui.graphics.Color.Companion