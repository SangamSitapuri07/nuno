package com.nuno.app.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(
    roomCode: String,
    gameMode: String = "Classic",
    pingMs: Int = 52,
    isMicMuted: Boolean = true,
    isSpeakerMuted: Boolean = true,
    onChat: () -> Unit,
    onVoice: () -> Unit,
    onSpeaker: () -> Unit = {},
    onMenu: () -> Unit,
    onLeaveRoom: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp)
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(width = 52.dp, height = 26.dp)
                    .background(Color(0xFFE50914), RoundedCornerShape(13.dp))
                    .border(1.5.dp, Color(0xFFFBC02D), RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("NUNO", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black)
            }

            Spacer(modifier = Modifier.width(6.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFF0F142A).copy(alpha = 0.85f), RoundedCornerShape(8.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Column {
                    Text("Room Code: $roomCode", color = Color.White, fontSize = 8.5.sp, fontWeight = FontWeight.Bold)
                    Text("Mode: $gameMode", color = Color(0xFFFFC107), fontSize = 7.5.sp)
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFF0F142A).copy(alpha = 0.85f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
                Text("📶 ${pingMs}ms", color = Color(0xFF4CAF50), fontSize = 8.5.sp, fontWeight = FontWeight.Bold)
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            IconButton(
                onClick = onChat,
                modifier = Modifier
                    .size(28.dp)
                    .background(Color(0xFF0F142A).copy(alpha = 0.85f), CircleShape)
            ) {
                Icon(Icons.Default.Chat, null, tint = Color.White, modifier = Modifier.size(14.dp))
            }

            IconButton(
                onClick = onVoice,
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        if (isMicMuted) Color(0xFFE50914) else Color(0xFF0F142A).copy(alpha = 0.85f),
                        CircleShape
                    )
            ) {
                Icon(
                    if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }

            IconButton(
                onClick = onSpeaker,
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        if (isSpeakerMuted) Color(0xFFE50914) else Color(0xFF0F142A).copy(alpha = 0.85f),
                        CircleShape
                    )
            ) {
                Icon(
                    if (isSpeakerMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                    null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }

            IconButton(
                onClick = onMenu,
                modifier = Modifier
                    .size(28.dp)
                    .background(Color(0xFF0F142A).copy(alpha = 0.85f), CircleShape)
            ) {
                Icon(Icons.Default.Menu, null, tint = Color.White, modifier = Modifier.size(14.dp))
            }

            Button(
                onClick = onLeaveRoom,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                modifier = Modifier.height(26.dp)
            ) {
                Text("LEAVE ROOM", color = Color.White, fontSize = 9.5.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}