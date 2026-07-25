package com.nuno.app.features.gameplay.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.gameplay.GameCard
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun DrawPile(drawCount: Int, isMyTurn: Boolean, onDrawClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 145.dp)
                .background(
                    brush = Brush.linearGradient(colors = listOf(PrimaryBlue, PrimaryPurple)),
                    shape = RoundedCornerShape(10.dp)
                )
                .border(2.dp, AccentGold, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "NUNO", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Black)
        }

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = onDrawClick,
            enabled = isMyTurn,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isMyTurn) PrimaryBlue else NeutralGray700
            ),
            modifier = Modifier.height(28.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
            shape = RoundedCornerShape(6.dp)
        ) {
            Icon(Icons.Default.Add, null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("DRAW", fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }

        Text(text = "$drawCount left", color = TextTertiary, fontSize = 9.sp)
    }
}

@Composable
fun DiscardPile(topCard: GameCard?, currentColor: String) {
    val shake = remember { Animatable(0f) }

    LaunchedEffect(topCard?.cardId) {
        if (topCard != null) {
            shake.snapTo(0f)
            shake.animateTo(
                targetValue = 1f,
                animationSpec = keyframes {
                    durationMillis = 400
                    0f at 0
                    -8f at 60
                    8f at 120
                    -6f at 180
                    6f at 240
                    -3f at 300
                    0f at 400
                }
            )
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (topCard != null) {
            val cardColor = when (topCard.color) {
                "RED" -> CardRed
                "BLUE" -> CardBlue
                "GREEN" -> CardGreen
                "YELLOW" -> CardYellow
                else -> CardWild
            }

            val displayText = when (topCard.value) {
                "SKIP" -> "⊘"
                "REVERSE" -> "⇄"
                "DRAW_TWO" -> "+2"
                "WILD" -> "★"
                "WILD_DRAW_FOUR" -> "+4"
                else -> topCard.value
            }

            Box(
                modifier = Modifier
                    .size(width = 100.dp, height = 145.dp)
                    .graphicsLayer {
                        translationX = shake.value
                        rotationZ = shake.value * 0.5f
                    }
                    .background(cardColor, RoundedCornerShape(10.dp))
                    .border(2.dp, TextPrimary.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayText,
                    color = if (topCard.color == "YELLOW") Color.Black else Color.White,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Color: $currentColor",
            color = when (currentColor) {
                "RED" -> CardRed
                "BLUE" -> CardBlue
                "GREEN" -> CardGreen
                "YELLOW" -> CardYellow
                else -> TextPrimary
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}