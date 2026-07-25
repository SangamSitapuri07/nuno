package com.nuno.app.features.lobby

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.theme.*
import com.nuno.app.core.utils.showToast

@Composable
fun LobbyScreen(
    onBack: () -> Unit,
    onGameStart: (matchId: String) -> Unit,
    viewModel: LobbyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var isMuted by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.gameStarted) {
        if (uiState.gameStarted && uiState.matchId != null) {
            onGameStart(uiState.matchId!!)
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            context.showToast(it)
            viewModel.clearError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(BackgroundDark, BackgroundDarker)))
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(color = AccentCyan, modifier = Modifier.align(Alignment.Center))
            }

            uiState.room != null -> {
                LobbyContent(
                    room = uiState.room!!,
                    countdown = uiState.countdown,
                    currentUserId = uiState.currentUserId,
                    isMuted = isMuted,
                    onToggleReady = { viewModel.toggleReady() },
                    onLeave = {
                        viewModel.leaveRoom()
                        onBack()
                    },
                    onToggleMute = { isMuted = !isMuted },
                    onKickPlayer = { viewModel.kickPlayer(it) }
                )
            }

            else -> {
                Text(text = "No room data", color = TextSecondary, modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun LobbyContent(
    room: LobbyRoom,
    countdown: Int,
    currentUserId: String?,
    isMuted: Boolean,
    onToggleReady: () -> Unit,
    onLeave: () -> Unit,
    onToggleMute: () -> Unit,
    onKickPlayer: (String) -> Unit
) {
    val currentPlayer = room.players.find { it.userId == currentUserId }
    val isHost = currentPlayer?.isHost == true

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            roomCode = room.roomCode,
            onLeave = onLeave,
            isMuted = isMuted,
            onToggleMute = onToggleMute
        )

        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight().padding(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    when {
                        room.players.size <= 2 -> TwoPlayersLayout(players = room.players, gameMode = room.gameMode)
                        room.players.size <= 4 -> GridPlayersLayout(players = room.players, columns = 2, gameMode = room.gameMode)
                        room.players.size <= 6 -> GridPlayersLayout(players = room.players, columns = 3, gameMode = room.gameMode)
                        else -> GridPlayersLayout(players = room.players, columns = 4, gameMode = room.gameMode)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                BottomPanels(
                    gameMode = room.gameMode,
                    currentPlayer = currentPlayer,
                    countdown = countdown,
                    onToggleReady = onToggleReady
                )
            }

            PlayersListSidebar(
                players = room.players,
                maxPlayers = room.maxPlayers,
                currentUserId = currentUserId,
                isHost = isHost,
                onKick = onKickPlayer
            )
        }
    }
}

@Composable
private fun TwoPlayersLayout(players: List<LobbyPlayer>, gameMode: String) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerBattleCard(player = players.getOrNull(0), isLeft = true, modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(120.dp)
        ) {
            Text(text = "VS", color = AccentGold, fontSize = 56.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, BorderPurple.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "GAME MODE", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = gameMode, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        PlayerBattleCard(player = players.getOrNull(1), isLeft = false, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun GridPlayersLayout(players: List<LobbyPlayer>, columns: Int, gameMode: String) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(players) { player ->
            CompactPlayerCard(player = player)
        }
    }
}

@Composable
private fun CompactPlayerCard(player: LobbyPlayer) {
    val borderColor = if (player.isReady) SuccessGreen else BorderPurple

    Card(
        modifier = Modifier.fillMaxWidth().height(140.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(2.dp, borderColor.copy(alpha = 0.7f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(borderColor.copy(alpha = 0.2f), BackgroundDark))),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            brush = Brush.radialGradient(colors = listOf(borderColor.copy(alpha = 0.7f), PrimaryDark)),
                            shape = CircleShape
                        )
                        .border(2.dp, borderColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = player.username.firstOrNull()?.uppercase() ?: "?",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = player.username, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    if (player.isHost) Text(text = " 👑", fontSize = 10.sp)
                }

                Text(text = "Lv. 1 • Bronze III", color = TextSecondary, fontSize = 9.sp)

                if (player.isReady) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.background(SuccessGreen, RoundedCornerShape(12.dp)).padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Check, null, tint = TextPrimary, modifier = Modifier.size(10.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "READY", color = TextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text(text = "Not Ready", color = TextTertiary, fontSize = 9.sp)
                }
            }
        }
    }
}

@Composable
private fun BottomPanels(
    gameMode: String,
    currentPlayer: LobbyPlayer?,
    countdown: Int,
    onToggleReady: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.weight(1f).height(90.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, BorderPurple.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "MODE", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(text = gameMode, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Card(
            modifier = Modifier.weight(1.5f).height(90.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, BorderPurple.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "RULES", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(text = "• 7 cards each\n• 20s per turn\n• First to empty hand wins", color = TextPrimary, fontSize = 9.sp)
            }
        }

        // READY UP button using Box + clickable for reliability
        val isReady = currentPlayer?.isReady == true
        Box(
            modifier = Modifier
                .weight(1f)
                .height(90.dp)
                .background(
                    color = if (isReady) SuccessGreen else AccentGold,
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable {
                    android.util.Log.d("LobbyScreen", "READY UP button clicked")
                    onToggleReady()
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isReady) "✓ READY" else "READY UP",
                    color = BackgroundDark,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
                if (countdown > 0) {
                    Text(text = "Starts in ${countdown}s", color = BackgroundDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                } else if (isReady) {
                    Text(text = "Waiting for others...", color = BackgroundDark, fontSize = 9.sp)
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    roomCode: String,
    onLeave: () -> Unit,
    isMuted: Boolean,
    onToggleMute: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().background(SurfaceCard).padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onLeave) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextPrimary)
            }
            Column {
                Text(text = "LOBBY", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Black)
                Text(text = "Room ID: $roomCode", color = TextSecondary, fontSize = 10.sp)
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(
                onClick = { },
                modifier = Modifier.size(40.dp).background(SurfaceDark, CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.Chat, null, tint = AccentCyan)
            }

            IconButton(
                onClick = onToggleMute,
                modifier = Modifier.size(40.dp).background(if (isMuted) DangerRed else SurfaceDark, CircleShape)
            ) {
                Icon(
                    if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    null,
                    tint = if (isMuted) TextPrimary else AccentCyan
                )
            }

            IconButton(
                onClick = { },
                modifier = Modifier.size(40.dp).background(SurfaceDark, CircleShape)
            ) {
                Icon(Icons.Default.VolumeUp, null, tint = AccentCyan)
            }

            Spacer(modifier = Modifier.width(4.dp))

            Button(
                onClick = onLeave,
                colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(40.dp)
            ) {
                Text(text = "LEAVE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PlayerBattleCard(player: LobbyPlayer?, isLeft: Boolean, modifier: Modifier = Modifier) {
    val borderColor = if (isLeft) AccentCyan else DangerRed
    val gradientColors = if (isLeft) listOf(PrimaryBlue.copy(alpha = 0.3f), BackgroundDark)
    else listOf(DangerRed.copy(alpha = 0.3f), BackgroundDark)

    Card(
        modifier = modifier.height(280.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, borderColor.copy(alpha = if (player != null) 0.8f else 0.2f))
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(gradientColors)),
            contentAlignment = Alignment.Center
        ) {
            if (player == null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "?", color = TextTertiary, fontSize = 80.sp, fontWeight = FontWeight.Black)
                    Text(text = "Waiting for player...", color = TextTertiary, fontSize = 12.sp)
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(
                                brush = Brush.radialGradient(colors = listOf(borderColor.copy(alpha = 0.6f), PrimaryDark)),
                                shape = CircleShape
                            )
                            .border(3.dp, borderColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = player.username.firstOrNull()?.uppercase() ?: "?", color = TextPrimary, fontSize = 40.sp, fontWeight = FontWeight.Black)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = player.username, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        if (player.isHost) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "👑", fontSize = 14.sp)
                        }
                    }
                    Text(text = "Lv. 1", color = TextSecondary, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = RankBronze, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Bronze III", color = TextSecondary, fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(if (player.isReady) SuccessGreen else NeutralGray700, RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        if (player.isReady) {
                            Icon(Icons.Default.Check, null, tint = TextPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(text = if (player.isReady) "READY" else "NOT READY", color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayersListSidebar(
    players: List<LobbyPlayer>,
    maxPlayers: Int,
    currentUserId: String?,
    isHost: Boolean,
    onKick: (String) -> Unit
) {
    Card(
        modifier = Modifier.width(200.dp).fillMaxHeight().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderPurple.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "PLAYERS", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Black)
                Text(text = "${players.size}/$maxPlayers", color = AccentCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            HorizontalDivider(color = BorderPurple.copy(alpha = 0.3f))
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(players) { player ->
                    PlayerListItem(
                        player = player,
                        isCurrentUser = player.userId == currentUserId,
                        canKick = isHost && player.userId != currentUserId,
                        onKick = { onKick(player.userId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerListItem(
    player: LobbyPlayer,
    isCurrentUser: Boolean,
    canKick: Boolean,
    onKick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isCurrentUser) SurfaceLight else Color.Transparent, RoundedCornerShape(6.dp))
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(28.dp).background(PrimaryBlue, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = player.username.firstOrNull()?.uppercase() ?: "?", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(6.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = player.username, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                if (player.isHost) Text(text = " 👑", fontSize = 10.sp)
            }
            Text(
                text = if (player.isReady) "Ready" else "Not Ready",
                color = if (player.isReady) SuccessGreen else TextTertiary,
                fontSize = 9.sp
            )
        }
        if (canKick) {
            TextButton(onClick = onKick, contentPadding = PaddingValues(4.dp)) {
                Text("×", color = DangerRed, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}