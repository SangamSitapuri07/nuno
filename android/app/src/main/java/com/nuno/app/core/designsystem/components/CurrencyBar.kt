package com.nuno.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PremiumCurrencyPill(icon = "🪙", value = coins, accent = GameColors.Gold)
        PremiumCurrencyPill(icon = "💎", value = gems, accent = GameColors.Cyan)
    }
}

@Composable
private fun PremiumCurrencyPill(icon: String, value: Int, accent: Color) {
    Box(
        modifier = Modifier
            .shadow(6.dp, RoundedCornerShape(20.dp), spotColor = accent.copy(0.25f))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF212654),
                        Color(0xFF171A36)
                    )
                ),
                RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                Brush.linearGradient(listOf(Color.White.copy(0.15f), Color.Transparent)),
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .shadow(4.dp, RoundedCornerShape(8.dp), spotColor = accent.copy(0.5f))
                    .background(
                        Brush.radialGradient(listOf(accent.copy(0.9f), accent.copy(0.6f))),
                        RoundedCornerShape(8.dp)
                    )
                    .border(1.dp, Color.White.copy(0.4f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = formatNumber(value),
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
fun PremiumCurrencyDisplay(
    icon: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(listOf(Color(0xFF1E2249), Color(0xFF131636))),
                RoundedCornerShape(12.dp)
            )
            .border(1.dp, accent.copy(0.3f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

private fun formatNumber(n: Int): String = when {
    n >= 1_000_000 -> String.format("%.1fM", n / 1_000_000.0)
    n >= 1_000 -> String.format("%.1fK", n / 1_000.0)
    else -> n.toString()
}
