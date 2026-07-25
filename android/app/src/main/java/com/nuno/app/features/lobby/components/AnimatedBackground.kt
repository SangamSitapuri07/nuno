package com.nuno.app.features.lobby.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Animated galaxy background with slow zoom, drift, and animated stars.
 * Blue and purple color palette for space feel.
 */
@Composable
fun AnimatedBackground(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "background")

    // Slow zoom in/out
    val scale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(30_000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Slow drift
    val offsetX by infiniteTransition.animateFloat(
        initialValue = -30f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(40_000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetX"
    )

    val offsetY by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(35_000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )

    // Animated star field
    val stars = remember {
        List(150) {
            Star(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 2f + 0.5f,
                speed = Random.nextFloat() * 0.5f + 0.5f,
                phase = Random.nextFloat() * 2f * Math.PI.toFloat()
            )
        }
    }

    val starPulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing)
        ),
        label = "starPulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1A0B3D), // Deep purple
                        Color(0xFF0F0625), // Very dark purple
                        Color(0xFF050212)  // Almost black
                    )
                )
            )
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offsetX
                translationY = offsetY
            }
    ) {
        // Nebula layers
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Purple nebula
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF6C2BD9).copy(alpha = 0.3f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.3f, size.height * 0.4f),
                    radius = size.width * 0.5f
                ),
                radius = size.width * 0.5f,
                center = Offset(size.width * 0.3f, size.height * 0.4f)
            )

            // Blue nebula
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF3A0CA3).copy(alpha = 0.25f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.7f, size.height * 0.6f),
                    radius = size.width * 0.4f
                ),
                radius = size.width * 0.4f,
                center = Offset(size.width * 0.7f, size.height * 0.6f)
            )

            // Cyan glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF4CC9F0).copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.5f, size.height * 0.5f),
                    radius = size.width * 0.6f
                ),
                radius = size.width * 0.6f,
                center = Offset(size.width * 0.5f, size.height * 0.5f)
            )

            // Draw stars with twinkle
            stars.forEach { star ->
                val twinkle = (sin(starPulse * star.speed + star.phase) + 1f) / 2f
                val alpha = 0.3f + twinkle * 0.7f
                drawCircle(
                    color = Color.White.copy(alpha = alpha),
                    radius = star.size,
                    center = Offset(star.x * size.width, star.y * size.height)
                )
            }
        }
    }
}

private data class Star(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float,
    val phase: Float
)