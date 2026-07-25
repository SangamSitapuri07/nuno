package com.nuno.app.features.history.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.history.models.MatchRecord

@Composable
fun MatchRecordCard(match: MatchRecord, onClick: () -> Unit = {}) {
    val resultColor = if (match.isWinner) SuccessGreen else DangerRed

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, resultColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Result indicator
            Column(
                modifier = Modifier.width(60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = if (match.isWinner) "🏆" else "💔", fontSize = 28.sp)
                Text(
                    text = if (match.isWinner) "WIN" else "LOSS",
                    color = resultColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Opponent avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        brush = Brush.radialGradient(colors = listOf(PrimaryBlue, PrimaryPurple)),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = match.opponentName.firstOrNull()?.uppercase() ?: "?",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Match info
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "vs ${match.opponentName}", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = match.gameMode, color = AccentCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(text = " • ", color = TextTertiary, fontSize = 10.sp)
                    Text(text = formatDuration(match.duration), color = TextSecondary, fontSize = 10.sp)
                    Text(text = " • ", color = TextTertiary, fontSize = 10.sp)
                    Text(text = "${match.cardsPlayed} cards", color = TextSecondary, fontSize = 10.sp)
                }
                Text(text = match.playedAt, color = TextTertiary, fontSize = 10.sp)
            }

            // Rewards
            Column(horizontalAlignment = Alignment.End) {
                if (match.ratingChange != 0) {
                    Text(
                        text = if (match.ratingChange > 0) "+${match.ratingChange}" else "${match.ratingChange}",
                        color = if (match.ratingChange > 0) SuccessGreen else DangerRed,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(text = "Rating", color = TextTertiary, fontSize = 9.sp)
                }
                Text(text = "+${match.xpEarned} XP", color = AccentGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(text = "+${match.coinsEarned} 🪙", color = AccentGold, fontSize = 10.sp)
            }

            Icon(
                Icons.Default.KeyboardArrowRight,
                null,
                tint = TextTertiary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "%d:%02d".format(minutes, secs)
}