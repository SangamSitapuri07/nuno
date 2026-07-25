package com.nuno.app.features.store.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*

@Composable
fun CurrencyBar(coins: Int, gems: Int = 0, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CurrencyChip(icon = "🪙", value = coins, color = AccentGold)
        CurrencyChip(icon = "💎", value = gems, color = AccentCyan)
    }
}

@Composable
private fun CurrencyChip(icon: String, value: Int, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = value.toString(), color = color, fontSize = 14.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.Default.Add, null, tint = color, modifier = Modifier.size(14.dp))
        }
    }
}