package com.nuno.app.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.theme.*

@Composable
fun GameScreenScaffold(
    title: String,
    selectedRoute: String,
    onBack: () -> Unit,
    onNavigateToPlay: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToFriends: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    topBarActions: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(BackgroundDark, BackgroundDarker)))
    ) {
        LeftSidebar(
            username = "Player",
            level = 1,
            coins = 0,
            selectedRoute = selectedRoute,
            onPlayClick = onNavigateToPlay,
            onProfileClick = onNavigateToProfile,
            onFriendsClick = onNavigateToFriends,
            onLeaderboardClick = onNavigateToLeaderboard,
            onStoreClick = onNavigateToShop,
            onSettingsClick = onNavigateToSettings,
            onLogout = onLogout
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White.copy(0.08f), RoundedCornerShape(10.dp))
                            .border(1.dp, Color.White.copy(0.12f), RoundedCornerShape(10.dp))
                            .clickable { onBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextPrimary, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                }
                topBarActions()
            }

            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun LeftSidebar(
    username: String,
    level: Int,
    coins: Int,
    selectedRoute: String,
    onPlayClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onFriendsClick: () -> Unit = {},
    onLeaderboardClick: () -> Unit = {},
    onStoreClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .width(220.dp)
            .fillMaxHeight()
            .background(Brush.verticalGradient(listOf(Color(0xFF121535), Color(0xFF0A0C22))))
            .border(1.dp, Color.White.copy(0.06f))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .shadow(8.dp, CircleShape, spotColor = GameColors.Purple.copy(0.4f))
                    .background(Brush.radialGradient(listOf(Color(0xFF7B5CFF), Color(0xFF3A2A8A))), CircleShape)
                    .border(2.dp, GameColors.Cyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(username.firstOrNull()?.uppercase() ?: "P", color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(username, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text("Lv. $level • $coins 🪙", color = Color(0xFF8B92C0), fontSize = 10.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        SidebarItem(icon = Icons.Filled.PlayArrow, label = "Play", isSelected = selectedRoute == "home" || selectedRoute == "play", onClick = onPlayClick)
        SidebarItem(icon = Icons.Filled.Person, label = "Profile", isSelected = selectedRoute == "profile", onClick = onProfileClick)
        SidebarItem(icon = Icons.Filled.ShoppingCart, label = "Shop", isSelected = selectedRoute == "store", onClick = onStoreClick)
        SidebarItem(icon = Icons.Filled.Group, label = "Friends", isSelected = selectedRoute == "friends", onClick = onFriendsClick)
        SidebarItem(icon = Icons.Filled.EmojiEvents, label = "Leaderboard", isSelected = selectedRoute == "leaderboard", onClick = onLeaderboardClick)
        SidebarItem(icon = Icons.Filled.Settings, label = "Settings", isSelected = selectedRoute == "settings", onClick = onSettingsClick)

        Spacer(modifier = Modifier.weight(1f))

        SidebarItem(icon = Icons.AutoMirrored.Filled.Logout, label = "Logout", isSelected = false, onClick = onLogout)
    }
}

@Composable
private fun SidebarItem(icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .background(
                if (isSelected) Brush.linearGradient(listOf(Color(0xFF3B6BFF).copy(0.22f), Color(0xFF7B5CFF).copy(0.14f)))
                else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
                RoundedCornerShape(10.dp)
            )
            .border(1.dp, if (isSelected) GameColors.Blue.copy(0.3f) else Color.Transparent, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 9.dp, horizontal = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = if (isSelected) GameColors.Cyan else Color(0xFF8B92C0), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(label, color = if (isSelected) Color.White else Color(0xFF8B92C0), fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
        }
    }
}
