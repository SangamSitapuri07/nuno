package com.nuno.app.features.rewards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.theme.*
import com.nuno.app.core.utils.showToast
import com.nuno.app.features.home.LeftSidebar
import com.nuno.app.features.rewards.components.ChestCard
import com.nuno.app.features.rewards.components.DailyRewardGrid
import com.nuno.app.features.rewards.models.DailyReward
import com.nuno.app.features.rewards.models.RewardTab

@Composable
fun RewardsScreen(
    onBack: () -> Unit,
    onNavigateToPlay: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToFriends: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: RewardsViewModel = hiltViewModel()
) {
    val message by viewModel.message.collectAsState()
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(RewardTab.DAILY) }

    LaunchedEffect(message) {
        message?.let {
            context.showToast(it)
            viewModel.clearMessage()
        }
    }

    val dailyRewards = remember {
        listOf(
            DailyReward(1, "🪙", 100, "COINS", true, false),
            DailyReward(2, "⭐", 50, "XP", true, false),
            DailyReward(3, "🪙", 150, "COINS", false, true),
            DailyReward(4, "🎴", 1, "CARD", false, false),
            DailyReward(5, "💎", 10, "GEMS", false, false),
            DailyReward(6, "🪙", 300, "COINS", false, false),
            DailyReward(7, "📦", 1, "CHEST", false, false)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(BackgroundDark, BackgroundDarker)))
    ) {
        LeftSidebar(
            username = "Player", level = 1, coins = 0,
            selectedRoute = "rewards",
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "REWARDS",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                )
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RewardTab.entries.forEach { tab ->
                        TabButton(
                            label = tab.label,
                            icon = tab.icon,
                            isSelected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when (selectedTab) {
                        RewardTab.DAILY -> {
                            Text(
                                text = "DAILY LOGIN REWARDS",
                                color = AccentCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                            DailyRewardGrid(
                                rewards = dailyRewards,
                                onClaim = { viewModel.claimDailyReward() }
                            )
                        }
                        RewardTab.SEASON -> {
                            Text("SEASON PROGRESS", color = AccentCyan, fontSize = 12.sp, fontWeight = FontWeight.Black)
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Text("Season 1 - Card Masters", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                    Text("Ends in 30 days", color = TextSecondary, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    LinearProgressIndicator(
                                        progress = { 0.35f },
                                        modifier = Modifier.fillMaxWidth().height(12.dp),
                                        color = AccentGold,
                                        trackColor = SurfaceDark
                                    )
                                    Text("Level 12/50", color = AccentGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        RewardTab.ACHIEVEMENTS -> {
                            Text("ACHIEVEMENT REWARDS", color = AccentCyan, fontSize = 12.sp, fontWeight = FontWeight.Black)
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Complete achievements to earn rewards",
                                    color = TextSecondary,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(20.dp)
                                )
                            }
                        }
                        RewardTab.CHESTS -> {
                            Text("TREASURE CHESTS", color = AccentCyan, fontSize = 12.sp, fontWeight = FontWeight.Black)
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Box(modifier = Modifier.weight(1f)) {
                                    ChestCard(name = "COMMON CHEST", rarity = "COMMON", icon = "📦", onOpen = {})
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    ChestCard(name = "EPIC CHEST", rarity = "EPIC", icon = "🎁", onOpen = {})
                                }
                                Box(modifier = Modifier.weight(1f)) {
                                    ChestCard(name = "LEGENDARY", rarity = "LEGENDARY", icon = "💎", onOpen = {})
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun TabButton(
    label: String,
    icon: String,
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
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = if (isSelected) TextPrimary else TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}