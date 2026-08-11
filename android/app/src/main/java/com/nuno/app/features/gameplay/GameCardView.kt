package com.nuno.app.features.gameplay

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nuno.app.screens.game.CardSize
import com.nuno.app.screens.game.GameCardData
import com.nuno.app.screens.game.UnoCardBack
import com.nuno.app.screens.game.UnoCardView

@Composable
fun GameCardView(
    card: GameCard,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isPlayable: Boolean = false
) {
    val cardData = GameCardData(
        cardId = card.cardId,
        color = card.color,
        value = card.value,
        type = card.type
    )

    UnoCardView(
        card = cardData,
        size = CardSize.MEDIUM,
        isPlayable = isPlayable,
        onClick = onClick
    )
}

@Composable
fun CardBack(modifier: Modifier = Modifier) {
    UnoCardBack(size = CardSize.MEDIUM)
}