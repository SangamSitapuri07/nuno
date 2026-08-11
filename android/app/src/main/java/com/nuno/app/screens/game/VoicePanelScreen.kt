package com.nuno.app.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.GameAvatar
import com.nuno.app.core.designsystem.components.GameButton
import com.nuno.app.core.designsystem.components.GamePanel
import com.nuno.app.core.designsystem.components.ButtonStyle

data class VoiceParticipant(
    val username: String,
    val isSpeaking: Boolean = false,
    val isMuted: Boolean = false,
    val isYou: Boolean = false
)

@Composable
fun VoicePanel(
    participants: List<VoiceParticipant>,
    isMuted: Boolean,
    onToggleMute: () -> Unit,
    onLeave: () -> Unit,
    onClose: () -> Unit
) {
    GamePanel(
        modifier = Modifier
            .width(280.dp)
            .padding(GameDimens.paddingSm),
        borderColor = GameColors.Cyan
    ) {
        Column(modifier = Modifier.padding(GameDimens.paddingLg)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("VOICE CHAT", color = GameColors.TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Black)
                IconButton(onClick = onClose, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Close, null, tint = GameColors.TextGray)
                }
            }

            Text(
                text = "${participants.count { it.isSpeaking }} Speaking",
                color = GameColors.Green,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Participants
            participants.forEach { participant ->
                VoiceParticipantRow(participant = participant)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            if (isMuted) GameColors.Red else GameColors.Surface,
                            CircleShape
                        )
                        .clickable { onToggleMute() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                GameButton(
                    text = "Leave",
                    onClick = onLeave,
                    style = ButtonStyle.DANGER,
                    height = 44.dp,
                    modifier = Modifier.width(100.dp)
                )
            }
        }
    }
}

@Composable
private fun VoiceParticipantRow(participant: VoiceParticipant) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GameAvatar(
            username = participant.username,
            size = 32.dp,
            borderColor = when {
                participant.isSpeaking -> GameColors.Green
                participant.isMuted -> GameColors.Red
                else -> GameColors.Blue
            }
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = participant.username + if (participant.isYou) " (You)" else "",
            color = GameColors.TextWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        // Audio level indicator (#15 in reference)
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            repeat(4) { i ->
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height((8 + i * 4).dp)
                        .background(
                            when {
                                participant.isMuted -> GameColors.Red
                                participant.isSpeaking && i < 3 -> GameColors.Green
                                else -> GameColors.TextDark
                            },
                            RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}