package com.nuno.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens

@Composable
fun GamePanel(
    modifier: Modifier = Modifier,
    backgroundColor: Color = GameColors.SurfaceCard,
    borderColor: Color = GameColors.BorderPurple.copy(alpha = 0.25f),
    borderWidth: Dp = GameDimens.borderThin,
    radius: Dp = GameDimens.radiusMd,
    elevation: Dp = 8.dp,
    glassEffect: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val bgBrush = if (glassEffect) {
        Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.08f),
                Color.White.copy(alpha = 0.02f),
                GameColors.SurfaceCard.copy(alpha = 0.85f)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                backgroundColor.copy(alpha = 0.95f),
                backgroundColor
            )
        )
    }

    val shape = RoundedCornerShape(radius)

    Box(
        modifier = modifier
            .shadow(elevation, shape, spotColor = Color.Black.copy(0.6f))
            .background(bgBrush, shape)
            .border(
                width = borderWidth,
                brush = Brush.linearGradient(
                    colors = if (glassEffect) listOf(
                        Color.White.copy(alpha = 0.18f),
                        Color.White.copy(alpha = 0.05f)
                    ) else listOf(
                        borderColor.copy(alpha = 0.8f),
                        borderColor.copy(alpha = 0.15f)
                    )
                ),
                shape = shape
            )
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        // Top highlight for depth
        if (!glassEffect) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Color.White.copy(alpha = 0.06f),
                        RoundedCornerShape(topStart = radius, topEnd = radius)
                    )
            )
        }
        content()
    }
}

@Composable
fun GlassPanel(
    modifier: Modifier = Modifier,
    radius: Dp = GameDimens.radiusLg,
    borderColor: Color = GameColors.BorderGlass,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    GamePanel(
        modifier = modifier,
        borderColor = borderColor,
        radius = radius,
        glassEffect = true,
        elevation = 12.dp,
        onClick = onClick,
        content = content
    )
}

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    accentColor: Color = GameColors.Gold,
    radius: Dp = GameDimens.radiusLg,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(radius)
    Box(
        modifier = modifier
            .shadow(16.dp, shape, spotColor = accentColor.copy(0.25f))
            .background(
                Brush.verticalGradient(
                    listOf(
                        GameColors.SurfaceLight,
                        GameColors.SurfaceCard
                    )
                ),
                shape
            )
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(
                        accentColor.copy(0.6f),
                        Color.White.copy(0.08f)
                    )
                ),
                shape
            )
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.5.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            accentColor.copy(0.4f),
                            Color.Transparent
                        )
                    )
                )
        )
        content()
    }
}
