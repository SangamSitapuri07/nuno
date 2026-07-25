package com.nuno.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.GameTypography

enum class ButtonStyle {
    PRIMARY,
    SECONDARY,
    GOLD,
    DANGER,
    GREEN,
    OUTLINE
}

@Composable
fun GameButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.PRIMARY,
    height: Dp = GameDimens.buttonLg,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    val gradient = when (style) {
        ButtonStyle.PRIMARY -> listOf(GameColors.Blue, GameColors.Purple)
        ButtonStyle.SECONDARY -> listOf(GameColors.SurfaceLight, GameColors.Surface)
        ButtonStyle.GOLD -> listOf(GameColors.Gold, GameColors.GoldDark)
        ButtonStyle.DANGER -> listOf(GameColors.Red, Color(0xFFCC0000))
        ButtonStyle.GREEN -> listOf(GameColors.Green, Color(0xFF00B862))
        ButtonStyle.OUTLINE -> listOf(Color.Transparent, Color.Transparent)
    }

    val borderColor = when (style) {
        ButtonStyle.OUTLINE -> GameColors.BorderGlow
        ButtonStyle.GOLD -> GameColors.Gold
        else -> Color.Transparent
    }

    val textColor = when (style) {
        ButtonStyle.GOLD -> Color.Black
        else -> GameColors.TextWhite
    }

    Box(
        modifier = modifier
            .height(height)
            .shadow(
                elevation = if (enabled) 8.dp else 0.dp,
                shape = RoundedCornerShape(GameDimens.radiusMd),
                spotColor = gradient[0].copy(alpha = 0.5f)
            )
            .background(
                brush = Brush.horizontalGradient(
                    if (enabled) gradient else listOf(GameColors.TextDark, GameColors.TextDisabled)
                ),
                shape = RoundedCornerShape(GameDimens.radiusMd)
            )
            .then(
                if (borderColor != Color.Transparent) {
                    Modifier.border(GameDimens.borderMedium, borderColor, RoundedCornerShape(GameDimens.radiusMd))
                } else Modifier
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = GameDimens.paddingLg)
        ) {
            leadingIcon?.invoke()
            if (leadingIcon != null) Spacer(modifier = Modifier.width(GameDimens.paddingSm))
            Text(
                text = text,
                style = GameTypography.button,
                color = if (enabled) textColor else GameColors.TextDisabled
            )
        }
    }
}