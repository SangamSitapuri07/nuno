package com.nuno.app.features.lobby.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RotatingCardRing(
    modifier: Modifier = Modifier,
    cardCount: Int = 40,
    radiusDp: Int = 320
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ring")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(60_000, easing = LinearEasing)
        ),
        label = "rotation"
    )

    val cards = remember {
        List(cardCount) {
            CardData(
                color = when (it % 4) {
                    0 -> Color(0xFFE53935)
                    1 -> Color(0xFF1E88E5)
                    2 -> Color(0xFF43A047)
                    else -> Color(0xFFFDD835)
                },
                value = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "⊘", "⇄", "+2").random()
            )
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size((radiusDp * 2).dp)
                .rotate(rotation)
        ) {
            cards.forEachIndexed { index, card ->
                val angle = (index * (360f / cardCount)).toDouble()
                val angleRad = Math.toRadians(angle)
                val x = (cos(angleRad) * radiusDp).toFloat()
                val y = (sin(angleRad) * radiusDp).toFloat()

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .graphicsLayer {
                            translationX = x * density
                            translationY = y * density
                            rotationZ = angle.toFloat() + 90f
                        }
                ) {
                    UnoCard(card = card)
                }
            }
        }
    }
}

@Composable
private fun UnoCard(card: CardData) {
    val textColor = if (card.color == Color(0xFFFDD835)) Color.Black else Color.White

    Box(
        modifier = Modifier
            .size(width = 50.dp, height = 75.dp)
            .shadow(4.dp, RoundedCornerShape(6.dp))
            .background(card.color, RoundedCornerShape(6.dp))
            .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = card.value,
            color = textColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black
        )
    }
}

private data class CardData(val color: Color, val value: String)

private val Float.density: Float get() = this