package com.nuno.app.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors

enum class CardSize(val width: Dp, val height: Dp, val fontSize: Int) {
    SMALL(50.dp, 75.dp, 20),
    MEDIUM(65.dp, 95.dp, 28),
    LARGE(90.dp, 130.dp, 38)
}

@Composable
fun UnoCardView(
    card: GameCardData,
    size: CardSize = CardSize.MEDIUM,
    isPlayable: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val cardColor = when (card.color) {
        "RED" -> GameColors.CardRed
        "BLUE" -> GameColors.CardBlue
        "GREEN" -> GameColors.CardGreen
        "YELLOW" -> GameColors.CardYellow
        else -> GameColors.CardBlack
    }

    val displayText = when (card.value) {
        "SKIP" -> "⊘"
        "REVERSE" -> "⇄"
        "DRAW_TWO" -> "+2"
        "WILD" -> "★"
        "WILD_DRAW_FOUR" -> "+4"
        else -> card.value
    }

    val textColor = if (card.color == "YELLOW") Color.Black else Color.White

    Box(
        modifier = Modifier
            .size(width = size.width, height = size.height)
            .shadow(
                elevation = if (isPlayable) 12.dp else 4.dp,
                shape = RoundedCornerShape(8.dp),
                spotColor = if (isPlayable) GameColors.Cyan else Color.Black
            )
            .background(cardColor, RoundedCornerShape(8.dp))
            .border(
                width = if (isPlayable) 3.dp else 1.dp,
                color = if (isPlayable) GameColors.Cyan else Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        // Card value
        Text(
            text = displayText,
            color = textColor,
            fontSize = size.fontSize.sp,
            fontWeight = FontWeight.Black
        )

        // Top left small value
        Text(
            text = displayText,
            color = textColor.copy(alpha = 0.7f),
            fontSize = (size.fontSize / 3).sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(4.dp)
        )

        // Bottom right small value
        Text(
            text = displayText,
            color = textColor.copy(alpha = 0.7f),
            fontSize = (size.fontSize / 3).sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(4.dp)
        )
    }
}

@Composable
fun UnoCardBack(size: CardSize = CardSize.MEDIUM) {
    Box(
        modifier = Modifier
            .size(width = size.width, height = size.height)
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .background(
                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                    listOf(GameColors.Blue, GameColors.Purple)
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .border(2.dp, GameColors.Gold, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "NUNO",
            color = Color.White,
            fontSize = (size.fontSize / 2).sp,
            fontWeight = FontWeight.Black
        )
    }
}