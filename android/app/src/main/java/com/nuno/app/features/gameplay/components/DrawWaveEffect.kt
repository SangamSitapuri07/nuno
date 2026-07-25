package com.nuno.app.features.gameplay.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun DrawWaveEffect(waveColor: Color = Color(0xFF4CC9F0)) {
    val infiniteTransition = rememberInfiniteTransition(label = "drawWave")

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing)
        ),
        label = "progress"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f

        // Three expanding rings
        for (i in 0..2) {
            val ringProgress = ((progress + i * 0.33f) % 1f)
            val radius = size.width * 0.6f * ringProgress
            val alpha = (1f - ringProgress) * 0.8f

            drawCircle(
                color = waveColor.copy(alpha = alpha),
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 6f)
            )

            drawCircle(
                color = waveColor.copy(alpha = alpha * 0.3f),
                radius = radius,
                center = Offset(centerX, centerY)
            )
        }
    }
}