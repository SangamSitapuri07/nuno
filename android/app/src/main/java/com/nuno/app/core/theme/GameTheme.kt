package com.nuno.app.core.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object GameTheme {
    object Spacing {
        val xs = 4.dp
        val sm = 8.dp
        val md = 16.dp
        val lg = 24.dp
        val xl = 32.dp
    }

    object Radius {
        val sm = 8.dp
        val md = 12.dp
        val lg = 16.dp
        val xl = 24.dp
        val full = 999.dp
    }

    object TextSize {
        val xs = 10.sp
        val sm = 12.sp
        val md = 14.sp
        val lg = 16.sp
        val xl = 20.sp
        val xxl = 28.sp
        val title = 36.sp
    }

    object GlowColor {
        val purple = Color(0xFF9D4EDD)
        val blue = Color(0xFF4CC9F0)
        val gold = Color(0xFFFFD700)
        val red = Color(0xFFE53935)
    }
}