package com.nuno.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens

@Composable
fun GamePanel(
    modifier: Modifier = Modifier,
    backgroundColor: Color = GameColors.SurfaceCard,
    borderColor: Color = GameColors.BorderPurple.copy(alpha = 0.3f),
    borderWidth: Dp = GameDimens.borderThin,
    radius: Dp = GameDimens.radiusMd,
    elevation: Dp = 4.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(elevation, RoundedCornerShape(radius))
            .background(backgroundColor, RoundedCornerShape(radius))
            .border(borderWidth, borderColor, RoundedCornerShape(radius))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        content()
    }
}