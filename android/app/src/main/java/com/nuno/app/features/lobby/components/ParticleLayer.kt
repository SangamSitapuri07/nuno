package com.nuno.app.features.lobby.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

/**
 * Floating particle system for ambient sparkles and dust.
 * Uses Canvas API for performance.
 */
@Composable
fun ParticleLayer(
    modifier: Modifier = Modifier,
    particleCount: Int = 50
) {
    val infiniteTransition = rememberInfiniteTransition(label = "particles")

    val particles = remember {
        List(particleCount) {
            Particle(
                startX = Random.nextFloat(),
                startY = Random.nextFloat(),
                speed = Random.nextFloat() * 0.3f + 0.1f,
                size = Random.nextFloat() * 3f + 1f,
                color = when (Random.nextInt(4)) {
                    0 -> Color(0xFFFFD700) // Gold
                    1 -> Color(0xFF4CC9F0) // Cyan
                    2 -> Color(0xFFB980F5) // Purple
                    else -> Color.White
                },
                phaseOffset = Random.nextFloat() * 2f * Math.PI.toFloat()
            )
        }
    }

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10_000, easing = LinearEasing)
        ),
        label = "progress"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val progressForParticle = (progress + particle.phaseOffset / (2f * Math.PI.toFloat())) % 1f

            val x = particle.startX * size.width
            val y = (particle.startY + progressForParticle * particle.speed) % 1f * size.height

            // Fade in and out
            val alpha = when {
                progressForParticle < 0.1f -> progressForParticle * 10f
                progressForParticle > 0.9f -> (1f - progressForParticle) * 10f
                else -> 1f
            } * 0.6f

            drawCircle(
                color = particle.color.copy(alpha = alpha),
                radius = particle.size,
                center = Offset(x, y)
            )

            // Glow effect
            drawCircle(
                color = particle.color.copy(alpha = alpha * 0.3f),
                radius = particle.size * 3f,
                center = Offset(x, y)
            )
        }
    }
}

private data class Particle(
    val startX: Float,
    val startY: Float,
    val speed: Float,
    val size: Float,
    val color: Color,
    val phaseOffset: Float
)