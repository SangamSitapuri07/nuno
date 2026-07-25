package com.nuno.app.features.lobby.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LogoSection(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "logo")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Column(
        modifier = modifier.scale(scale),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            LogoLetter(letter = "N", background = Color(0xFFE53935))
            LogoLetter(letter = "U", background = Color(0xFF1E88E5))
            LogoLetter(letter = "N", background = Color(0xFF43A047))
            LogoLetter(letter = "O", background = Color(0xFFFDD835), textColor = Color.Black)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Play. Compete. Win.",
            color = Color.White.copy(alpha = glowAlpha),
            fontSize = 14.sp,
            letterSpacing = 6.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun LogoLetter(letter: String, background: Color, textColor: Color = Color.White) {
    Box(
        modifier = Modifier
            .size(60.dp, 80.dp)
            .background(background, RoundedCornerShape(6.dp))
            .border(2.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = letter, color = textColor, fontSize = 42.sp, fontWeight = FontWeight.Black)
    }
}