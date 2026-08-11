package com.nuno.app.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OpponentFan(
    username: String,
    level: Int,
    trophies: Int,
    cardCount: Int,
    borderColor: Color,
    chatMessage: String? = null
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PlayerAvatar(username = username, level = level, borderColor = borderColor, size = 32.dp)
            Spacer(modifier = Modifier.width(4.dp))
            PlayerInfoBadge(username = username, trophies = trophies)
            if (!chatMessage.isNullOrEmpty()) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("$chatMessage 📶", color = Color.Black, fontSize = 10.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(horizontalArrangement = Arrangement.spacedBy((-14).dp)) {
            repeat(cardCount.coerceAtMost(7)) {
                UnoCardBack(size = CardSize.SMALL)
            }
        }
    }
}