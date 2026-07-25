package com.nuno.app.features.profile.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.network.UserStatistics
import com.nuno.app.core.theme.*

data class StatItem(val icon: String, val label: String, val value: String, val color: Color)

@Composable
fun StatisticsGrid(stats: UserStatistics?, modifier: Modifier = Modifier) {
    val items = remember(stats) {
        listOf(
            StatItem("🎮", "Matches", stats?.gamesPlayed?.toString() ?: "0", AccentCyan),
            StatItem("🏆", "Wins", stats?.gamesWon?.toString() ?: "0", SuccessGreen),
            StatItem("💔", "Losses", stats?.gamesLost?.toString() ?: "0", DangerRed),
            StatItem("📊", "Win Rate", "${((stats?.winRate ?: 0f) * 100).toInt()}%", AccentGold),
            StatItem("🔥", "Best Streak", stats?.longestWinStreak?.toString() ?: "0", WarningOrange),
            StatItem("⚡", "Current", stats?.currentWinStreak?.toString() ?: "0", AccentPink),
            StatItem("🎴", "Cards Played", stats?.cardsPlayed?.toString() ?: "0", PrimaryBlue),
            StatItem("📥", "Cards Drawn", stats?.cardsDrawn?.toString() ?: "0", PrimaryPurple)
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.chunked(4).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { item ->
                    StatCard(item = item, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun StatCard(item: StatItem, modifier: Modifier = Modifier) {
    val animatedValue = item.value.toIntOrNull() ?: 0
    val displayValue by animateIntAsState(
        targetValue = animatedValue,
        animationSpec = tween(1000),
        label = "count"
    )

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, item.color.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = item.icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (item.value.contains("%")) item.value else displayValue.toString(),
                color = item.color,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )
            Text(text = item.label, color = TextSecondary, fontSize = 10.sp)
        }
    }
}