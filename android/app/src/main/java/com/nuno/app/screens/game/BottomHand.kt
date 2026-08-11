package com.nuno.app.screens.game

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomHand(
    cards: List<GameCardData>,
    currentColor: String,
    currentValue: String,
    isMyTurn: Boolean,
    onPlayCard: (GameCardData) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy((-16).dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        cards.forEach { card ->
            val isPlayable = isMyTurn && (card.type == "WILD" || card.color == currentColor || card.value == currentValue)
            UnoCard(
                card = card,
                size = CardSize.MEDIUM,
                isPlayable = isPlayable,
                onClick = { onPlayCard(card) }
            )
        }
    }
}