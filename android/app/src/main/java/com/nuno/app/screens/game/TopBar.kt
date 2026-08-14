package com.nuno.app.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors

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
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left cluster
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .shadow(8.dp, RoundedCornerShape(12.dp), spotColor = Color(0xFFE50914).copy(0.4f))
                    .background(Color(0xFFE50914), RoundedCornerShape(12.dp))
                    .border(1.5.dp, Color(0xFFFFC71F), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("NUNO", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            }

            Box(
                modifier = Modifier
                    .background(Color(0xFF0E1130).copy(0.85f), RoundedCornerShape(10.dp))
                    .border(1.dp, Color.White.copy(0.08f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Column {
                    Text("Room: $roomCode", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text("Mode: $gameMode", color = GameColors.Gold, fontSize = 8.sp, fontWeight = FontWeight.Medium)
                }
            }

            Box(
                modifier = Modifier
                    .background(Color(0xFF00E676).copy(0.12f), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF00E676).copy(0.25f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("📶 ${pingMs}ms", color = GameColors.Green, fontSize = 9.sp, fontWeight = FontWeight.Black)
            }
        }

        // Right cluster
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PremiumGameIconButton(icon = Icons.Default.Chat, isActive = false, onClick = onChat)
            PremiumGameIconButton(icon = if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic, isActive = !isMicMuted, activeColor = GameColors.Green, onClick = onVoice)
            PremiumGameIconButton(icon = if (isSpeakerMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp, isActive = !isSpeakerMuted, activeColor = GameColors.Cyan, onClick = onSpeaker)
            PremiumGameIconButton(icon = Icons.Default.Menu, isActive = false, onClick = onMenu)

            Box(
                modifier = Modifier
                    .shadow(8.dp, RoundedCornerShape(12.dp), spotColor = Color(0xFFE50914).copy(0.5f))
                    .background(Brush.linearGradient(listOf(Color(0xFFFF3B5C), Color(0xFFD5002B))), RoundedCornerShape(12.dp))
                    .border(1.dp, Color.White.copy(0.2f), RoundedCornerShape(12.dp))
                    .clickable { onLeaveRoom() }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text("LEAVE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 0.8.sp)
            }
        }
    }
}

@Composable
private fun PremiumGameIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    activeColor: Color = GameColors.Cyan,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .shadow(if (isActive) 8.dp else 0.dp, CircleShape, spotColor = activeColor.copy(0.5f))
            .background(
                if (isActive) Brush.linearGradient(listOf(activeColor, activeColor.copy(0.7f)))
                else Brush.linearGradient(listOf(Color(0xFF0E1130).copy(0.85f), Color(0xFF0E1130).copy(0.9f))),
                CircleShape
            )
            .border(1.dp, if (isActive) Color.White.copy(0.3f) else Color.White.copy(0.08f), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = Color.White, modifier = Modifier.size(16.dp))
    }
}
