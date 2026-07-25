package com.nuno.app.features.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Notifications
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
import com.nuno.app.features.notifications.components.NotificationCard

@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    onNavigateToPlay: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToFriends: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: NotificationsViewModel = hiltViewModel()
) {
    val notificationsState by viewModel.notificationsState.collectAsState()
    var selectedTab by remember { mutableStateOf(NotificationTab.ALL) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(BackgroundDark, BackgroundDarker)))
    ) {
        LeftSidebar(
            username = "Player", level = 1, coins = 0,
            selectedRoute = "notifications",
            onPlayClick = onNavigateToPlay,
            onProfileClick = onNavigateToProfile,
            onFriendsClick = onNavigateToFriends,
            onLeaderboardClick = onNavigateToLeaderboard,
            onStoreClick = onNavigateToShop,
            onSettingsClick = onNavigateToSettings,
            onLogout = onLogout
        )

        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "NOTIFICATIONS",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                )

                Button(
                    onClick = { viewModel.markAllAsRead() },
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(Icons.Default.DoneAll, null, tint = AccentCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Mark All Read", color = AccentCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    NotificationTab.entries.forEach { tab ->
                        TabChip(
                            tab = tab,
                            isSelected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.fillMaxSize()) {
                    when (val state = notificationsState) {
                        is UiState.Loading -> CircularProgressIndicator(
                            color = AccentCyan,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        is UiState.Success -> {
                            val filtered = if (selectedTab == NotificationTab.ALL) state.data
                            else state.data.filter { it.type.contains(selectedTab.name, ignoreCase = true) }

                            if (filtered.isEmpty()) EmptyNotifications()
                            else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(filtered) { notif ->
                                    NotificationCard(notification = notif)
                                }
                            }
                        }
                        is UiState.Error -> Text(state.message, color = DangerRed)
                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
private fun TabChip(
    tab: NotificationTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PrimaryPurple else SurfaceCard
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = tab.icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = tab.label,
                color = if (isSelected) TextPrimary else TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun EmptyNotifications() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Notifications, null, tint = TextTertiary, modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "No notifications", color = TextSecondary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = "You're all caught up!", color = TextTertiary, fontSize = 12.sp)
    }
}