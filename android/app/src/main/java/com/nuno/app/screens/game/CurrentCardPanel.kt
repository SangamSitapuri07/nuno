package com.nuno.app.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors

@Composable
fun CurrentCardPanel(
    topCard: GameCardData?,
    currentColor: String,
    modifier: Modifier = Modifier
) {
    val cardColor = when (currentColor) {
        "RED" -> GameColors.CardRed
        "BLUE" -> GameColors.CardBlue
        "GREEN" -> GameColors.CardGreen
        "YELLOW" -> GameColors.CardYellow
        else -> GameColors.TextWhite
    }

    Box(
        modifier = modifier
            .background(Color(0xFF0F142A).copy(alpha = 0.85f), RoundedCornerShape(8.dp))
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Current Color", color = Color(0xFFAAAAAA), fontSize = 8.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(cardColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(currentColor, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}