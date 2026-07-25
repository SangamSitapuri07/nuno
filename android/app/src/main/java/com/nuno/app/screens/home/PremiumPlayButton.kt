package com.nuno.app.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PremiumPlayButton(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "play")

    val glow by infiniteTransition.animateFloat(
        0.5f, 1f,
        infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = "glow"
    )

    val scale by infiniteTransition.animateFloat(
        1f, 1.02f,
        infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "scale"
    )

    val shineOffset by infiniteTransition.animateFloat(
        -1f, 2f,
        infiniteRepeatable(tween(3000, easing = LinearEasing)),
        label = "shine"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .scale(scale)
                .width(240.dp)
                .height(60.dp)
                .shadow(24.dp, RoundedCornerShape(30.dp), spotColor = Color(0xFFFFB800).copy(alpha = glow))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFFFFB800), Color(0xFFFF9500), Color(0xFFFFB800), Color(0xFFFFD700))
                    ),
                    RoundedCornerShape(30.dp)
                )
                .border(
                    2.dp,
                    Brush.horizontalGradient(listOf(Color(0xFFFFE57F).copy(alpha = 0.6f), Color(0xFFFFB800).copy(alpha = 0.3f), Color(0xFFFFE57F).copy(alpha = 0.6f))),
                    RoundedCornerShape(30.dp)
                )
                .clip(RoundedCornerShape(30.dp))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            // Shine effect moving across button
            Canvas(modifier = Modifier.fillMaxSize()) {
                val shineX = shineOffset * size.width
                drawRect(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, Color.White.copy(alpha = 0.2f), Color.Transparent),
                        startX = shineX - 80f, endX = shineX + 80f
                    )
                )
            }

            // Top glossy highlight
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.25f), Color.Transparent)),
                        RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    )
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PlayArrow, null, tint = Color(0xFF3E2723), modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("PLAY", color = Color(0xFF3E2723), fontSize = 24.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text("Quick Match", color = Color(0xFFAAAAAA), fontSize = 11.sp)
    }
}