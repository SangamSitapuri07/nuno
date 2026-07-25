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
import com.nuno.app.features.gameplay.GameCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope

// ─────────────────────────────────────────
// PLAY CARD ANIMATION
// Card flies from hand to discard pile
// ─────────────────────────────────────────

@Composable
fun PlayCardAnimation(
    card: GameCard,
    startX: Float,
    startY: Float,
    onComplete: () -> Unit
) {
    val progress = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        // Card flies with rotation and scaling
        launch { progress.animateTo(1f, tween(1200, easing = FastOutSlowInEasing)) }
        launch { rotation.animateTo(720f, tween(1200)) }
        launch {
            scale.animateTo(1.3f, tween(600))
            scale.animateTo(1f, tween(600))
        }
        delay(1200)
        onComplete()
    }

    val cardColor = getCardColor(card.color)
    val displayText = getCardText(card.value)

    Box(
        modifier = Modifier
            .size(width = 80.dp, height = 120.dp)
            .graphicsLayer {
                translationX = (startX * (1f - progress.value)) * density
                translationY = (startY * (1f - progress.value)) * density
                rotationZ = rotation.value
                scaleX = scale.value
                scaleY = scale.value
            }
            .background(cardColor, RoundedCornerShape(10.dp))
            .border(2.dp, Color.White, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText,
            color = if (card.color == "YELLOW") Color.Black else Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black
        )
    }
}

// ─────────────────────────────────────────
// DRAW CARD ANIMATION
// Card flies from deck to player hand
// ─────────────────────────────────────────

@Composable
fun DrawCardAnimation(
    endX: Float,
    endY: Float,
    onComplete: () -> Unit
) {
    val progress = remember { Animatable(0f) }
    val rotation = remember { Animatable(-180f) }

    LaunchedEffect(Unit) {
        launch { progress.animateTo(1f, tween(1000, easing = FastOutSlowInEasing)) }
        launch { rotation.animateTo(0f, tween(1000)) }
        delay(1000)
    }

    Box(
        modifier = Modifier
            .size(width = 70.dp, height = 105.dp)
            .graphicsLayer {
                translationX = (endX * progress.value) * density
                translationY = (endY * progress.value) * density
                rotationY = rotation.value
                alpha = progress.value
            }
            .background(
                brush = Brush.linearGradient(colors = listOf(PrimaryBlue, PrimaryPurple)),
                shape = RoundedCornerShape(8.dp)
            )
            .border(2.dp, AccentGold, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("NUNO", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Black)
    }
}

// ─────────────────────────────────────────
// SKIP EFFECT - Blue energy wave
// ─────────────────────────────────────────

@Composable
fun SkipEffect(onComplete: () -> Unit) {
    val wave = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        launch { wave.animateTo(1f, tween(800, easing = FastOutSlowInEasing)) }
        launch {
            delay(500)
            alpha.animateTo(0f, tween(300))
        }
        delay(800)
        onComplete()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Expanding wave
        Box(
            modifier = Modifier
                .size((300 * wave.value).dp)
                .graphicsLayer {
                    this.alpha = alpha.value
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AccentCyan.copy(alpha = 0.8f),
                            AccentCyan.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    ),
                    shape = androidx.compose.foundation.shape.CircleShape
                )
        )

        Text(
            text = "SKIPPED!",
            color = AccentCyan,
            fontSize = 40.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 4.sp,
            modifier = Modifier.graphicsLayer { this.alpha = alpha.value }
        )
    }
}

// ─────────────────────────────────────────
// REVERSE EFFECT - Rotation animation
// ─────────────────────────────────────────

@Composable
fun ReverseEffect(onComplete: () -> Unit) {
    val rotation = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        launch { rotation.animateTo(360f, tween(1000, easing = FastOutSlowInEasing)) }
        launch {
            delay(700)
            alpha.animateTo(0f, tween(300))
        }
        delay(1000)
        onComplete()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "⇄",
            color = AccentGold,
            fontSize = 200.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.graphicsLayer {
                rotationZ = rotation.value
                this.alpha = alpha.value
            }
        )

        Text(
            text = "REVERSED!",
            color = AccentGold,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 3.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
                .graphicsLayer { this.alpha = alpha.value }
        )
    }
}

// ─────────────────────────────────────────
// DRAW TWO EFFECT - Two cards fly
// ─────────────────────────────────────────

@Composable
fun DrawTwoEffect(onComplete: () -> Unit) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.3f) }

    LaunchedEffect(Unit) {
        launch { alpha.animateTo(1f, tween(300)) }
        launch { scale.animateTo(1.2f, tween(400, easing = FastOutSlowInEasing)) }
        delay(600)
        launch { alpha.animateTo(0f, tween(400)) }
        delay(400)
        onComplete()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .size(width = 80.dp, height = 120.dp)
                            .graphicsLayer {
                                this.alpha = alpha.value
                                scaleX = scale.value
                                scaleY = scale.value
                            }
                            .background(CardBlue, RoundedCornerShape(10.dp))
                            .border(3.dp, Color.White, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+2",
                            color = Color.White,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Text(
                text = "DRAW TWO!",
                color = CardBlue,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 3.sp,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .graphicsLayer { this.alpha = alpha.value }
            )
        }
    }
}

// ─────────────────────────────────────────
// WILD DRAW FOUR EFFECT - Lightning
// ─────────────────────────────────────────

@Composable
fun WildDrawFourEffect(onComplete: () -> Unit) {
    val flash = remember { Animatable(0f) }
    val shake = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        repeat(3) {
            flash.animateTo(1f, tween(100))
            flash.animateTo(0f, tween(100))
        }
        launch { shake.animateTo(1f, tween(600)) }
        delay(600)
        onComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = flash.value * 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer {
                translationX = (kotlin.math.sin(shake.value * 30f) * 20f)
            }
        ) {
            Text(text = "⚡", fontSize = 120.sp)
            Text(
                text = "+4 DRAW FOUR!",
                color = AccentGold,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp
            )
        }
    }
}

// ─────────────────────────────────────────
// WILD EFFECT - Color explosion
// ─────────────────────────────────────────

@Composable
fun WildEffect(onComplete: () -> Unit) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        launch { scale.animateTo(1.5f, tween(600, easing = FastOutSlowInEasing)) }
        delay(600)
        launch { alpha.animateTo(0f, tween(300)) }
        delay(300)
        onComplete()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                    this.alpha = alpha.value
                }
                .background(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            CardRed, CardYellow, CardGreen, CardBlue, CardRed
                        )
                    ),
                    shape = androidx.compose.foundation.shape.CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "★", color = Color.White, fontSize = 100.sp, fontWeight = FontWeight.Black)
        }
    }
}

// ─────────────────────────────────────────
// HELPERS
// ─────────────────────────────────────────

private fun getCardColor(color: String): Color = when (color) {
    "RED" -> CardRed
    "BLUE" -> CardBlue
    "GREEN" -> CardGreen
    "YELLOW" -> CardYellow
    else -> CardWild
}

private fun getCardText(value: String): String = when (value) {
    "SKIP" -> "⊘"
    "REVERSE" -> "⇄"
    "DRAW_TWO" -> "+2"
    "WILD" -> "★"
    "WILD_DRAW_FOUR" -> "+4"
    else -> value
}

