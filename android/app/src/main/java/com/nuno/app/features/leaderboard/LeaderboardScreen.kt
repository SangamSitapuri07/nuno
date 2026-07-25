package com.nuno.app.features.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.common.UiState
import com.nuno.app.core.theme.*
import com.nuno.app.features.home.LeftSidebar
import com.nuno.app.features.leaderboard.components.LeaderboardRow
import com.nuno.app.features.leaderboard.components.TopThreeSection

@Composable
fun LeaderboardScreen(
    onBack: () -> Unit,
    onNavigateToPlay: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToFriends: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val globalState by viewModel.globalState.collectAsState()
    val friendsState by viewModel.friendsState.collectAsState()
    var selectedTab by remember { mutableStateOf(LeaderboardTab.GLOBAL) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(BackgroundDark, BackgroundDarker)))
    ) {
        LeftSidebar(
            username = "Player", level = 1, coins = 0,
            selectedRoute = "leaderboard",
            onPlayClick = onNavigateToPlay,
            onProfileClick = onNavigateToProfile,
            onFriendsClick = onNavigateToFriends,
            onStoreClick = onNavigateToShop,
            onSettingsClick = onNavigateToSettings,
            onLogout = onLogout
        )

        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            // Fixed header
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.EmojiEvents, null, tint = AccentGold, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LEADERBOARD",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 3.sp
                    )
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "Season 1 • Ends in 30d",
                        color = AccentGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            // Tabs
            LazyColumn(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        LeaderboardTab.entries.forEach { tab ->
                            TabChip(
                                label = tab.label,
                                isSelected = selectedTab == tab,
                                onClick = { selectedTab = tab },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                val currentState = if (selectedTab == LeaderboardTab.FRIENDS) friendsState else globalState

                when (currentState) {
                    is UiState.Loading -> item {
                        Box(modifier = Modifier.fillMaxWidth().padding(40.dp)) {
                            CircularProgressIndicator(color = AccentCyan, modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    is UiState.Success -> {
                        if (currentState.data.isEmpty()) {
                            item {
                                Text("No entries yet", color = TextSecondary, modifier = Modifier.padding(16.dp))
                            }
                        } else {
                            item {
                                TopThreeSection(entries = currentState.data.take(3))
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            items(currentState.data.drop(3)) { entry ->
                                LeaderboardRow(entry = entry)
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                    }
                    is UiState.Error -> item {
                        Text(currentState.message, color = DangerRed, modifier = Modifier.padding(16.dp))
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun TabChip(label: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PrimaryPurple else SurfaceCard
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) TextPrimary else TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}