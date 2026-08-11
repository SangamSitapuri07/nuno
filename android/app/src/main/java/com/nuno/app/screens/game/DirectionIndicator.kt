package com.nuno.app.screens.game

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun DirectionIndicator(direction: String) {
    Text(
        text = if (direction == "CLOCKWISE") "↻" else "↺",
        color = Color(0xFFFFB300).copy(alpha = 0.35f),
        fontSize = 180.sp,
        fontWeight = FontWeight.Black
    )
}