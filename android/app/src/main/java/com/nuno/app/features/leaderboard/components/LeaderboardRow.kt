package com.nuno.app.features.leaderboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.leaderboard.LeaderboardEntry

@Composable
fun LeaderboardRow(entry: LeaderboardEntry, isCurrentPlayer: Boolean = false) {
    val bgColor = if (isCurrentPlayer) PrimaryPurple.copy(alpha = 0.3f) else SurfaceCard

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            if (isCurrentPlayer) 2.dp else 1.dp,
            if (isCurrentPlayer) AccentCyan else BorderPurple.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#${entry.rank}",
                color = when (entry.rank) {
                    1 -> AccentGold
                    2 -> Color(0xFFC0C0C0)
                    3 -> Color(0xFFCD7F32)
                    else -> TextTertiary
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.width(50.dp)
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        brush = Brush.radialGradient(colors = listOf(PrimaryBlue, PrimaryPurple)),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = entry.username.firstOrNull()?.uppercase() ?: "?",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = entry.username, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    if (isCurrentPlayer) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "YOU", color = AccentCyan, fontSize = 10.sp, fontWeight = FontWeight.Black)
                    }
                }
                Text(text = "${entry.tier} ${entry.division} • ${entry.country}", color = TextSecondary, fontSize = 11.sp)
            }

            if (entry.rankChange != 0) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (entry.rankChange > 0) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        null,
                        tint = if (entry.rankChange > 0) SuccessGreen else DangerRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = kotlin.math.abs(entry.rankChange).toString(),
                        color = if (entry.rankChange > 0) SuccessGreen else DangerRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = entry.rating.toString(), color = AccentGold, fontSize = 16.sp, fontWeight = FontWeight.Black)
                Text(text = "${entry.wins} wins", color = TextSecondary, fontSize = 10.sp)
            }
        }
    }
}