package com.nuno.app.features.gameplay.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.gameplay.GameState

@Composable
fun RightInfoPanel(gameState: GameState, remainingTime: Int, isMyTurn: Boolean) {
    Column(
        modifier = Modifier.width(120.dp).fillMaxHeight().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        InfoCard(
            title = if (isMyTurn) "YOUR TURN" else "OPPONENT",
            value = "${remainingTime}s",
            containerColor = if (isMyTurn) SuccessGreen else SurfaceCard
        )
        InfoCard(
            title = "DIRECTION",
            value = if (gameState.direction == "CLOCKWISE") "↻" else "↺"
        )
        InfoCard(title = "TURNS", value = gameState.totalTurns.toString())
    }
}

@Composable
private fun InfoCard(title: String, value: String, containerColor: androidx.compose.ui.graphics.Color = SurfaceCard) {
    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, BorderPurple.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, color = if (containerColor == SuccessGreen) TextPrimary else TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
            Text(value, color = TextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Black)
        }
    }
}