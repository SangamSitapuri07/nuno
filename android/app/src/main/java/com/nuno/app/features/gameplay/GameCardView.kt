package com.nuno.app.features.gameplay

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*

@Composable
fun GameCardView(
    card: GameCard,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isPlayable: Boolean = false
) {
    val cardColor = when (card.color) {
        "RED" -> CardRed
        "BLUE" -> CardBlue
        "GREEN" -> CardGreen
        "YELLOW" -> CardYellow
        "WILD" -> CardWild
        else -> NeutralGray600
    }

    val textColor = when (card.color) {
        "YELLOW" -> Color.Black
        else -> Color.White
    }

    val displayText = when (card.value) {
        "SKIP" -> "⊘"
        "REVERSE" -> "⇄"
        "DRAW_TWO" -> "+2"
        "WILD" -> "★"
        "WILD_DRAW_FOUR" -> "+4"
        else -> card.value
    }

    Card(
        modifier = modifier
            .width(Dimensions.CardWidth)
            .height(Dimensions.CardHeight)
            .then(
                if (onClick != null && isPlayable) {
                    Modifier
                        .border(3.dp, AccentCyan, RoundedCornerShape(Dimensions.RadiusMedium))
                } else if (onClick != null) {
                    Modifier
                } else {
                    Modifier
                }
            ),
        onClick = onClick ?: {},
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(Dimensions.RadiusMedium)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (card.value == "WILD" || card.value == "WILD_DRAW_FOUR") {
                // Wild card colored corners
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.sweepGradient(
                                colors = listOf(
                                    CardRed, CardYellow, CardGreen, CardBlue, CardRed
                                )
                            )
                        )
                )
            }

            Text(
                text = displayText,
                color = textColor,
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CardBack(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(Dimensions.CardWidth)
            .height(Dimensions.CardHeight),
        colors = CardDefaults.cardColors(containerColor = PrimaryDark),
        shape = RoundedCornerShape(Dimensions.RadiusMedium)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(PrimaryBlue, PrimaryPurple)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "NUNO",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}