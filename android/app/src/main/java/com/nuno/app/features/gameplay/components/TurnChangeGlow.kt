package com.nuno.app.features.gameplay.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import kotlinx.coroutines.launch

@Composable
fun TurnChangeGlow(isMyTurn: Boolean) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        launch { scale.animateTo(1f, tween(400, easing = FastOutSlowInEasing)) }
        kotlinx.coroutines.delay(400)
        launch { alpha.animateTo(0f, tween(400)) }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(400.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    this.alpha = alpha.value
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            if (isMyTurn) SuccessGreen.copy(alpha = 0.5f) else AccentGold.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Text(
            text = if (isMyTurn) "YOUR TURN!" else "OPPONENT'S TURN",
            color = if (isMyTurn) SuccessGreen else AccentGold,
            fontSize = 40.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 4.sp,
            modifier = Modifier.graphicsLayer {
                this.alpha = alpha.value
                scaleX = 0.5f + scale.value * 0.5f
                scaleY = 0.5f + scale.value * 0.5f
            }
        )
    }
}

