package com.nuno.app.features.gameplay.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DealingAnimation(playerCount: Int) {
    var cardsDealt by remember { mutableStateOf(0) }
    val totalCardsToShow = playerCount * 7

    LaunchedEffect(Unit) {
        for (i in 0 until totalCardsToShow) {
            delay(80)
            cardsDealt = i + 1
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "DEALING CARDS",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 3.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Box(
                modifier = Modifier.size(400.dp),
                contentAlignment = Alignment.Center
            ) {
                // Central deck
                Box(
                    modifier = Modifier
                        .size(width = 70.dp, height = 100.dp)
                        .background(
                            brush = Brush.linearGradient(colors = listOf(PrimaryBlue, PrimaryPurple)),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(2.dp, AccentGold, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("NUNO", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Black)
                }

                // Flying cards to each player position
                for (i in 0 until cardsDealt.coerceAtMost(totalCardsToShow)) {
                    val playerIndex = i % playerCount
                    val cardIndex = i / playerCount

                    val angle = (playerIndex * (360f / playerCount)) - 90f
                    val angleRad = Math.toRadians(angle.toDouble())
                    val targetRadius = 150f
                    val targetX = (cos(angleRad) * targetRadius).toFloat()
                    val targetY = (sin(angleRad) * targetRadius).toFloat()

                    FlyingCard(
                        targetX = targetX,
                        targetY = targetY,
                        cardIndex = cardIndex,
                        playerIndex = playerIndex
                    )
                }
            }

            Text(
                text = "$cardsDealt / $totalCardsToShow",
                color = AccentCyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}

@Composable
private fun FlyingCard(targetX: Float, targetY: Float, cardIndex: Int, playerIndex: Int) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        )
    }

    val currentX = targetX * progress.value
    val currentY = targetY * progress.value
    val stackOffset = cardIndex * 3f

    Box(
        modifier = Modifier
            .size(width = 40.dp, height = 60.dp)
            .graphicsLayer {
                translationX = (currentX + stackOffset) * density
                translationY = (currentY + stackOffset) * density
                alpha = progress.value
                rotationZ = 360f * progress.value
            }
            .background(
                brush = Brush.linearGradient(colors = listOf(PrimaryBlue, PrimaryPurple)),
                shape = RoundedCornerShape(6.dp)
            )
            .border(1.dp, AccentGold, RoundedCornerShape(6.dp))
    )
}