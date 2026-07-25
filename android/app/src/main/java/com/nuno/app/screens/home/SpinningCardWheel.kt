package com.nuno.app.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpinningCardWheel(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "wheel")

    val cardRotation by infiniteTransition.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(55_000, easing = LinearEasing)),
        label = "cards"
    )

    val energyRotation by infiniteTransition.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(30_000, easing = LinearEasing)),
        label = "energy"
    )

    val reverseRotation by infiniteTransition.animateFloat(
        360f, 0f,
        infiniteRepeatable(tween(25_000, easing = LinearEasing)),
        label = "reverse"
    )

    val glowPulse by infiniteTransition.animateFloat(
        0.3f, 0.85f,
        infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = "glow"
    )

    val sparkle by infiniteTransition.animateFloat(
        0.4f, 1f,
        infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "sparkle"
    )

    val particleOrbit by infiniteTransition.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(6000, easing = LinearEasing)),
        label = "particle"
    )

    val cards = remember {
        val colors = listOf(GameColors.CardRed, GameColors.CardBlue, GameColors.CardGreen, GameColors.CardYellow)
        val values = listOf("7", "8", "9", "9", "+2", "0", "1", "2", "3", "4", "5", "0", "9", "5", "4", "3", "2", "1", "0", "6", "5", "3", "4", "7")
        List(24) { Pair(colors[it % 4], values[it % values.size]) }
    }

    Box(modifier = modifier.size(340.dp), contentAlignment = Alignment.Center) {
        // Layer 1: Outer energy aura
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val outerRadius = size.width * 0.5f

            // Soft outer aura
            drawCircle(
                Brush.radialGradient(
                    listOf(
                        Color(0xFF9D4EDD).copy(alpha = glowPulse * 0.3f),
                        Color(0xFF4CC9F0).copy(alpha = glowPulse * 0.15f),
                        Color.Transparent
                    ),
                    center = center, radius = outerRadius
                ),
                radius = outerRadius, center = center
            )

            // Outer bright ring 1 (cyan)
            drawCircle(
                Color(0xFF4CC9F0).copy(alpha = glowPulse * 0.7f),
                radius = size.width * 0.48f, center = center,
                style = Stroke(width = 2.5f)
            )
            // Outer glow for ring 1
            drawCircle(
                Color(0xFF4CC9F0).copy(alpha = glowPulse * 0.15f),
                radius = size.width * 0.485f, center = center,
                style = Stroke(width = 8f)
            )

            // Ring 2 (purple)
            drawCircle(
                Color(0xFF9D4EDD).copy(alpha = glowPulse * 0.5f),
                radius = size.width * 0.455f, center = center,
                style = Stroke(width = 1.5f)
            )

            // Ring 3 (inner cyan)
            drawCircle(
                Color(0xFF4CC9F0).copy(alpha = glowPulse * 0.4f),
                radius = size.width * 0.35f, center = center,
                style = Stroke(width = 2f)
            )
            drawCircle(
                Color(0xFF4CC9F0).copy(alpha = glowPulse * 0.1f),
                radius = size.width * 0.355f, center = center,
                style = Stroke(width = 6f)
            )

            // Inner hub ring (dark metallic)
            drawCircle(
                Color(0xFF1A2040).copy(alpha = 0.8f),
                radius = size.width * 0.2f, center = center
            )
            drawCircle(
                Color(0xFF4CC9F0).copy(alpha = 0.3f),
                radius = size.width * 0.2f, center = center,
                style = Stroke(width = 2f)
            )
            drawCircle(
                Color(0xFF1A2040).copy(alpha = 0.9f),
                radius = size.width * 0.15f, center = center
            )
            drawCircle(
                Color(0xFF4CC9F0).copy(alpha = 0.2f),
                radius = size.width * 0.15f, center = center,
                style = Stroke(width = 1.5f)
            )

            // Energy swirl particles (rotating in outer ring)
            for (i in 0..11) {
                val eAngle = Math.toRadians((energyRotation + i * 30).toDouble())
                val ex = center.x + (cos(eAngle) * size.width * 0.47f).toFloat()
                val ey = center.y + (sin(eAngle) * size.height * 0.47f).toFloat()
                val eColor = if (i % 2 == 0) Color(0xFF4CC9F0) else Color(0xFF9D4EDD)
                drawCircle(eColor.copy(alpha = 0.6f), radius = 2f, center = Offset(ex, ey))
                drawCircle(eColor.copy(alpha = 0.15f), radius = 6f, center = Offset(ex, ey))
            }

            // Inner ring particles (reverse rotation)
            for (i in 0..7) {
                val rAngle = Math.toRadians((reverseRotation + i * 45).toDouble())
                val rx = center.x + (cos(rAngle) * size.width * 0.35f).toFloat()
                val ry = center.y + (sin(rAngle) * size.height * 0.35f).toFloat()
                drawCircle(Color(0xFF4CC9F0).copy(alpha = 0.4f), radius = 1.5f, center = Offset(rx, ry))
            }

            // Diamond indicators at cardinal points
            val diamondSize = 10f
            val indicatorRadius = size.width * 0.48f

            // Top diamond
            val topCenter = Offset(center.x, center.y - indicatorRadius)
            drawPath(
                Path().apply {
                    moveTo(topCenter.x, topCenter.y - diamondSize)
                    lineTo(topCenter.x + diamondSize * 0.6f, topCenter.y)
                    lineTo(topCenter.x, topCenter.y + diamondSize)
                    lineTo(topCenter.x - diamondSize * 0.6f, topCenter.y)
                    close()
                },
                Color(0xFF4CC9F0).copy(alpha = sparkle)
            )

            // Bottom diamond
            val bottomCenter = Offset(center.x, center.y + indicatorRadius)
            drawPath(
                Path().apply {
                    moveTo(bottomCenter.x, bottomCenter.y - diamondSize)
                    lineTo(bottomCenter.x + diamondSize * 0.6f, bottomCenter.y)
                    lineTo(bottomCenter.x, bottomCenter.y + diamondSize)
                    lineTo(bottomCenter.x - diamondSize * 0.6f, bottomCenter.y)
                    close()
                },
                Color(0xFF4CC9F0).copy(alpha = sparkle)
            )

            // Left diamond
            val leftCenter = Offset(center.x - indicatorRadius, center.y)
            drawPath(
                Path().apply {
                    moveTo(leftCenter.x - diamondSize, leftCenter.y)
                    lineTo(leftCenter.x, leftCenter.y - diamondSize * 0.6f)
                    lineTo(leftCenter.x + diamondSize, leftCenter.y)
                    lineTo(leftCenter.x, leftCenter.y + diamondSize * 0.6f)
                    close()
                },
                Color(0xFF4CC9F0).copy(alpha = sparkle)
            )

            // Right diamond
            val rightCenter = Offset(center.x + indicatorRadius, center.y)
            drawPath(
                Path().apply {
                    moveTo(rightCenter.x - diamondSize, rightCenter.y)
                    lineTo(rightCenter.x, rightCenter.y - diamondSize * 0.6f)
                    lineTo(rightCenter.x + diamondSize, rightCenter.y)
                    lineTo(rightCenter.x, rightCenter.y + diamondSize * 0.6f)
                    close()
                },
                Color(0xFF4CC9F0).copy(alpha = sparkle)
            )

            // Sparkle star bursts at cardinal points
            listOf(topCenter, bottomCenter, leftCenter, rightCenter).forEach { point ->
                // Cross sparkle
                drawLine(Color.White.copy(alpha = sparkle * 0.8f), Offset(point.x - 12f, point.y), Offset(point.x + 12f, point.y), strokeWidth = 0.8f)
                drawLine(Color.White.copy(alpha = sparkle * 0.8f), Offset(point.x, point.y - 12f), Offset(point.x, point.y + 12f), strokeWidth = 0.8f)
                // Diagonal sparkle
                drawLine(Color.White.copy(alpha = sparkle * 0.5f), Offset(point.x - 8f, point.y - 8f), Offset(point.x + 8f, point.y + 8f), strokeWidth = 0.5f)
                drawLine(Color.White.copy(alpha = sparkle * 0.5f), Offset(point.x + 8f, point.y - 8f), Offset(point.x - 8f, point.y + 8f), strokeWidth = 0.5f)
            }
        }

        // Layer 2: Rotating cards
        cards.forEachIndexed { index, (color, value) ->
            val angle = (index * (360f / cards.size)) + cardRotation
            val angleRad = Math.toRadians(angle.toDouble())
            val radius = 125f

            Box(
                modifier = Modifier
                    .size(width = 42.dp, height = 60.dp)
                    .graphicsLayer {
                        translationX = (cos(angleRad) * radius).toFloat() * density
                        translationY = (sin(angleRad) * radius).toFloat() * density
                        rotationZ = angle + 90f
                    }
                    .shadow(10.dp, RoundedCornerShape(5.dp), spotColor = color.copy(alpha = 0.8f))
                    .background(
                        Brush.verticalGradient(
                            listOf(color, color.copy(alpha = 0.75f), color.copy(alpha = 0.9f))
                        ),
                        RoundedCornerShape(5.dp)
                    )
                    .border(1.5.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Glossy highlight
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .align(Alignment.TopCenter)
                        .background(
                            Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.3f), Color.Transparent)),
                            RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
                        )
                )

                Text(
                    value,
                    color = if (color == GameColors.CardYellow) Color.Black else Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Layer 3: Center NUNO letters
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            NunoLetter("N", GameColors.CardRed)
            NunoLetter("U", GameColors.CardBlue)
            NunoLetter("N", GameColors.CardGreen)
            NunoLetter("O", GameColors.CardYellow, Color.Black)
        }
    }
}

@Composable
private fun NunoLetter(letter: String, bg: Color, textColor: Color = Color.White) {
    Box(
        modifier = Modifier
            .size(width = 40.dp, height = 50.dp)
            .shadow(8.dp, RoundedCornerShape(5.dp), spotColor = bg)
            .background(
                Brush.verticalGradient(listOf(bg, bg.copy(alpha = 0.8f))),
                RoundedCornerShape(5.dp)
            )
            .border(2.5.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(5.dp)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.35f), Color.Transparent)),
                    RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
                )
        )
        Text(letter, color = textColor, fontSize = 24.sp, fontWeight = FontWeight.Black)
    }
}