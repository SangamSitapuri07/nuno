package com.nuno.app.features.gameplay.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.gameplay.GameCard

@Composable
fun PlayerHandRow(
    cards: List<GameCard>,
    currentColor: String,
    currentValue: String,
    isMyTurn: Boolean,
    onCardClick: (GameCard) -> Unit,
    onSortClick: () -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "YOUR HAND",
                color = TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onSortClick, contentPadding = PaddingValues(horizontal = 8.dp)) {
                    Icon(Icons.Default.Sort, null, tint = AccentCyan, modifier = Modifier.size(14.dp))
                    Text("Sort", color = AccentCyan, fontSize = 10.sp)
                }
                Text("${cards.size} cards", color = AccentCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(horizontal = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(cards) { card ->
                val isPlayable = isMyTurn && isCardPlayable(card, currentColor, currentValue)
                HandCard(
                    card = card,
                    isPlayable = isPlayable,
                    isMyTurn = isMyTurn,
                    onClick = {
                        android.util.Log.d("HandCard", "TAPPED: ${card.value} ${card.color} playable=$isPlayable myTurn=$isMyTurn")
                        onCardClick(card)
                    }
                )
            }
        }
    }
}

@Composable
private fun HandCard(
    card: GameCard,
    isPlayable: Boolean,
    isMyTurn: Boolean,
    onClick: () -> Unit
) {
    val cardColor = when (card.color) {
        "RED" -> CardRed
        "BLUE" -> CardBlue
        "GREEN" -> CardGreen
        "YELLOW" -> CardYellow
        else -> CardWild
    }

    val displayText = when (card.value) {
        "SKIP" -> "⊘"
        "REVERSE" -> "⇄"
        "DRAW_TWO" -> "+2"
        "WILD" -> "★"
        "WILD_DRAW_FOUR" -> "+4"
        else -> card.value
    }

    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.15f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .size(width = 65.dp, height = 95.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (isPlayable) 12.dp else 4.dp,
                shape = RoundedCornerShape(8.dp),
                spotColor = if (isPlayable) AccentCyan else Color.Black
            )
            .background(cardColor, RoundedCornerShape(8.dp))
            .border(
                width = if (isPlayable) 3.dp else 1.dp,
                color = when {
                    isPlayable -> AccentCyan
                    isMyTurn -> Color.White.copy(alpha = 0.3f)
                    else -> Color.White.copy(alpha = 0.1f)
                },
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                isPressed = true
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText,
            color = if (card.color == "YELLOW") Color.Black else Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Black
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(150)
            isPressed = false
        }
    }
}

private fun isCardPlayable(card: GameCard, currentColor: String, currentValue: String): Boolean {
    if (card.type == "WILD") return true
    if (card.color == currentColor) return true
    if (card.value == currentValue) return true
    return false
}