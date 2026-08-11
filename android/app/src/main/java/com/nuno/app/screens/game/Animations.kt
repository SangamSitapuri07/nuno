package com.nuno.app.screens.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun TurnGlowEffect(isMyTurn: Boolean) {
    if (!isMyTurn) return

    val infiniteTransition = rememberInfiniteTransition(label = "turnGlow")
    val alpha by infiniteTransition.animateFloat(
        0.1f, 0.35f,
        infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4CAF50).copy(alpha = alpha))
    )
}