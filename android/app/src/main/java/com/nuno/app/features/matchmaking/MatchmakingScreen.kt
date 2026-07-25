package com.nuno.app.features.matchmaking


import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.theme.*
import com.nuno.app.core.utils.Constants

@Composable
fun MatchmakingScreen(
    onBack: () -> Unit,
    onMatchFound: (matchId: String, roomId: String) -> Unit,
    onNavigateToProfile: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToFriends: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    viewModel: MatchmakingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()




    LaunchedEffect(uiState.status) {
        when (uiState.status) {
            QueueStatus.MATCH_FOUND -> {
                kotlinx.coroutines.delay(500)
            }
            QueueStatus.GAME_STARTING -> {
                onMatchFound(uiState.matchId ?: "", uiState.roomId ?: "")
            }
            else -> {}
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BackgroundDark, BackgroundDarker)
                )
            )
    ) {
        LeftSidebar(
            onHomeClick = onBack,
            onProfileClick = onNavigateToProfile,
            onShopClick = onNavigateToShop,
            onFriendsClick = onNavigateToFriends,
            onLeaderboardClick = onNavigateToLeaderboard,
            onSettingsClick = onNavigateToSettings
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(24.dp)
        ) {
            when (uiState.status) {
                QueueStatus.IDLE, QueueStatus.CONNECTING -> {
                    ModeSelectionCenter(
                        isLoading = uiState.status == QueueStatus.CONNECTING,
                        onModeSelected = { mode -> viewModel.joinQueue(mode) }
                    )
                }

                QueueStatus.SEARCHING -> {
                    SearchingCenter(
                        elapsedSeconds = uiState.elapsedSeconds,
                        onCancel = { viewModel.leaveQueue() }
                    )
                }

                QueueStatus.MATCH_FOUND, QueueStatus.GAME_STARTING -> {
                    MatchFoundCenter()
                }

                QueueStatus.ERROR -> {
                    ErrorCenter(
                        message = uiState.errorMessage ?: "Error",
                        onBack = onBack
                    )
                }
            }

        }

        RightInfoPanel()
    }
}

@Composable
private fun LeftSidebar(
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onShopClick: () -> Unit,
    onFriendsClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .fillMaxHeight()
            .background(SurfaceCard)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(PrimaryBlue, PrimaryPurple)
                        ),
                        shape = CircleShape
                    )
                    .border(2.dp, AccentCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "N", color = TextPrimary, fontWeight = FontWeight.Black, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = "Player", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(text = "Lv. 1", color = TextSecondary, fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { 0.3f },
            modifier = Modifier.fillMaxWidth().height(4.dp),
            color = AccentGold,
            trackColor = SurfaceDark
        )

        Spacer(modifier = Modifier.height(24.dp))

        SidebarItem(icon = Icons.Default.PlayArrow, label = "Play", isSelected = true) { }
        SidebarItem(icon = Icons.Default.Person, label = "Profile") { onProfileClick() }
        SidebarItem(icon = Icons.Default.ShoppingCart, label = "Shop") { onShopClick() }
        SidebarItem(icon = Icons.Default.Group, label = "Friends") { onFriendsClick() }
        SidebarItem(icon = Icons.Default.EmojiEvents, label = "Leaderboard") { onLeaderboardClick() }
        SidebarItem(icon = Icons.Default.Settings, label = "Settings") { onSettingsClick() }

        Spacer(modifier = Modifier.weight(1f))

        SidebarItem(icon = Icons.Default.Home, label = "Back") { onHomeClick() }
    }
}

@Composable
private fun SidebarItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) PrimaryPurple.copy(alpha = 0.3f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .then(
                if (!isSelected) Modifier.clickable { onClick() } else Modifier
            )
            .padding(vertical = 10.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (isSelected) AccentCyan else TextSecondary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            color = if (isSelected) TextPrimary else TextSecondary,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun ModeSelectionCenter(
    isLoading: Boolean,
    onModeSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.SportsEsports,
            contentDescription = null,
            tint = AccentCyan,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "SELECT GAME MODE",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            ModeCard(
                title = "CASUAL",
                subtitle = "Play for fun",
                onClick = { onModeSelected(Constants.MODE_CASUAL) },
                gradient = listOf(PrimaryBlue, AccentCyan),
                enabled = !isLoading
            )
            ModeCard(
                title = "RANKED",
                subtitle = "Competitive",
                onClick = { onModeSelected(Constants.MODE_RANKED) },
                gradient = listOf(PrimaryPurple, AccentPink),
                enabled = !isLoading
            )
        }

        if (isLoading) {
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator(color = AccentCyan)
        }
    }
}

@Composable
private fun ModeCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    gradient: List<Color>,
    enabled: Boolean
) {
    Card(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .width(180.dp)
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.linearGradient(colors = gradient)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = title, color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Black)
                Text(text = subtitle, color = TextPrimary.copy(alpha = 0.8f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun SearchingCenter(
    elapsedSeconds: Int,
    onCancel: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "search")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(3000, easing = LinearEasing)),
        label = "rotation"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(animation = tween(1500), repeatMode = RepeatMode.Reverse),
        label = "pulse"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "FINDING MATCH",
            color = TextPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 3.sp
        )
        Text(text = "Ranked Mode", color = TextSecondary, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier.size(240.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .border(2.dp, PrimaryPurple.copy(alpha = 0.3f), CircleShape)
                    .rotate(rotation)
            )
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .border(2.dp, PrimaryPurple.copy(alpha = 0.5f), CircleShape)
                    .rotate(-rotation)
            )
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .border(3.dp, AccentCyan, CircleShape)
                    .scale(pulse)
            )
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(
                        brush = Brush.radialGradient(colors = listOf(PrimaryPurple, DeepPurple)),
                        shape = CircleShape
                    )
                    .scale(pulse),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = AccentGold,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = formatTime(elapsedSeconds),
            color = TextPrimary,
            fontSize = 36.sp,
            fontWeight = FontWeight.Black
        )
        Text(text = "Searching for opponent...", color = TextSecondary, fontSize = 12.sp)
        Text(text = "Estimated time: 00:15", color = TextTertiary, fontSize = 11.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onCancel,
            modifier = Modifier
                .width(240.dp)
                .height(44.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DangerRed)
        ) {
            Text(text = "CANCEL", fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 2.sp)
        }
    }
}

@Composable
private fun MatchFoundCenter() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🎉", fontSize = 80.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "MATCH FOUND!",
            color = SuccessGreen,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 3.sp
        )
    }
}

@Composable
private fun ErrorCenter(message: String, onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "⚠️", fontSize = 60.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = message, color = DangerRed, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("GO BACK")
        }
    }
}

@Composable
private fun RightInfoPanel() {
    Column(
        modifier = Modifier
            .width(240.dp)
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InfoCard(title = "GAME MODE") {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = AccentGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Casual", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }

        InfoCard(title = "YOUR RANK") {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(RankBronze, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = TextPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = "Bronze III", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(text = "0/500", color = TextSecondary, fontSize = 10.sp)
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { 0.0f },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = AccentCyan,
                    trackColor = SurfaceDark
                )
            }
        }

        InfoCard(title = "SEASON ENDS IN") {
            Text(text = "30d 12h 45m", color = AccentGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun InfoCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, BorderPurple.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                color = TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(minutes, secs)
}