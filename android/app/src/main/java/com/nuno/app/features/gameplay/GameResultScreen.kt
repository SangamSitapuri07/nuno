package com.nuno.app.features.gameplay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*

@Composable
fun GameResultScreen(
    isWinner: Boolean,
    winnerName: String,
    duration: Int,
    totalTurns: Int,
    xpEarned: Int = 0,
    coinsEarned: Int = 0,
    ratingChange: Int = 0,
    onPlayAgain: () -> Unit,
    onHome: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = if (isWinner) {
                        listOf(SuccessGreen.copy(alpha = 0.3f), BackgroundDark, BackgroundDarker)
                    } else {
                        listOf(DangerRed.copy(alpha = 0.2f), BackgroundDark, BackgroundDarker)
                    }
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.PaddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Result Icon
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = if (isWinner) {
                                listOf(AccentGold, PrimaryPurple)
                            } else {
                                listOf(NeutralGray600, SurfaceDark)
                            }
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = if (isWinner) AccentGold else NeutralGray400,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(Dimensions.SpaceLarge))

            // Result Text
            Text(
                text = if (isWinner) "VICTORY!" else "DEFEAT",
                color = if (isWinner) AccentGold else DangerRed,
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Dimensions.SpaceSmall))

            Text(
                text = if (isWinner) "Congratulations!" else "Better luck next time",
                color = TextSecondary,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(Dimensions.SpaceXLarge))

            // Rewards Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(Dimensions.RadiusMedium)
            ) {
                Column(
                    modifier = Modifier.padding(Dimensions.PaddingMedium)
                ) {
                    Text(
                        text = "Rewards",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(Dimensions.SpaceMedium))

                    RewardRow(icon = "⭐", label = "XP Earned", value = "+$xpEarned")
                    RewardRow(icon = "🪙", label = "Coins", value = "+$coinsEarned")
                    if (ratingChange != 0) {
                        RewardRow(
                            icon = "📊",
                            label = "Rating",
                            value = if (ratingChange > 0) "+$ratingChange" else "$ratingChange"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimensions.SpaceMedium))

            // Match Stats Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(Dimensions.RadiusMedium)
            ) {
                Column(
                    modifier = Modifier.padding(Dimensions.PaddingMedium)
                ) {
                    Text(
                        text = "Match Stats",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(Dimensions.SpaceMedium))

                    StatRow(label = "Duration", value = formatDuration(duration))
                    StatRow(label = "Total Turns", value = totalTurns.toString())
                }
            }

            Spacer(modifier = Modifier.height(Dimensions.SpaceXLarge))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.SpaceSmall)
            ) {
                OutlinedButton(
                    onClick = onHome,
                    modifier = Modifier
                        .weight(1f)
                        .height(Dimensions.ButtonHeightLarge),
                    shape = RoundedCornerShape(Dimensions.RadiusMedium)
                ) {
                    Icon(Icons.Default.Home, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("HOME", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onPlayAgain,
                    modifier = Modifier
                        .weight(1f)
                        .height(Dimensions.ButtonHeightLarge),
                    shape = RoundedCornerShape(Dimensions.RadiusMedium),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("PLAY AGAIN", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun RewardRow(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, color = TextSecondary, fontSize = 14.sp)
        }
        Text(
            text = value,
            color = AccentGold,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextSecondary, fontSize = 14.sp)
        Text(
            text = value,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "%d:%02d".format(minutes, secs)
}