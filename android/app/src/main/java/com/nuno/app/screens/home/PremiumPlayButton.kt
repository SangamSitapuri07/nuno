package com.nuno.app.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nuno.app.R
import com.nuno.app.core.designsystem.GameColors

@Composable
fun PremiumPlayButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = tween(120, easing = FastOutSlowInEasing),
        label = "pressScale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "playPremiumAnim")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            tween(1800, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val floatBounce by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -7f,
        animationSpec = infiniteRepeatable(
            tween(1600, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "bounce"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            tween(1500, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = Modifier
            .scale(pressScale * pulseScale)
            .offset(y = floatBounce.dp)
            .size(width = 168.dp, height = 124.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Multi-layer glow
        Box(
            modifier = Modifier
                .size(width = 140.dp, height = 90.dp)
                .background(
                    Brush.radialGradient(
                        listOf(
                            GameColors.Gold.copy(alpha = glowAlpha * 0.5f),
                            GameColors.Orange.copy(alpha = glowAlpha * 0.25f),
                            Color.Transparent
                        )
                    ),
                    RoundedCornerShape(24.dp)
                )
                .blur(16.dp)
        )

        Box(
            modifier = Modifier
                .size(width = 120.dp, height = 70.dp)
                .background(
                    Brush.radialGradient(
                        listOf(
                            Color.White.copy(alpha = glowAlpha * 0.35f),
                            Color.Transparent
                        )
                    ),
                    RoundedCornerShape(20.dp)
                )
                .blur(8.dp)
        )

        // Button image with shadow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(22.dp),
                    spotColor = GameColors.Gold.copy(alpha = 0.6f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_play_tile_3d),
                contentDescription = "PLAY",
                modifier = Modifier.fillMaxSize()
            )
        }

        // Sparkle particles
        Box(
            modifier = Modifier
                .size(5.dp)
                .offset(x = 50.dp, y = (-30).dp)
                .background(Color.White.copy(alpha = glowAlpha), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(3.dp)
                .offset(x = (-48).dp, y = 28.dp)
                .background(GameColors.Gold.copy(alpha = glowAlpha), CircleShape)
        )
    }
}
