package com.nuno.app.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nuno.app.R

@Composable
fun PremiumGalaxyBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "bgAnim")

    val scalePulse by infiniteTransition.animateFloat(
        initialValue = 2.25f,
        targetValue = 2.38f,
        animationSpec = infiniteRepeatable(
            tween(12000, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "scale"
    )

    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(180_000, easing = LinearEasing)
        ),
        label = "rotation"
    )

    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            tween(3500, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "shimmer"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Base dark gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF0A0C24),
                            Color(0xFF06071A),
                            Color(0xFF030410)
                        )
                    )
                )
        )

        // Rotating galaxy image
        Image(
            painter = painterResource(id = R.drawable.bg_galaxy_spiral),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .scale(scalePulse)
                .graphicsLayer {
                    rotationZ = rotationAngle
                }
        )

        // Premium overlay gradients for depth
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFF06071A).copy(alpha = 0.4f),
                            Color(0xFF030410).copy(alpha = 0.85f)
                        ),
                        radius = 1200f
                    )
                )
        )

        // Top vignette
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF7B5CFF).copy(alpha = 0.08f),
                            Color.Transparent,
                            Color.Transparent,
                            Color(0xFF030410).copy(alpha = 0.3f)
                        )
                    )
                )
        )

        // Shimmer overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFF4CC9F0).copy(alpha = shimmerAlpha * 0.06f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

@Composable
fun PremiumGameTableBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Deep table background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF1A1F4A),
                            Color(0xFF0F1230),
                            Color(0xFF06071A)
                        ),
                        radius = 1800f
                    )
                )
        )

        // Subtle galaxy
        Image(
            painter = painterResource(id = R.drawable.bg_galaxy_spiral),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .scale(2f)
                .blur(1.dp)
        )

        // Dark overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF030410).copy(alpha = 0.65f))
        )
    }
}
