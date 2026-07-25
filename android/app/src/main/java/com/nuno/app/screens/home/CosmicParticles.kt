package com.nuno.app.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun PremiumCosmicParticles() {
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val progress by infiniteTransition.animateFloat(
        0f, 1f, infiniteRepeatable(tween(18_000, easing = LinearEasing)), label = "p"
    )

    val particles = remember {
        List(40) {
            CosmicParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                speed = Random.nextFloat() * 0.15f + 0.03f,
                size = Random.nextFloat() * 2.5f + 0.8f,
                drift = (Random.nextFloat() - 0.5f) * 0.02f,
                color = listOf(
                    Color(0xFFFFD700), Color(0xFF4CC9F0), Color(0xFFB980F5),
                    Color(0xFFFF6B9D), Color(0xFF00E5FF), Color(0xFFFFFFFF)
                ).random()
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { p ->
            val y = ((p.y + progress * p.speed) % 1f) * size.height
            val x = (p.x + sin(progress * 6.28 + p.x * 10).toFloat() * p.drift) * size.width
            val lifecycle = ((p.y + progress * p.speed) % 1f)
            val alpha = when {
                lifecycle < 0.1f -> lifecycle * 10f
                lifecycle > 0.85f -> (1f - lifecycle) * 6.67f
                else -> 0.5f
            }

            // Core particle
            drawCircle(p.color.copy(alpha = alpha), p.size, Offset(x, y))
            // Soft glow
            drawCircle(p.color.copy(alpha = alpha * 0.15f), p.size * 3.5f, Offset(x, y))
            // Outer halo
            drawCircle(p.color.copy(alpha = alpha * 0.05f), p.size * 6f, Offset(x, y))
        }
    }
}

private data class CosmicParticle(
    val x: Float, val y: Float, val speed: Float,
    val size: Float, val drift: Float, val color: Color
)