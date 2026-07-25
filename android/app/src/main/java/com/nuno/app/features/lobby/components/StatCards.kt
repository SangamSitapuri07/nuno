package com.nuno.app.features.lobby.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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

@Composable
fun RankCard(rank: String, modifier: Modifier = Modifier) {
    StatCardBase(
        title = "RANK",
        value = rank,
        icon = { RankIcon() },
        modifier = modifier
    )
}

@Composable
fun LevelCard(level: Int, modifier: Modifier = Modifier) {
    StatCardBase(
        title = "LEVEL",
        value = "Lv. $level",
        icon = { LevelIcon() },
        modifier = modifier
    )
}

@Composable
fun CoinCard(coins: Int, modifier: Modifier = Modifier) {
    StatCardBase(
        title = "COINS",
        value = coins.toString(),
        icon = { CoinIcon() },
        modifier = modifier
    )
}

@Composable
private fun StatCardBase(
    title: String,
    value: String,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cardGlow")
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(16.dp), spotColor = Color(0xFFFFD700).copy(alpha = glow)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D33).copy(alpha = 0.85f)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            2.dp,
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFFFFD700),
                    Color(0xFFB8860B),
                    Color(0xFFFFD700)
                )
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color(0xFF8B92B8),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
            }
            icon()
        }
    }
}

@Composable
private fun RankIcon() {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFCD7F32), Color(0xFF8B4513))
                ),
                shape = CircleShape
            )
            .border(2.dp, Color(0xFFFFD700), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text("N", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun LevelIcon() {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF9D4EDD), Color(0xFF3A0CA3))
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(30.dp))
    }
}

@Composable
private fun CoinIcon() {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFD700), Color(0xFFB8860B))
                ),
                shape = CircleShape
            )
            .border(2.dp, Color(0xFFFFAA00), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text("N", color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Black)
    }
}