package com.nuno.app.features.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.profile.models.MatchHistoryItem

@Composable
fun MatchHistoryList(matches: List<MatchHistoryItem>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "RECENT MATCHES",
            color = AccentCyan,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (matches.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, BorderPurple.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "No matches played yet. Start playing to see your history!",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                items(matches) { match ->
                    MatchHistoryRow(match = match)
                }
            }
        }
    }
}

@Composable
private fun MatchHistoryRow(match: MatchHistoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, if (match.isWinner) SuccessGreen.copy(alpha = 0.5f) else DangerRed.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (match.isWinner) "🏆" else "💔",
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (match.isWinner) "VICTORY" else "DEFEAT",
                    color = if (match.isWinner) SuccessGreen else DangerRed,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "vs ${match.opponentName}", color = TextPrimary, fontSize = 11.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (match.ratingChange >= 0) "+${match.ratingChange}" else "${match.ratingChange}",
                    color = if (match.ratingChange >= 0) SuccessGreen else DangerRed,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "+${match.xpEarned} XP", color = AccentGold, fontSize = 10.sp)
            }
        }
    }
}