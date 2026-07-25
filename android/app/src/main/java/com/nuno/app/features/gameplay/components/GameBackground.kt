package com.nuno.app.features.gameplay.components

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

@Composable
fun GameBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "gameBg")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(30_000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val nebulaShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(60_000, easing = LinearEasing)
        ),
        label = "nebula"
    )

    val stars = remember {
        List(200) {
            Star(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 2.5f + 0.5f,
                speed = Random.nextFloat() * 0.8f + 0.3f,
                phase = Random.nextFloat() * 2f * Math.PI.toFloat(),
                color = when (Random.nextInt(4)) {
                    0 -> Color(0xFFFFFFFF)
                    1 -> Color(0xFF4CC9F0)
                    2 -> Color(0xFFB980F5)
                    else -> Color(0xFFFFD700)
                }
            )
        }
    }

    val orbitalDust = remember {
        List(80) {
            OrbitalParticle(
                orbitRadius = Random.nextFloat() * 0.4f + 0.2f,
                angle = Random.nextFloat() * 360f,
                speed = Random.nextFloat() * 0.3f + 0.1f,
                size = Random.nextFloat() * 1.5f + 0.5f,
                color = if (Random.nextBoolean()) Color(0xFF4CC9F0) else Color(0xFFB980F5)
            )
        }
    }

    val starPulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing)
        ),
        label = "starPulse"
    )

    val orbitalRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(120_000, easing = LinearEasing)
        ),
        label = "orbital"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1E0B4B), // Deep purple center
                        Color(0xFF120638),
                        Color(0xFF080319),
                        Color(0xFF020108)  // Almost black edges
                    )
                )
            )
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2f
            val centerY = size.height / 2f

            // ═══ NEBULA CLOUDS ═══

            // Purple nebula top-left
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF7B2CBF).copy(alpha = 0.4f),
                        Color(0xFF5A189A).copy(alpha = 0.2f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.2f, size.height * 0.3f),
                    radius = size.width * 0.4f
                ),
                radius = size.width * 0.4f,
                center = Offset(size.width * 0.2f, size.height * 0.3f)
            )

            // Blue nebula bottom-right
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF3A0CA3).copy(alpha = 0.4f),
                        Color(0xFF4361EE).copy(alpha = 0.2f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.8f, size.height * 0.7f),
                    radius = size.width * 0.35f
                ),
                radius = size.width * 0.35f,
                center = Offset(size.width * 0.8f, size.height * 0.7f)
            )

            // Pink nebula bottom-left
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFC77DFF).copy(alpha = 0.25f),
                        Color(0xFF9D4EDD).copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.15f, size.height * 0.8f),
                    radius = size.width * 0.3f
                ),
                radius = size.width * 0.3f,
                center = Offset(size.width * 0.15f, size.height * 0.8f)
            )

            // Cyan glow top-right
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF4CC9F0).copy(alpha = 0.25f),
                        Color(0xFF00E5FF).copy(alpha = 0.1f),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.85f, size.height * 0.2f),
                    radius = size.width * 0.3f
                ),
                radius = size.width * 0.3f,
                center = Offset(size.width * 0.85f, size.height * 0.2f)
            )

            // Center cosmic spotlight
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF9D4EDD).copy(alpha = 0.3f),
                        Color(0xFF7B2CBF).copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    center = Offset(centerX, centerY),
                    radius = size.width * 0.35f
                ),
                radius = size.width * 0.35f,
                center = Offset(centerX, centerY)
            )

            // ═══ ORBITAL RINGS (faint circles around center) ═══

            for (i in 1..3) {
                val ringRadius = size.width * (0.15f + i * 0.08f)
                drawCircle(
                    color = Color(0xFF7B2CBF).copy(alpha = 0.08f - i * 0.02f),
                    radius = ringRadius,
                    center = Offset(centerX, centerY),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5f)
                )
            }

            // ═══ ORBITAL PARTICLES ═══

            orbitalDust.forEach { particle ->
                val angleRad = Math.toRadians((particle.angle + orbitalRotation * particle.speed).toDouble())
                val x = centerX + (cos(angleRad) * size.width * particle.orbitRadius).toFloat()
                val y = centerY + (sin(angleRad) * size.width * particle.orbitRadius * 0.6f).toFloat()

                drawCircle(
                    color = particle.color.copy(alpha = 0.6f),
                    radius = particle.size,
                    center = Offset(x, y)
                )
                // Soft glow around each particle
                drawCircle(
                    color = particle.color.copy(alpha = 0.2f),
                    radius = particle.size * 2.5f,
                    center = Offset(x, y)
                )
            }

            // ═══ TWINKLING STARS ═══

            stars.forEach { star ->
                val twinkle = (sin(starPulse * star.speed + star.phase) + 1f) / 2f
                val alpha = 0.3f + twinkle * 0.7f

                drawCircle(
                    color = star.color.copy(alpha = alpha),
                    radius = star.size,
                    center = Offset(star.x * size.width, star.y * size.height)
                )

                // Extra bright stars have subtle glow
                if (star.size > 1.8f) {
                    drawCircle(
                        color = star.color.copy(alpha = alpha * 0.3f),
                        radius = star.size * 3f,
                        center = Offset(star.x * size.width, star.y * size.height)
                    )
                }
            }
        }
    }
}

private data class Star(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float,
    val phase: Float,
    val color: Color
)

private data class OrbitalParticle(
    val orbitRadius: Float,
    val angle: Float,
    val speed: Float,
    val size: Float,
    val color: Color
)