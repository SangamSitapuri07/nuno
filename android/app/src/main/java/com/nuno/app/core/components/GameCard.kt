package com.nuno.app.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nuno.app.core.theme.*

@Composable
fun GameCard(
    modifier: Modifier = Modifier,
    borderColor: Color = BorderPurple.copy(alpha = 0.5f),
    glowColor: Color = Color.Transparent,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val cardModifier = if (onClick != null) {
        modifier
            .shadow(8.dp, RoundedCornerShape(GameTheme.Radius.md), spotColor = glowColor)
    } else {
        modifier
    }

    Card(
        modifier = cardModifier,
        onClick = onClick ?: {},
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(GameTheme.Radius.md),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

@Composable
fun GlowingBorderCard(
    modifier: Modifier = Modifier,
    borderColors: List<Color> = listOf(AccentGold, Color(0xFFB8860B), AccentGold),
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Card(
        modifier = modifier
            .shadow(12.dp, RoundedCornerShape(GameTheme.Radius.md), spotColor = AccentGold.copy(alpha = 0.5f)),
        onClick = onClick ?: {},
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(GameTheme.Radius.md),
        border = BorderStroke(2.dp, Brush.linearGradient(borderColors))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}