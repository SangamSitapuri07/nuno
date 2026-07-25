package com.nuno.app.screens.lobby

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.*

data class LobbyPlayerData(
    val userId: String,
    val username: String,
    val level: Int = 1,
    val isReady: Boolean = false,
    val isHost: Boolean = false,
    val ping: Int = 45
)

data class InvitableFriend(
    val userId: String,
    val username: String,
    val isOnline: Boolean
)

@Composable
fun RoomLobbyScreen(
    roomCode: String,
    players: List<LobbyPlayerData>,
    maxPlayers: Int,
    currentUserId: String?,
    countdown: Int,
    onlineFriends: List<InvitableFriend> = emptyList(),
    onBack: () -> Unit,
    onReady: () -> Unit,
    onStartGame: () -> Unit,
    onInviteFriend: (String) -> Unit,
    onKickPlayer: (String) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val currentPlayer = players.find { it.userId == currentUserId }
    val isHost = currentPlayer?.isHost == true
    val allReady = players.size >= 2 && players.all { it.isReady }

    var showInvitePicker by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Background)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // LEFT - Room Info + UNO Card
            Column(
                modifier = Modifier
                    .weight(0.55f)
                    .fillMaxHeight()
                    .padding(GameDimens.paddingLg)
            ) {
                // Back + Title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GameColors.TextWhite)
                    }
                    Text(
                        text = "ROOM LOBBY",
                        color = GameColors.TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Room Code Panel
                GamePanel(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = GameColors.Gold.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(GameDimens.paddingMd),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("ROOM CODE", color = GameColors.TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = roomCode,
                                color = GameColors.Gold,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 4.sp
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SmallIconButton(
                                icon = Icons.Default.ContentCopy,
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(roomCode))
                                }
                            )
                            SmallIconButton(
                                icon = Icons.Default.Share,
                                onClick = { }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Center UNO Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 120.dp, height = 180.dp)
                            .shadow(16.dp, RoundedCornerShape(12.dp), spotColor = GameColors.Purple)
                            .background(
                                brush = Brush.linearGradient(listOf(GameColors.Blue, GameColors.Purple)),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(3.dp, GameColors.Gold, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "NUNO",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GameButton(
                        text = "INVITE FRIEND",
                        onClick = { showInvitePicker = true },
                        style = ButtonStyle.OUTLINE,
                        modifier = Modifier.weight(1f)
                    )
                    GameButton(
                        text = if (countdown > 0) "STARTING ${countdown}s" else "START GAME",
                        onClick = if (isHost && allReady) onStartGame else onReady,
                        style = if (allReady) ButtonStyle.GREEN else ButtonStyle.GOLD,
                        modifier = Modifier.weight(1f),
                        enabled = true
                    )
                }
            }

            // RIGHT - Players List
            Column(
                modifier = Modifier
                    .weight(0.45f)
                    .fillMaxHeight()
                    .padding(GameDimens.paddingLg)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PLAYERS",
                        color = GameColors.TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "${players.size}/$maxPlayers",
                        color = GameColors.Gold,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(players) { player ->
                        PlayerListCard(
                            player = player,
                            isCurrentUser = player.userId == currentUserId,
                            canKick = isHost && player.userId != currentUserId,
                            onKick = { onKickPlayer(player.userId) }
                        )
                    }

                    // Empty slots
                    val emptySlots = maxPlayers - players.size
                    items(emptySlots) {
                        EmptyPlayerSlot()
                    }
                }
            }
        }

        if (showInvitePicker) {
            InviteFriendDialog(
                friends = onlineFriends,
                onInvite = { friendId ->
                    onInviteFriend(friendId)
                    showInvitePicker = false
                },
                onDismiss = { showInvitePicker = false }
            )
        }
    }
}

@Composable
private fun InviteFriendDialog(
    friends: List<InvitableFriend>,
    onInvite: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Invite a friend") },
        text = {
            val online = friends.filter { it.isOnline }
            if (online.isEmpty()) {
                Text("No friends online right now.", color = GameColors.TextGray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(online) { friend ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onInvite(friend.userId) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GameAvatar(username = friend.username, size = 32.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(friend.username, color = GameColors.TextWhite, fontSize = 14.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

@Composable
private fun PlayerListCard(
    player: LobbyPlayerData,
    isCurrentUser: Boolean,
    canKick: Boolean,
    onKick: () -> Unit
) {
    GamePanel(
        borderColor = when {
            isCurrentUser -> GameColors.Gold.copy(alpha = 0.7f)
            player.isReady -> GameColors.Green.copy(alpha = 0.5f)
            else -> GameColors.BorderPurple.copy(alpha = 0.3f)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(GameDimens.paddingMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GameAvatar(
                username = player.username,
                size = 40.dp,
                borderColor = if (player.isHost) GameColors.Gold else GameColors.Blue
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = player.username,
                        color = GameColors.TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (player.isHost) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "👑", fontSize = 12.sp)
                    }
                    if (isCurrentUser) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "(You)", color = GameColors.Gold, fontSize = 10.sp)
                    }
                }
                Text(
                    text = "Lv. ${player.level} • ${player.ping}ms",
                    color = GameColors.TextGray,
                    fontSize = 10.sp
                )
            }

            // Ready indicator
            Box(
                modifier = Modifier
                    .background(
                        if (player.isReady) GameColors.Green else GameColors.Red.copy(alpha = 0.5f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (player.isReady) "READY" else "NOT READY",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (canKick) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    Icons.Default.Close,
                    null,
                    tint = GameColors.Red,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onKick() }
                )
            }
        }
    }
}

@Composable
private fun EmptyPlayerSlot() {
    GamePanel(borderColor = GameColors.BorderPurple.copy(alpha = 0.15f)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(GameDimens.paddingMd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Waiting for player...",
                color = GameColors.TextDark,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun SmallIconButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .background(GameColors.Surface, CircleShape)
            .border(1.dp, GameColors.BorderPurple.copy(alpha = 0.3f), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = GameColors.Gold, modifier = Modifier.size(18.dp))
    }
}