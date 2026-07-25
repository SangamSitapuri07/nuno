package com.nuno.app.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.GamePanel
import androidx.compose.ui.graphics.Color

@Composable
fun QuickChatPanel(
    onSendQuickChat: (String) -> Unit,
    onSendEmote: (String) -> Unit,
    onClose: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val quickMessages = listOf("Hello!", "Good luck!", "Well played!", "Thanks!", "Oops!", "Nice move!")
    val emotes = listOf("😀", "😂", "😎", "😢", "😡", "👍", "👎", "❤️", "🎉", "🔥", "💀", "🤔")

    GamePanel(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight()
            .padding(GameDimens.paddingSm),
        borderColor = GameColors.Green
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(GameDimens.paddingMd)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("QUICK CHAT / EMOTES", color = GameColors.TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Black)
                IconButton(onClick = onClose, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Close, null, tint = GameColors.TextGray)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tabs
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TabButton("Messages", selectedTab == 0) { selectedTab = 0 }
                TabButton("Emotes", selectedTab == 1) { selectedTab = 1 }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (selectedTab == 0) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(quickMessages) { msg ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(GameColors.Surface, RoundedCornerShape(GameDimens.radiusSm))
                                .clickable { onSendQuickChat(msg) }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = msg,
                                color = GameColors.TextWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(emotes) { emote ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .background(GameColors.Surface, RoundedCornerShape(GameDimens.radiusSm))
                                .clickable { onSendEmote(emote) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emote, fontSize = 28.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) GameColors.Green else GameColors.Surface,
                RoundedCornerShape(GameDimens.radiusFull)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.Black else GameColors.TextGray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

