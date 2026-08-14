package com.nuno.app.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nuno.app.R
import com.nuno.app.core.designsystem.GameColors

@Composable
fun SpinningCardWheel(
    modifier: Modifier = Modifier,
    wheelSize: Dp = 220.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pedestalPremium")

    val scalePulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            tween(2200, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "scale"
    )

    val floatBounce by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -8f,
        animationSpec = infiniteRepeatable(
            tween(1800, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "bounce"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = modifier
            .scale(scalePulse)
            .offset(y = floatBounce.dp),
        contentAlignment = Alignment.Center
    ) {
        // Glow behind pedestal
        Box(
            modifier = Modifier
                .size(width = 200.dp, height = 80.dp)
                .offset(y = 60.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            GameColors.Purple.copy(alpha = glowAlpha * 0.4f),
                            GameColors.Blue.copy(alpha = glowAlpha * 0.15f),
                            Color.Transparent
                        )
                    ),
                    CircleShape
                )
                .blur(18.dp)
        )

        // Soft shadow floor
        Box(
            modifier = Modifier
                .size(width = 180.dp, height = 32.dp)
                .offset(y = 84.dp)
                .background(
                    Color.Black.copy(alpha = 0.45f),
                    CircleShape
                )
                .blur(12.dp)
        )

        Box(
            modifier = Modifier
                .size(width = 300.dp, height = 220.dp)
                .shadow(
                    elevation = 24.dp,
                    spotColor = GameColors.Purple.copy(alpha = 0.4f),
                    ambientColor = Color.Black.copy(alpha = 0.5f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_card_pedestal_3d),
                contentDescription = "NUNO Card Pedestal",
                modifier = Modifier.fillMaxSize()
            )
        }

        // Particle glow dots around
        Box(
            modifier = Modifier
                .size(8.dp)
                .offset(x = (-90).dp, y = (-20).dp)
                .background(GameColors.Cyan.copy(alpha = glowAlpha), CircleShape)
                .blur(1.dp)
        )
        Box(
            modifier = Modifier
                .size(6.dp)
                .offset(x = 95.dp, y = (-35).dp)
                .background(GameColors.Gold.copy(alpha = glowAlpha), CircleShape)
                .blur(1.dp)
        )
    }
}
