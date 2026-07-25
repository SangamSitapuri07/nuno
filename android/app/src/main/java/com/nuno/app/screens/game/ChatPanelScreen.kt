package com.nuno.app.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.GameAvatar
import com.nuno.app.core.designsystem.components.GamePanel

data class ChatMessageData(
    val username: String,
    val message: String,
    val timestamp: String = "now"
)

@Composable
fun ChatPanel(
    messages: List<ChatMessageData>,
    onSend: (String) -> Unit,
    onClose: () -> Unit
) {
    var message by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }

    GamePanel(
        modifier = Modifier
            .width(320.dp)
            .fillMaxHeight()
            .padding(GameDimens.paddingSm),
        borderColor = GameColors.BorderBlue
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(GameDimens.paddingMd)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("CHAT", color = GameColors.TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Black)
                IconButton(onClick = onClose, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Close, null, tint = GameColors.TextGray)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChatTab("Room", selectedTab == 0) { selectedTab = 0 }
                ChatTab("Team", selectedTab == 1) { selectedTab = 1 }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(msg)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = { Text("Type a message...", color = GameColors.TextDark, fontSize = 12.sp) },
                    modifier = Modifier.weight(1f).height(44.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(GameDimens.radiusFull),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = GameColors.TextWhite,
                        unfocusedTextColor = GameColors.TextWhite,
                        focusedBorderColor = GameColors.Blue,
                        unfocusedBorderColor = GameColors.BorderPurple
                    ),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(
                    onClick = {
                        if (message.isNotBlank()) {
                            onSend(message)
                            message = ""
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(GameColors.Blue, androidx.compose.foundation.shape.CircleShape)
                ) {
                    Icon(Icons.Default.Send, null, tint = GameColors.TextWhite, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(msg: ChatMessageData) {
    Row(verticalAlignment = Alignment.Top) {
        GameAvatar(username = msg.username, size = 28.dp, borderColor = GameColors.Blue)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = msg.username, color = GameColors.Cyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(text = msg.message, color = GameColors.TextWhite, fontSize = 12.sp)
        }
    }
}

@Composable
private fun ChatTab(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) GameColors.Blue else GameColors.Surface,
                RoundedCornerShape(GameDimens.radiusFull)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) GameColors.TextWhite else GameColors.TextGray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}