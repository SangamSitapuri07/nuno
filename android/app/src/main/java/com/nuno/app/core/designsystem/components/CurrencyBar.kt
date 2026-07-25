package com.nuno.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens

@Composable
fun CurrencyBar(
    coins: Int,
    gems: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(GameDimens.paddingSm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CurrencyChip(icon = "🪙", value = coins, color = GameColors.Gold)
        CurrencyChip(icon = "💎", value = gems, color = GameColors.Cyan)
    }
}

@Composable
private fun CurrencyChip(icon: String, value: Int, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier
            .background(GameColors.Surface, RoundedCornerShape(GameDimens.radiusFull))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = icon, fontSize = 14.sp)
        Text(
            text = formatNumber(value),
            color = color,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatNumber(n: Int): String = when {
    n >= 1_000_000 -> String.format("%.1fM", n / 1_000_000.0)
    n >= 1_000 -> String.format("%.1fK", n / 1_000.0)
    else -> n.toString()
}