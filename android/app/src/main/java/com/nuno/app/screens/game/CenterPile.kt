package com.nuno.app.screens.game

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CenterPile(
    topCard: GameCardData?,
    currentColor: String,
    drawPileCount: Int,
    direction: String,
    isMyTurn: Boolean,
    onDrawCard: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Direction Circular Arrows behind piles
        Text(
            text = if (direction == "CLOCKWISE") "↻" else "↺",
            color = Color(0xFFFFB300).copy(alpha = 0.25f),
            fontSize = 110.sp,
            fontWeight = FontWeight.Black
        )

        // Draw + Discard Piles
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Draw Pile
            Box(modifier = Modifier.clickable(enabled = isMyTurn) { onDrawCard() }) {
                UnoCardBack(size = CardSize.LARGE)
            }

            // Discard Pile
            topCard?.let { card ->
                UnoCard(card = card, size = CardSize.LARGE)
            }
        }

        // Current Card Panel (Floating box to the right of discard pile)
        CurrentCardPanel(
            topCard = topCard,
            currentColor = currentColor,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = 88.dp, y = 0.dp)
        )
    }
}