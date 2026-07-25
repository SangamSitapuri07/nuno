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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ShuffleAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "shuffle")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 720f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        ),
        label = "rotation"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                // Multiple cards swirling
                for (i in 0..7) {
                    val angle = (i * 45f) + rotation
                    val angleRad = Math.toRadians(angle.toDouble())
                    val radius = 60f
                    val x = (cos(angleRad) * radius).toFloat()
                    val y = (sin(angleRad) * radius).toFloat()

                    val cardColor = when (i % 4) {
                        0 -> CardRed
                        1 -> CardBlue
                        2 -> CardGreen
                        else -> CardYellow
                    }

                    Box(
                        modifier = Modifier
                            .size(width = 60.dp, height = 90.dp)
                            .graphicsLayer {
                                translationX = x * density
                                translationY = y * density
                                rotationZ = angle
                            }
                            .background(cardColor, RoundedCornerShape(8.dp))
                            .border(2.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "SHUFFLING",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp,
                modifier = Modifier.graphicsLayer {
                    scaleX = pulse
                    scaleY = pulse
                }
            )
        }
    }
}