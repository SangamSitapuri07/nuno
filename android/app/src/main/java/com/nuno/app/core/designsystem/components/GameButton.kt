package com.nuno.app.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.GameTypography

enum class ButtonStyle {
    PRIMARY,
    SECONDARY,
    GOLD,
    DANGER,
    GREEN,
    OUTLINE,
    GLASS
}

@Composable
fun GameButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.PRIMARY,
    height: Dp = GameDimens.buttonLg,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.96f else 1f,
        label = "scale"
    )

    val (gradient, borderBrush, shadowColor, textColor) = when (style) {
        ButtonStyle.PRIMARY -> Quad(
            Brush.linearGradient(listOf(Color(0xFF4A6BFF), Color(0xFF7B5CFF))),
            Brush.linearGradient(listOf(Color.White.copy(0.3f), Color.Transparent)),
            Color(0xFF4A6BFF),
            Color.White
        )
        ButtonStyle.SECONDARY -> Quad(
            Brush.linearGradient(listOf(Color(0xFF1E2249), Color(0xFF131636))),
            Brush.linearGradient(listOf(Color(0x33FFFFFF), Color.Transparent)),
            Color.Black.copy(0.5f),
            Color.White
        )
        ButtonStyle.GOLD -> Quad(
            Brush.linearGradient(listOf(Color(0xFFFFD23F), Color(0xFFFF9A00))),
            Brush.linearGradient(listOf(Color.White.copy(0.9f), Color(0xFFFFC71F).copy(0.5f))),
            Color(0xFFFFC71F),
            Color.Black
        )
        ButtonStyle.DANGER -> Quad(
            Brush.linearGradient(listOf(Color(0xFFFF3B5C), Color(0xFFD5002B))),
            Brush.linearGradient(listOf(Color.White.copy(0.3f), Color.Transparent)),
            Color(0xFFFF3B5C),
            Color.White
        )
        ButtonStyle.GREEN -> Quad(
            Brush.linearGradient(listOf(Color(0xFF00E676), Color(0xFF00B248))),
            Brush.linearGradient(listOf(Color.White.copy(0.4f), Color.Transparent)),
            Color(0xFF00E676),
            Color.White
        )
        ButtonStyle.OUTLINE -> Quad(
            Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
            Brush.linearGradient(listOf(GameColors.Cyan.copy(0.8f), GameColors.Blue.copy(0.5f))),
            Color.Transparent,
            Color.White
        )
        ButtonStyle.GLASS -> Quad(
            Brush.linearGradient(listOf(Color.White.copy(0.12f), Color.White.copy(0.05f))),
            Brush.linearGradient(listOf(Color.White.copy(0.18f), Color.White.copy(0.05f))),
            Color.Black.copy(0.3f),
            Color.White
        )
    }

    val shape = RoundedCornerShape(if (height <= 36.dp) 10.dp else 14.dp)
    val elevation = if (style == ButtonStyle.OUTLINE || style == ButtonStyle.GLASS) 0.dp else 10.dp

    Box(
        modifier = modifier
            .height(height)
            .scale(scale)
            .shadow(
                elevation = if (enabled) elevation else 0.dp,
                shape = shape,
                spotColor = shadowColor.copy(alpha = 0.6f)
            )
            .background(
                brush = if (enabled) gradient else Brush.linearGradient(
                    listOf(
                        GameColors.SurfaceLight.copy(alpha = 0.5f),
                        GameColors.Surface.copy(alpha = 0.5f)
                    )
                ),
                shape = shape
            )
            .border(
                width = if (style == ButtonStyle.OUTLINE) 1.5.dp else 1.dp,
                brush = if (enabled) borderBrush else Brush.linearGradient(
                    listOf(
                        GameColors.TextDark.copy(0.3f),
                        GameColors.TextDark.copy(0.1f)
                    )
                ),
                shape = shape
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (style != ButtonStyle.OUTLINE && style != ButtonStyle.GLASS) {
            val topCornerRadius = if (height <= 36.dp) 10.dp else 14.dp
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        Color.White.copy(alpha = 0.35f),
                        RoundedCornerShape(topStart = topCornerRadius, topEnd = topCornerRadius)
                    )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 18.dp)
        ) {
            leadingIcon?.let {
                it()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = if (height >= 52.dp) GameTypography.buttonLarge else GameTypography.button,
                color = if (enabled) textColor else GameColors.TextDark,
                fontSize = when {
                    height <= 32.dp -> 11.sp
                    height <= 44.dp -> 12.sp
                    height >= 60.dp -> 16.sp
                    else -> 13.sp
                },
                fontWeight = FontWeight.Black
            )
            trailingIcon?.let {
                Spacer(modifier = Modifier.width(8.dp))
                it()
            }
        }
    }
}

private data class Quad(
    val first: Brush,
    val second: Brush,
    val third: Color,
    val fourth: Color
)
