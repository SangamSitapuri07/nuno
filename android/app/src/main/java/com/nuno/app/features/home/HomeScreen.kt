package com.nuno.app.features.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.common.UiState
import com.nuno.app.core.theme.*
import com.nuno.app.core.utils.showToast
import com.nuno.app.features.auth.AuthViewModel
import com.nuno.app.features.profile.ProfileViewModel
import com.nuno.app.features.rewards.RewardsViewModel

@Composable
fun HomeScreen(
    onPlayClick: () -> Unit,
    onProfileClick: () -> Unit,
    onFriendsClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onStoreClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onLogout: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    rewardsViewModel: RewardsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val profileState by profileViewModel.profileState.collectAsState()
    val rewardsMessage by rewardsViewModel.message.collectAsState()

    val username = when (val s = profileState) {
        is UiState.Success -> s.data.username
        else -> "Player"
    }
    val level = when (val s = profileState) {
        is UiState.Success -> s.data.level
        else -> 1
    }
    val coins = when (val s = profileState) {
        is UiState.Success -> s.data.coins
        else -> 0
    }
    val rank = when (val s = profileState) {
        is UiState.Success -> s.data.leaderboard?.tier ?: "Bronze"
        else -> "Bronze"
    }

    LaunchedEffect(rewardsMessage) {
        rewardsMessage?.let {
            context.showToast(it)
            rewardsViewModel.clearMessage()
            profileViewModel.loadProfile()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = listOf(BackgroundDark, BackgroundDarker))
            )
    ) {
        // Left Sidebar Navigation
        LeftSidebar(
            username = username,
            level = level,
            coins = coins,
            selectedRoute = "home",
            onProfileClick = onProfileClick,
            onFriendsClick = onFriendsClick,
            onLeaderboardClick = onLeaderboardClick,
            onStoreClick = onStoreClick,
            onSettingsClick = onSettingsClick,
            onLogout = {
                authViewModel.logout()
                onLogout()
            }
        )

        // Main Content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // NUNO Logo
            Text(
                text = "NUNO",
                fontSize = 96.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                letterSpacing = 8.sp
            )
            Text(
                text = "Play. Compete. Win.",
                color = TextSecondary,
                fontSize = 14.sp,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Play Button (large)
            Button(
                onClick = onPlayClick,
                modifier = Modifier
                    .width(400.dp)
                    .height(80.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(PrimaryBlue, PrimaryPurple)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.PlayArrow,
                            null,
                            tint = TextPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PLAY",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary,
                            letterSpacing = 4.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Daily Reward
            Card(
                onClick = { rewardsViewModel.claimDailyReward() },
                modifier = Modifier
                    .width(400.dp)
                    .height(60.dp),
                colors = CardDefaults.cardColors(containerColor = AccentGold.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, AccentGold.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "🎁", fontSize = 28.sp)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Daily Reward", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Claim your daily bonus", color = TextSecondary, fontSize = 11.sp)
                    }
                    Text(text = "+100 🪙", color = AccentGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Right Side - Player Stats Panel
        Column(
            modifier = Modifier
                .width(240.dp)
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(SurfaceCard, CircleShape)
                    .align(Alignment.End)
            ) {
                Icon(Icons.Default.Notifications, null, tint = AccentCyan)
            }

            StatCard(title = "RANK", value = rank, icon = "🏆")
            StatCard(title = "LEVEL", value = "Lv. $level", icon = "⭐")
            StatCard(title = "COINS", value = coins.toString(), icon = "🪙")
        }
    }
}

@Composable
fun LeftSidebar(
    username: String,
    level: Int,
    coins: Int,
    selectedRoute: String,
    onProfileClick: () -> Unit = {},
    onFriendsClick: () -> Unit = {},
    onLeaderboardClick: () -> Unit = {},
    onStoreClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onPlayClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .width(220.dp)
            .fillMaxHeight()
            .background(SurfaceCard)
            .padding(16.dp)
    ) {
        // Player Info
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.radialGradient(colors = listOf(PrimaryBlue, PrimaryPurple)),
                        shape = CircleShape
                    )
                    .border(2.dp, AccentCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = username.firstOrNull()?.uppercase() ?: "?",
                    color = TextPrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = username, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(text = "Lv. $level", color = TextSecondary, fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SidebarItem(
            icon = Icons.Default.PlayArrow,
            label = "Play",
            isSelected = selectedRoute == "play" || selectedRoute == "home",
            onClick = onPlayClick
        )
        SidebarItem(
            icon = Icons.Default.Person,
            label = "Profile",
            isSelected = selectedRoute == "profile",
            onClick = onProfileClick
        )
        SidebarItem(
            icon = Icons.Default.ShoppingCart,
            label = "Shop",
            isSelected = selectedRoute == "shop",
            onClick = onStoreClick
        )
        SidebarItem(
            icon = Icons.Default.Group,
            label = "Friends",
            isSelected = selectedRoute == "friends",
            onClick = onFriendsClick
        )
        SidebarItem(
            icon = Icons.Default.EmojiEvents,
            label = "Leaderboard",
            isSelected = selectedRoute == "leaderboard",
            onClick = onLeaderboardClick
        )
        SidebarItem(
            icon = Icons.Default.Settings,
            label = "Settings",
            isSelected = selectedRoute == "settings",
            onClick = onSettingsClick
        )

        Spacer(modifier = Modifier.weight(1f))

        SidebarItem(
            icon = Icons.AutoMirrored.Filled.Logout,
            label = "Logout",
            isSelected = false,
            onClick = onLogout
        )
    }
}

@Composable
fun SidebarItem(
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
            .clickable { onClick() }
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
private fun StatCard(title: String, value: String, icon: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, BorderPurple.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = title, color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(text = value, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}