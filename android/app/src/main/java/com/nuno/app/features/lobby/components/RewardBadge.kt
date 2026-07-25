package com.nuno.app.features.lobby.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RewardBadge(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "reward")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(20_000, easing = LinearEasing)),
        label = "rotation"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(animation = tween(1500), repeatMode = RepeatMode.Reverse),
        label = "pulse"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .rotate(rotation)
                .background(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            Color(0xFFFFD700).copy(alpha = 0.3f),
                            Color.Transparent,
                            Color(0xFFFFD700).copy(alpha = 0.3f),
                            Color.Transparent,
                            Color(0xFFFFD700).copy(alpha = 0.3f)
                        )
                    ),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(90.dp)
                .shadow(12.dp, CircleShape, spotColor = Color(0xFFFFD700))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF8B4513), Color(0xFF3E2723))
                    ),
                    shape = CircleShape
                )
                .border(3.dp, Color(0xFFFFD700), CircleShape)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "🎁", fontSize = 32.sp, modifier = Modifier.rotate(-rotation))
                Text(
                    text = "+100",
                    color = Color(0xFFFFD700),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}