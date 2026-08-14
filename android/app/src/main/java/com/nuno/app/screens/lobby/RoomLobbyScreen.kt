package com.nuno.app.screens.lobby

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.components.GameAvatar

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
    val clipboard = LocalClipboardManager.current
    var showInvite by remember { mutableStateOf(false) }
    val currentPlayer = players.find { it.userId == currentUserId }
    val isHost = currentPlayer?.isHost == true

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0A0D1E))) {
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding().padding(16.dp)) {
            // Header - Room Name & Code like reference 5
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF1B1F3D), RoundedCornerShape(10.dp))
                        .border(1.dp, Color(0xFF2C3159), RoundedCornerShape(10.dp))
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("ROOM LOBBY", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("ROOM CODE: ", color = Color(0xFF5A607F), fontSize = 11.sp)
                        Text(roomCode.ifEmpty { "AB12C3" }, color = GameColors.Gold, fontSize = 14.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ContentCopy, null, tint = GameColors.Gold, modifier = Modifier.size(16.dp).clickable { clipboard.setText(AnnotatedString(roomCode)) })
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Text("${players.size}/$maxPlayers", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Left - Players list like reference
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFF12152E), RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(players) { player ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (player.userId == currentUserId) GameColors.Gold.copy(0.1f) else Color(0xFF1B1F3D),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .border(1.dp, if (player.isHost) GameColors.Gold.copy(0.5f) else Color(0xFF2C3159).copy(0.5f), RoundedCornerShape(12.dp))
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                GameAvatar(username = player.username, size = 36.dp, borderColor = if (player.isHost) GameColors.Gold else GameColors.Blue)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(player.username, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        if (player.isHost) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("👑", fontSize = 10.sp)
                                        }
                                        if (player.userId == currentUserId) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("(You)", color = GameColors.Gold, fontSize = 9.sp)
                                        }
                                    }
                                    Text(if (player.isReady) "Ready" else "Not Ready", color = if (player.isReady) GameColors.Green else Color(0xFF5A607F), fontSize = 10.sp)
                                }
                                if (isHost && player.userId != currentUserId) {
                                    Icon(Icons.Default.Close, null, tint = Color(0xFFFF5A5A), modifier = Modifier.size(18.dp).clickable { onKickPlayer(player.userId) })
                                }
                            }
                        }
                        val emptySlots = maxPlayers - players.size
                        items(emptySlots) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF0E1130).copy(0.5f), RoundedCornerShape(12.dp))
                                    .border(1.dp, Color.White.copy(0.05f), RoundedCornerShape(12.dp))
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Waiting for player...", color = Color(0xFF5A607F), fontSize = 11.sp)
                            }
                        }
                    }
                }

                // Right - UNO card center like reference
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFF12152E), RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(width = 100.dp, height = 140.dp)
                                .background(Color(0xFFE53935), RoundedCornerShape(12.dp))
                                .border(2.dp, Color.White, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("UNO", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Room: $roomCode", color = Color(0xFF5A607F), fontSize = 11.sp)
                        if (countdown > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Starting in $countdown...", color = GameColors.Green, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { showInvite = true },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3159))
                ) {
                    Text("INVITE FRIEND", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = if (isHost) onStartGame else onReady,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GameColors.Gold)
                ) {
                    Text(if (isHost) "START GAME" else if (currentPlayer?.isReady == true) "READY ✓" else "READY", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Black)
                }
            }
        }

        if (showInvite) {
            AlertDialog(
                onDismissRequest = { showInvite = false },
                containerColor = Color(0xFF12152E),
                title = { Text("Invite Friends", color = Color.White, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        if (onlineFriends.filter { it.isOnline }.isEmpty()) {
                            Text("No friends online", color = Color(0xFF5A607F), fontSize = 12.sp)
                        } else {
                            onlineFriends.filter { it.isOnline }.forEach { friend ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onInviteFriend(friend.userId); showInvite = false }
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    GameAvatar(username = friend.username, size = 32.dp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(friend.username, color = Color.White, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                    Text("INVITE", color = GameColors.Green, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                },
                confirmButton = { TextButton(onClick = { showInvite = false }) { Text("Close", color = Color.White) } }
            )
        }
    }
}
