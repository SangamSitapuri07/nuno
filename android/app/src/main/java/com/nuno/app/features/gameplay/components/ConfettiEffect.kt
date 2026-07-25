package com.nuno.app.features.gameplay.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.random.Random

@Composable
fun ConfettiEffect() {
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")

    val particles = remember {
        List(80) {
            ConfettiParticle(
                startX = Random.nextFloat(),
                startY = -0.1f - Random.nextFloat() * 0.2f,
                color = listOf(
                    Color(0xFFFFD700),
                    Color(0xFF4CC9F0),
                    Color(0xFFFF3D8B),
                    Color(0xFF00E676),
                    Color(0xFF9D4EDD),
                    Color(0xFFFFFFFF)
                ).random(),
                size = Random.nextFloat() * 8f + 4f,
                fallSpeed = Random.nextFloat() * 0.5f + 0.3f,
                driftSpeed = (Random.nextFloat() - 0.5f) * 0.3f,
                rotationSpeed = Random.nextFloat() * 720f
            )
        }
    }

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing)
        ),
        label = "progress"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val cycleProgress = ((progress + particle.startY * 0.5f) % 1f)

            val x = (particle.startX + particle.driftSpeed * cycleProgress) * size.width
            val y = (cycleProgress * particle.fallSpeed * 1.5f) * size.height

            val rotation = particle.rotationSpeed * cycleProgress
            val alpha = when {
                cycleProgress < 0.1f -> cycleProgress * 10f
                cycleProgress > 0.9f -> (1f - cycleProgress) * 10f
                else -> 1f
            }

            rotate(degrees = rotation, pivot = Offset(x, y)) {
                drawRect(
                    color = particle.color.copy(alpha = alpha),
                    topLeft = Offset(x - particle.size / 2, y - particle.size / 2),
                    size = Size(particle.size, particle.size * 1.5f)
                )
            }
        }
    }
}

private data class ConfettiParticle(
    val startX: Float,
    val startY: Float,
    val color: Color,
    val size: Float,
    val fallSpeed: Float,
    val driftSpeed: Float,
    val rotationSpeed: Float
)