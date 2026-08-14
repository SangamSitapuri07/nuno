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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.common.UiState
import com.nuno.app.core.designsystem.GameColors
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

    val username = (profileState as? UiState.Success)?.data?.username ?: "Player"
    val level = (profileState as? UiState.Success)?.data?.level ?: 1
    val coins = (profileState as? UiState.Success)?.data?.coins ?: 0
    val rank = (profileState as? UiState.Success)?.data?.leaderboard?.tier ?: "Bronze"

    LaunchedEffect(rewardsMessage) {
        rewardsMessage?.let {
            context.showToast(it)
            rewardsViewModel.clearMessage()
            profileViewModel.loadProfile()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF0A0C24), Color(0xFF030410))))) {
        Row(modifier = Modifier.fillMaxSize()) {
            LeftSidebarPremium(
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

            Column(
                modifier = Modifier.weight(1f).fillMaxHeight().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .shadow(24.dp, RoundedCornerShape(20.dp), spotColor = GameColors.Purple.copy(0.4f))
                        .background(Brush.verticalGradient(listOf(Color(0xFF1D2148), Color(0xFF131636))), RoundedCornerShape(20.dp))
                        .border(1.dp, Color.White.copy(0.08f), RoundedCornerShape(20.dp))
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(GameColors.CardRed, GameColors.CardBlue, GameColors.CardGreen, GameColors.CardYellow).forEach { c ->
                                Box(
                                    modifier = Modifier
                                        .size(width = 48.dp, height = 64.dp)
                                        .shadow(12.dp, RoundedCornerShape(8.dp), spotColor = c.copy(0.5f))
                                        .background(c, RoundedCornerShape(8.dp))
                                        .border(2.dp, Color.White, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("N", color = if (c == GameColors.CardYellow) Color.Black else Color.White, fontSize = 32.sp, fontWeight = FontWeight.Black)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("PLAY • COMPETE • WIN", color = Color.White.copy(0.6f), fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 4.sp)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onPlayClick,
                    modifier = Modifier.width(360.dp).height(64.dp).shadow(20.dp, RoundedCornerShape(18.dp), spotColor = GameColors.Blue.copy(0.5f)),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(Brush.horizontalGradient(listOf(Color(0xFF3B6BFF), Color(0xFF7B5CFF))), RoundedCornerShape(18.dp)).border(1.dp, Color.White.copy(0.2f), RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("PLAY NOW", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 3.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    onClick = { rewardsViewModel.claimDailyReward() },
                    modifier = Modifier.width(360.dp).height(64.dp).shadow(12.dp, RoundedCornerShape(14.dp), spotColor = GameColors.Gold.copy(0.3f)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFC71F).copy(0.12f)),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, GameColors.Gold.copy(0.35f))
                ) {
                    Row(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(
                            modifier = Modifier.size(44.dp).background(Brush.linearGradient(listOf(GameColors.Gold, Color(0xFFFF8A00))), CircleShape).border(1.dp, Color.White.copy(0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) { Text("🎁", fontSize = 22.sp) }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Daily Reward", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Claim your bonus", color = Color(0xFF8B92C0), fontSize = 11.sp)
                        }
                        Text("+100 🪙", color = GameColors.Gold, fontSize = 14.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            Column(modifier = Modifier.width(240.dp).fillMaxHeight().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier.size(48.dp).background(Color(0xFF1E2249), CircleShape).border(1.dp, Color.White.copy(0.1f), CircleShape).align(Alignment.End).clickable { onNotificationsClick() },
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Default.Notifications, null, tint = GameColors.Cyan, modifier = Modifier.size(22.dp)) }

                StatCardPremium(title = "RANK", value = rank, icon = "🏆")
                StatCardPremium(title = "LEVEL", value = "Lv. $level", icon = "⭐")
                StatCardPremium(title = "COINS", value = coins.toString(), icon = "🪙")
            }
        }
    }
}

@Composable
fun LeftSidebarPremium(
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
    Column(modifier = Modifier.width(220.dp).fillMaxHeight().background(Brush.verticalGradient(listOf(Color(0xFF121535), Color(0xFF0A0C22)))).border(1.dp, Color.White.copy(0.06f)).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).background(Brush.radialGradient(listOf(Color(0xFF7B5CFF), Color(0xFF3A2A8A))), CircleShape).border(2.dp, GameColors.Cyan, CircleShape),
                contentAlignment = Alignment.Center
            ) { Text(username.firstOrNull()?.uppercase() ?: "?", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp) }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(username, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text("Lv. $level • $coins 🪙", color = Color(0xFF8B92C0), fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SidebarItemPremium(icon = Icons.Default.PlayArrow, label = "Play", isSelected = selectedRoute == "home", onClick = onPlayClick)
        SidebarItemPremium(icon = Icons.Default.Person, label = "Profile", isSelected = selectedRoute == "profile", onClick = onProfileClick)
        SidebarItemPremium(icon = Icons.Default.ShoppingCart, label = "Shop", isSelected = selectedRoute == "shop", onClick = onStoreClick)
        SidebarItemPremium(icon = Icons.Default.Group, label = "Friends", isSelected = selectedRoute == "friends", onClick = onFriendsClick)
        SidebarItemPremium(icon = Icons.Default.EmojiEvents, label = "Leaderboard", isSelected = selectedRoute == "leaderboard", onClick = onLeaderboardClick)
        SidebarItemPremium(icon = Icons.Default.Settings, label = "Settings", isSelected = selectedRoute == "settings", onClick = onSettingsClick)

        Spacer(modifier = Modifier.weight(1f))

        SidebarItemPremium(icon = Icons.AutoMirrored.Filled.Logout, label = "Logout", isSelected = false, onClick = onLogout)
    }
}

@Composable
fun SidebarItemPremium(icon: ImageVector, label: String, isSelected: Boolean = false, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp).background(
            if (isSelected) Brush.linearGradient(listOf(Color(0xFF3B6BFF).copy(0.22f), Color(0xFF7B5CFF).copy(0.14f))) else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
            RoundedCornerShape(12.dp)
        ).border(1.dp, if (isSelected) GameColors.Blue.copy(0.3f) else Color.Transparent, RoundedCornerShape(12.dp)).clickable { onClick() }.padding(vertical = 10.dp, horizontal = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = if (isSelected) GameColors.Cyan else Color(0xFF8B92C0), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(label, color = if (isSelected) Color.White else Color(0xFF8B92C0), fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
        }
    }
}

@Composable
private fun StatCardPremium(title: String, value: String, icon: String) {
    Box(
        modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Color(0xFF1D2148), Color(0xFF131636))), RoundedCornerShape(14.dp)).border(1.dp, Color.White.copy(0.08f), RoundedCornerShape(14.dp)).padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(36.dp).background(Color.White.copy(0.06f), CircleShape), contentAlignment = Alignment.Center) { Text(icon, fontSize = 18.sp) }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(title, color = Color(0xFF8B92C0), fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
