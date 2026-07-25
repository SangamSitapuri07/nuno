package com.nuno.app.screens.home

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
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun PremiumGalaxyBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "galaxy")

    val nebulaPulse by infiniteTransition.animateFloat(
        0.12f, 0.3f,
        infiniteRepeatable(tween(10_000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "nebula"
    )
    val starPulse by infiniteTransition.animateFloat(
        0f, 6.28f,
        infiniteRepeatable(tween(7000, easing = LinearEasing)),
        label = "star"
    )
    val planet1 by infiniteTransition.animateFloat(
        0f, 360f, infiniteRepeatable(tween(180_000, easing = LinearEasing)), label = "p1"
    )
    val planet2 by infiniteTransition.animateFloat(
        0f, 360f, infiniteRepeatable(tween(120_000, easing = LinearEasing)), label = "p2"
    )
    val planet3 by infiniteTransition.animateFloat(
        0f, 360f, infiniteRepeatable(tween(240_000, easing = LinearEasing)), label = "p3"
    )
    val lensFlare by infiniteTransition.animateFloat(
        0.05f, 0.2f,
        infiniteRepeatable(tween(5000), RepeatMode.Reverse),
        label = "flare"
    )

    val stars = remember {
        List(200) {
            StarData(
                x = Random.nextFloat(), y = Random.nextFloat(),
                size = Random.nextFloat() * 2.5f + 0.5f,
                phase = Random.nextFloat() * 6.28f,
                speed = Random.nextFloat() * 0.8f + 0.3f,
                color = listOf(Color.White, Color(0xFFCCDDFF), Color(0xFFAABBEE), Color(0xFFFFDDAA)).random()
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF030510),
                        Color(0xFF0A0B20),
                        Color(0xFF0D1027),
                        Color(0xFF050212)
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // ═══ NEBULA CLOUDS ═══
            // Large purple nebula
            drawCircle(
                Brush.radialGradient(
                    listOf(Color(0xFF7B2CBF).copy(alpha = nebulaPulse), Color(0xFF5A189A).copy(alpha = nebulaPulse * 0.5f), Color.Transparent),
                    center = Offset(w * 0.15f, h * 0.25f), radius = w * 0.45f
                ),
                radius = w * 0.45f, center = Offset(w * 0.15f, h * 0.25f)
            )

            // Deep blue nebula
            drawCircle(
                Brush.radialGradient(
                    listOf(Color(0xFF3A0CA3).copy(alpha = nebulaPulse * 0.8f), Color(0xFF240066).copy(alpha = nebulaPulse * 0.4f), Color.Transparent),
                    center = Offset(w * 0.8f, h * 0.6f), radius = w * 0.4f
                ),
                radius = w * 0.4f, center = Offset(w * 0.8f, h * 0.6f)
            )

            // Cyan nebula
            drawCircle(
                Brush.radialGradient(
                    listOf(Color(0xFF4CC9F0).copy(alpha = nebulaPulse * 0.3f), Color.Transparent),
                    center = Offset(w * 0.5f, h * 0.4f), radius = w * 0.35f
                ),
                radius = w * 0.35f, center = Offset(w * 0.5f, h * 0.4f)
            )

            // Pink nebula (subtle)
            drawCircle(
                Brush.radialGradient(
                    listOf(Color(0xFFFF6B9D).copy(alpha = nebulaPulse * 0.15f), Color.Transparent),
                    center = Offset(w * 0.7f, h * 0.2f), radius = w * 0.25f
                ),
                radius = w * 0.25f, center = Offset(w * 0.7f, h * 0.2f)
            )

            // ═══ ORBITAL RINGS ═══
            for (i in 1..4) {
                drawCircle(
                    Color(0xFF6C2BD9).copy(alpha = 0.03f + i * 0.01f),
                    radius = w * (0.15f + i * 0.08f),
                    center = Offset(w * 0.5f, h * 0.5f),
                    style = Stroke(width = 0.8f)
                )
            }

            // ═══ PLANETS ═══
            // Planet 1 - Large purple with rings
            val p1Rad = Math.toRadians(planet1.toDouble())
            val p1x = w * 0.12f + (cos(p1Rad) * w * 0.06f).toFloat()
            val p1y = h * 0.18f + (sin(p1Rad) * h * 0.04f).toFloat()
            drawCircle(
                Brush.radialGradient(
                    listOf(Color(0xFFB980F5).copy(alpha = 0.7f), Color(0xFF5A189A).copy(alpha = 0.4f), Color.Transparent),
                    center = Offset(p1x, p1y), radius = 35f
                ),
                radius = 35f, center = Offset(p1x, p1y)
            )
            // Planet highlight
            drawCircle(Color(0xFFD4B0FF).copy(alpha = 0.3f), radius = 14f, center = Offset(p1x - 10f, p1y - 8f))
            // Planet ring
            drawCircle(Color(0xFFB980F5).copy(alpha = 0.15f), radius = 50f, center = Offset(p1x, p1y), style = Stroke(width = 1.5f))

            // Planet 2 - Small blue
            val p2Rad = Math.toRadians(planet2.toDouble())
            val p2x = w * 0.88f + (cos(p2Rad) * w * 0.04f).toFloat()
            val p2y = h * 0.12f + (sin(p2Rad) * h * 0.03f).toFloat()
            drawCircle(
                Brush.radialGradient(
                    listOf(Color(0xFF4CC9F0).copy(alpha = 0.6f), Color(0xFF3A0CA3).copy(alpha = 0.3f), Color.Transparent),
                    center = Offset(p2x, p2y), radius = 22f
                ),
                radius = 22f, center = Offset(p2x, p2y)
            )
            drawCircle(Color(0xFF7DE8FF).copy(alpha = 0.3f), radius = 8f, center = Offset(p2x - 6f, p2y - 5f))

            // Planet 3 - Distant gold
            val p3Rad = Math.toRadians(planet3.toDouble())
            val p3x = w * 0.55f + (cos(p3Rad) * w * 0.12f).toFloat()
            val p3y = h * 0.88f + (sin(p3Rad) * h * 0.02f).toFloat()
            drawCircle(
                Brush.radialGradient(
                    listOf(Color(0xFFFFD700).copy(alpha = 0.4f), Color(0xFFFF8C00).copy(alpha = 0.2f), Color.Transparent),
                    center = Offset(p3x, p3y), radius = 16f
                ),
                radius = 16f, center = Offset(p3x, p3y)
            )

            // ═══ LENS FLARE ═══
            drawCircle(
                Brush.radialGradient(
                    listOf(Color(0xFFFFFFFF).copy(alpha = lensFlare), Color(0xFF4CC9F0).copy(alpha = lensFlare * 0.3f), Color.Transparent),
                    center = Offset(w * 0.75f, h * 0.15f), radius = 60f
                ),
                radius = 60f, center = Offset(w * 0.75f, h * 0.15f)
            )

            // ═══ STARS ═══
            stars.forEach { star ->
                val twinkle = (sin(starPulse * star.speed + star.phase) + 1f) / 2f
                val alpha = 0.2f + twinkle * 0.7f
                val sx = star.x * w
                val sy = star.y * h

                drawCircle(star.color.copy(alpha = alpha), star.size, Offset(sx, sy))

                // Glow halo for bright stars
                if (star.size > 1.5f) {
                    drawCircle(star.color.copy(alpha = alpha * 0.12f), star.size * 4f, Offset(sx, sy))
                }
                // Cross sparkle for very bright stars
                if (star.size > 2f) {
                    drawLine(star.color.copy(alpha = alpha * 0.3f), Offset(sx - star.size * 3, sy), Offset(sx + star.size * 3, sy), strokeWidth = 0.5f)
                    drawLine(star.color.copy(alpha = alpha * 0.3f), Offset(sx, sy - star.size * 3), Offset(sx, sy + star.size * 3), strokeWidth = 0.5f)
                }
            }
        }
    }
}

private data class StarData(
    val x: Float, val y: Float, val size: Float,
    val phase: Float, val speed: Float, val color: Color
)