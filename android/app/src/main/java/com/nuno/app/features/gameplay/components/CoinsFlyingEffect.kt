package com.nuno.app.features.gameplay.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun CoinsFlyingEffect(coinCount: Int = 15) {
    Box(modifier = Modifier.fillMaxSize()) {
        repeat(coinCount) { index ->
            FlyingCoin(index = index, total = coinCount)
        }
    }
}

@Composable
private fun FlyingCoin(index: Int, total: Int) {
    val progress = remember { Animatable(0f) }
    val angle = remember { (index * (360f / total)) + Random.nextFloat() * 30f }
    val distance = remember { Random.nextFloat() * 250f + 150f }
    val delay = remember { index * 40 }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        progress.animateTo(1f, tween(1500, easing = FastOutSlowInEasing))
    }

    val angleRad = Math.toRadians(angle.toDouble())
    val currentDistance = distance * progress.value
    val x = (cos(angleRad) * currentDistance).toFloat()
    val y = (sin(angleRad) * currentDistance).toFloat() + (progress.value * progress.value * 200f)

    val alpha = when {
        progress.value < 0.2f -> progress.value * 5f
        progress.value > 0.8f -> (1f - progress.value) * 5f
        else -> 1f
    }

    val rotation = progress.value * 720f

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .graphicsLayer {
                    translationX = x
                    translationY = y
                    rotationY = rotation
                    this.alpha = alpha
                }
                .background(
                    color = Color(0xFFFFD700),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🪙",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}