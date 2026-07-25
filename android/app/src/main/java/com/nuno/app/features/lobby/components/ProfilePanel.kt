package com.nuno.app.features.lobby.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
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
fun ProfilePanel(
    username: String,
    level: Int,
    xpProgress: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "avatarGlow")
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(2000), repeatMode = RepeatMode.Reverse),
        label = "glow"
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .shadow(12.dp, CircleShape, spotColor = Color(0xFF4CC9F0).copy(alpha = glow))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF4CC9F0), Color(0xFF3A0CA3))
                    ),
                    shape = CircleShape
                )
                .border(3.dp, Color(0xFF00E5FF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = username.firstOrNull()?.uppercase() ?: "?",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
        }

        Column {
            Text(text = username, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = "Lv. $level", color = Color(0xFF8B92B8), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { xpProgress },
                modifier = Modifier.width(120.dp).height(4.dp),
                color = Color(0xFFFFD700),
                trackColor = Color(0xFF1A1D33)
            )
        }
    }
}