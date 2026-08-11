package com.nuno.app.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.components.GameAvatar

@Composable
fun PlayerAvatar(
    username: String,
    level: Int,
    borderColor: Color,
    size: Dp = 48.dp
) {
    Box {
        GameAvatar(username = username, size = size, borderColor = borderColor)
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(borderColor, CircleShape)
                .align(Alignment.TopEnd),
            contentAlignment = Alignment.Center
        ) {
            Text("$level", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}