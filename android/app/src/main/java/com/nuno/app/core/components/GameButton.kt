package com.nuno.app.core.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*

@Composable
fun PremiumButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    gradient: List<Color> = listOf(PrimaryBlue, PrimaryPurple),
    borderGradient: List<Color> = listOf(AccentGold, Color(0xFFB8860B), AccentGold),
    height: androidx.compose.ui.unit.Dp = 56.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "btn")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .scale(if (enabled) pulse else 1f)
            .shadow(12.dp, RoundedCornerShape(GameTheme.Radius.lg), spotColor = gradient[0])
            .height(height)
            .background(
                brush = Brush.horizontalGradient(if (enabled) gradient else listOf(NeutralGray600, NeutralGray700)),
                shape = RoundedCornerShape(GameTheme.Radius.lg)
            )
            .border(2.dp, Brush.horizontalGradient(borderGradient), RoundedCornerShape(GameTheme.Radius.lg))
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
    }
}