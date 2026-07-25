package com.nuno.app.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nuno.app.core.theme.*

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    borderColor: Color = BorderPurple.copy(alpha = 0.3f),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SurfaceCard.copy(alpha = 0.9f),
                        SurfaceCard.copy(alpha = 0.7f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
    ) {
        content()
    }
}