package com.nuno.app.features.leaderboard.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.leaderboard.LeaderboardEntry

@Composable
fun TopThreeSection(entries: List<LeaderboardEntry>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        // 2nd place
        entries.getOrNull(1)?.let {
            TopThreeCard(entry = it, position = 2, medalColor = Color(0xFFC0C0C0), modifier = Modifier.weight(1f).height(160.dp))
        } ?: Box(modifier = Modifier.weight(1f))

        // 1st place (larger)
        entries.getOrNull(0)?.let {
            TopThreeCard(entry = it, position = 1, medalColor = AccentGold, modifier = Modifier.weight(1.15f).height(190.dp))
        } ?: Box(modifier = Modifier.weight(1.15f))

        // 3rd place
        entries.getOrNull(2)?.let {
            TopThreeCard(entry = it, position = 3, medalColor = Color(0xFFCD7F32), modifier = Modifier.weight(1f).height(150.dp))
        } ?: Box(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun TopThreeCard(entry: LeaderboardEntry, position: Int, medalColor: Color, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "top")
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = "glow"
    )

    Card(
        modifier = modifier
            .shadow(16.dp, RoundedCornerShape(16.dp), spotColor = medalColor.copy(alpha = glow)),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, medalColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = when (position) { 1 -> "🥇"; 2 -> "🥈"; else -> "🥉" }, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        brush = Brush.radialGradient(colors = listOf(PrimaryBlue, PrimaryPurple)),
                        shape = CircleShape
                    )
                    .border(3.dp, medalColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = entry.username.firstOrNull()?.uppercase() ?: "?",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = entry.username, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(text = "${entry.tier} ${entry.division}", color = medalColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(text = "${entry.rating} pts", color = AccentGold, fontSize = 14.sp, fontWeight = FontWeight.Black)
        }
    }
}