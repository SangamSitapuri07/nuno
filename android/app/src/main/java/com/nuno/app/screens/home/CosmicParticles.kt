package com.nuno.app.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun PremiumCosmicParticles() {
    val infiniteTransition = rememberInfiniteTransition(label = "particlesPremium")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(22_000, easing = LinearEasing)
        ),
        label = "p"
    )

    val particles = remember {
        List(55) {
            CosmicParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                speed = Random.nextFloat() * 0.12f + 0.02f,
                size = Random.nextFloat() * 2.8f + 0.6f,
                drift = (Random.nextFloat() - 0.5f) * 0.025f,
                twinkleSpeed = Random.nextFloat() * 0.04f + 0.01f,
                twinkleOffset = Random.nextFloat() * 6.28f,
                color = listOf(
                    Color(0xFFFFD23F),
                    Color(0xFF00D9FF),
                    Color(0xFF7B5CFF),
                    Color(0xFFFF3D8B),
                    Color(0xFF00E676),
                    Color(0xFFFFFFFF),
                    Color(0xFF66EBFF)
                ).random()
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { p ->
            val y = ((p.y + progress * p.speed) % 1f) * size.height
            val x = (p.x + sin(progress * 6.28f + p.x * 12f).toFloat() * p.drift) * size.width
            val lifecycle = ((p.y + progress * p.speed) % 1f)
            val alphaBase = when {
                lifecycle < 0.08f -> lifecycle * 12.5f
                lifecycle > 0.88f -> (1f - lifecycle) * 8.33f
                else -> 1f
            }
            val twinkle = (sin(progress * 8f * p.twinkleSpeed + p.twinkleOffset) * 0.3f + 0.7f).coerceIn(0.2f, 1f)
            val alpha = (alphaBase * twinkle * 0.9f).coerceIn(0f, 1f)

            // Outer halo
            drawCircle(p.color.copy(alpha = alpha * 0.04f), p.size * 7f, Offset(x, y))
            // Glow
            drawCircle(p.color.copy(alpha = alpha * 0.18f), p.size * 3.2f, Offset(x, y))
            // Core
            drawCircle(p.color.copy(alpha = alpha), p.size, Offset(x, y))
            // Bright center
            drawCircle(Color.White.copy(alpha = alpha * 0.9f), p.size * 0.35f, Offset(x, y))
        }
    }
}

@Composable
fun SubtleParticleOverlay() {
    val infiniteTransition = rememberInfiniteTransition(label = "subtle")
    val progress by infiniteTransition.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(30_000, easing = LinearEasing)),
        label = "p"
    )

    val particles = remember {
        List(25) {
            CosmicParticle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                speed = Random.nextFloat() * 0.06f + 0.01f,
                size = Random.nextFloat() * 1.5f + 0.4f,
                drift = (Random.nextFloat() - 0.5f) * 0.01f,
                twinkleSpeed = 0.02f,
                twinkleOffset = 0f,
                color = Color.White
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { p ->
            val y = ((p.y + progress * p.speed) % 1f) * size.height
            val x = (p.x + sin(progress * 3f + p.x * 5).toFloat() * p.drift) * size.width
            drawCircle(p.color.copy(alpha = 0.15f), p.size, Offset(x, y))
        }
    }
}

private data class CosmicParticle(
    val x: Float,
    val y: Float,
    val speed: Float,
    val size: Float,
    val drift: Float,
    val twinkleSpeed: Float = 0.02f,
    val twinkleOffset: Float = 0f,
    val color: Color
)
