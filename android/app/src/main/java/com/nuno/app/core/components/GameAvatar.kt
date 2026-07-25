package com.nuno.app.core.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*

@Composable
fun GameAvatar(
    username: String,
    size: Dp = 48.dp,
    borderColor: Color = AccentCyan,
    isOnline: Boolean = false,
    withGlow: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "avatarGlow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = "glow"
    )

    Box {
        Box(
            modifier = Modifier
                .size(size)
                .then(if (withGlow) Modifier.shadow(size / 4, CircleShape, spotColor = borderColor.copy(alpha = glowAlpha)) else Modifier)
                .background(
                    brush = Brush.radialGradient(colors = listOf(PrimaryBlue, PrimaryPurple)),
                    shape = CircleShape
                )
                .border(2.dp, borderColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = username.firstOrNull()?.uppercase() ?: "?",
                color = Color.White,
                fontSize = (size.value * 0.4f).sp,
                fontWeight = FontWeight.Black
            )
        }

        if (isOnline) {
            Box(
                modifier = Modifier
                    .size(size / 4)
                    .background(SuccessGreen, CircleShape)
                    .border(2.dp, BackgroundDark, CircleShape)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}