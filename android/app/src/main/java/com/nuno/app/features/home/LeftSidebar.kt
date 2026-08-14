package com.nuno.app.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
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
            .background(Brush.verticalGradient(listOf(Color(0xFF121535), Color(0xFF0A0C22))))
            .border(1.dp, Color.White.copy(0.06f))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .shadow(8.dp, CircleShape, spotColor = GameColors.Purple.copy(0.4f))
                    .background(Brush.radialGradient(listOf(Color(0xFF7B5CFF), Color(0xFF3A2A8A))), CircleShape)
                    .border(2.dp, GameColors.Cyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(username.firstOrNull()?.uppercase() ?: "?", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(username, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text("Lv. $level • $coins 🪙", color = Color(0xFF8B92C0), fontSize = 11.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        SidebarItem(icon = Icons.Default.PlayArrow, label = "Play", isSelected = selectedRoute == "home" || selectedRoute == "play", onClick = onPlayClick)
        SidebarItem(icon = Icons.Default.Person, label = "Profile", isSelected = selectedRoute == "profile", onClick = onProfileClick)
        SidebarItem(icon = Icons.Default.ShoppingCart, label = "Shop", isSelected = selectedRoute == "store" || selectedRoute == "shop", onClick = onStoreClick)
        SidebarItem(icon = Icons.Default.Group, label = "Friends", isSelected = selectedRoute == "friends", onClick = onFriendsClick)
        SidebarItem(icon = Icons.Default.EmojiEvents, label = "Leaderboard", isSelected = selectedRoute == "leaderboard", onClick = onLeaderboardClick)
        SidebarItem(icon = Icons.Default.Settings, label = "Settings", isSelected = selectedRoute == "settings", onClick = onSettingsClick)

        Spacer(modifier = Modifier.weight(1f))

        SidebarItem(icon = Icons.AutoMirrored.Filled.Logout, label = "Logout", isSelected = false, onClick = onLogout)
    }
}

@Composable
fun SidebarItem(icon: ImageVector, label: String, isSelected: Boolean = false, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .background(
                if (isSelected) Brush.linearGradient(listOf(Color(0xFF3B6BFF).copy(0.22f), Color(0xFF7B5CFF).copy(0.14f)))
                else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
                RoundedCornerShape(12.dp)
            )
            .border(1.dp, if (isSelected) GameColors.Blue.copy(0.3f) else Color.Transparent, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = if (isSelected) GameColors.Cyan else Color(0xFF8B92C0), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(label, color = if (isSelected) Color.White else Color(0xFF8B92C0), fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
        }
    }
}
