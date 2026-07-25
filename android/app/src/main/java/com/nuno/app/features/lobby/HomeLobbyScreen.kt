package com.nuno.app.features.lobby

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.common.UiState
import com.nuno.app.features.auth.AuthViewModel
import com.nuno.app.features.lobby.components.*
import com.nuno.app.features.profile.ProfileViewModel
import com.nuno.app.features.rewards.RewardsViewModel

@Composable
fun HomeLobbyScreen(
    onPlayClick: () -> Unit,
    onProfileClick: () -> Unit,
    onFriendsClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onStoreClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onLogout: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    rewardsViewModel: RewardsViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val profileState by profileViewModel.profileState.collectAsState()

    val username = (profileState as? UiState.Success)?.data?.username ?: "Player"
    val level = (profileState as? UiState.Success)?.data?.level ?: 1
    val coins = (profileState as? UiState.Success)?.data?.coins ?: 0
    val rank = (profileState as? UiState.Success)?.data?.leaderboard?.tier ?: "BRONZE"

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedBackground()
        RotatingCardRing()
        OverlayGradient()

        Column(
            modifier = Modifier.fillMaxSize().padding(top = 20.dp, bottom = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LogoSection()
            Spacer(modifier = Modifier.height(30.dp))
            PlayButton(onClick = onPlayClick)
            Spacer(modifier = Modifier.height(20.dp))
            RewardBadge(onClick = { rewardsViewModel.claimDailyReward() })
        }

        ProfilePanel(
            username = username,
            level = level,
            xpProgress = 0.4f,
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
        )

        TopRightBar(
            onNotifications = onNotificationsClick,
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
        )

        // Smaller compact right side cards
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp, top = 70.dp, bottom = 90.dp)
                .width(180.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RankCard(rank = rank)
            LevelCard(level = level)
            CoinCard(coins = coins)
        }

        // Bottom navigation flush with screen
        BottomNavigation(
            onProfile = onProfileClick,
            onShop = onStoreClick,
            onFriends = onFriendsClick,
            onLeaderboard = onLeaderboardClick,
            onSettings = onSettingsClick,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        ParticleLayer()
    }
}