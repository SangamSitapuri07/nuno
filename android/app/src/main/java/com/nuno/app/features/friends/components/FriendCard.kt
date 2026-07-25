package com.nuno.app.features.friends.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.friends.Friend

@Composable
fun FriendCard(
    friend: Friend,
    onInvite: () -> Unit,
    onChat: () -> Unit,
    onMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusColor = when (friend.status) {
        "ONLINE" -> SuccessGreen
        "IN_MATCH" -> AccentGold
        "IN_LOBBY" -> AccentCyan
        "IN_VOICE" -> PrimaryPurple
        "DO_NOT_DISTURB" -> DangerRed
        else -> NeutralGray500
    }

    val infiniteTransition = rememberInfiniteTransition(label = "friendCard")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = "glow"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (friend.status != "OFFLINE") {
                    Modifier.shadow(
                        8.dp,
                        RoundedCornerShape(12.dp),
                        spotColor = statusColor.copy(alpha = glowAlpha)
                    )
                } else Modifier
            ),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            brush = Brush.radialGradient(colors = listOf(PrimaryBlue, PrimaryPurple)),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = friend.username.firstOrNull()?.uppercase() ?: "?",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(statusColor, CircleShape)
                        .align(Alignment.BottomEnd)
                )
                if (friend.isInVoice) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .background(AccentCyan, CircleShape)
                            .align(Alignment.TopEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Mic,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = friend.username,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "•", color = AccentGold, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = friend.currentRank,
                        color = AccentGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = statusLabel(friend.status),
                    color = statusColor,
                    fontSize = 11.sp
                )
                if (friend.currentGame != null) {
                    Text(
                        text = "Playing: ${friend.currentGame}",
                        color = TextTertiary,
                        fontSize = 10.sp
                    )
                }
            }

            if (friend.status == "IN_LOBBY" || friend.status == "ONLINE") {
                IconButton(
                    onClick = onInvite,
                    modifier = Modifier
                        .size(36.dp)
                        .background(SuccessGreen, CircleShape)
                ) {
                    Icon(
                        Icons.Default.SportsEsports,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            }

            IconButton(
                onClick = onChat,
                modifier = Modifier
                    .size(36.dp)
                    .background(SurfaceDark, CircleShape)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Chat,
                    null,
                    tint = AccentCyan,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(
                onClick = onMore,
                modifier = Modifier
                    .size(36.dp)
                    .background(SurfaceDark, CircleShape)
            ) {
                Icon(
                    Icons.Default.MoreVert,
                    null,
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

private fun statusLabel(status: String): String = when (status) {
    "ONLINE" -> "Online"
    "IN_MATCH" -> "In Match"
    "IN_LOBBY" -> "In Lobby"
    "IN_VOICE" -> "In Voice"
    "DO_NOT_DISTURB" -> "Do Not Disturb"
    else -> "Offline"
}