package com.nuno.app.screens.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Timer(remainingTime: Int, totalTime: Int = 30) {
    val progress = (remainingTime.toFloat() / totalTime.toFloat()).coerceIn(0f, 1f)

    val timerColor = when {
        remainingTime <= 5 -> Color(0xFFE50914)
        remainingTime <= 10 -> Color(0xFFFFC107)
        else -> Color(0xFF4CAF50)
    }

    Box(
        modifier = Modifier
            .size(42.dp)
            .background(Color(0xFF0F142A).copy(alpha = 0.88f), CircleShape)
            .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(2.dp)) {
            val strokeWidth = 3.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            val arcSize = Size(diameter, diameter)

            drawArc(
                color = Color.White.copy(alpha = 0.15f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth)
            )

            drawArc(
                color = timerColor,
                startAngle = -90f,
                sweepAngle = progress * 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$remainingTime",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "sec",
                color = Color(0xFFAAAAAA),
                fontSize = 7.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}