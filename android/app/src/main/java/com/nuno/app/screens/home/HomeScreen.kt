package com.nuno.app.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nuno.app.R
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.components.GameAvatar
import com.nuno.app.screens.social.DirectMessageDialog
import com.nuno.app.screens.social.FriendData
import java.util.Locale

data class OnlineFriendData(
    val userId: String,
    val username: String,
    val status: String,
    val isOnline: Boolean
)

@Composable
fun HomeScreen(
    username: String,
    level: Int,
    coins: Int,
    gems: Int,
    rank: String,
    onlineFriends: List<OnlineFriendData> = emptyList(),
    onPlay: () -> Unit,
    onNotifications: () -> Unit,
    onDailyReward: () -> Unit,
    onInviteFriend: (String) -> Unit = {},
    onNavigate: (String) -> Unit
) {
    var selectedFriendForAction by remember { mutableStateOf<OnlineFriendData?>(null) }
    var activeDmFriendData by remember { mutableStateOf<FriendData?>(null) }
    val config = LocalConfiguration.current
    val isWide = config.screenWidthDp > config.screenHeightDp

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0A0D1E))) {
        // Galaxy background - code, no image white issue
        Image(
            painter = painterResource(id = R.drawable.bg_galaxy_spiral),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.35f
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFF0E1230).copy(0.5f), Color(0xFF070818).copy(0.9f))))
        )

        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                GameAvatar(username = username.ifEmpty { "Sangam" }, size = 40.dp, borderColor = GameColors.Gold)
                Column {
                    Text(username.ifEmpty { "Sangam" }, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Lv. $level", color = GameColors.Cyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .background(Color(0xFF1B1F3D), RoundedCornerShape(20.dp))
                        .border(1.dp, Color(0xFF2C3159), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🪙", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(formatNum(coins.ifZero(12450)), color = GameColors.Gold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFF1B1F3D), RoundedCornerShape(20.dp))
                        .border(1.dp, Color(0xFF2C3159), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("💎", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(formatNum(gems.ifZero(230)), color = GameColors.Cyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF1B1F3D), CircleShape)
                        .border(1.dp, Color(0xFF2C3159), CircleShape)
                        .clickable { onNotifications() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Notifications, null, tint = Color(0xFF8A8FA8), modifier = Modifier.size(18.dp))
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF1B1F3D), CircleShape)
                        .border(1.dp, Color(0xFF2C3159), CircleShape)
                        .clickable { onNavigate("settings") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Settings, null, tint = Color(0xFF8A8FA8), modifier = Modifier.size(18.dp))
                }
            }
        }

        // Center - CODE DRAWN, NO WHITE BACKGROUND IMAGES
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // NUNO Logo - CODE, no image, so no white background visible - FIXED FROM UNO TO NUNO
                Box(
                    modifier = Modifier
                        .size(width = 140.dp, height = 80.dp)
                        .shadow(16.dp, RoundedCornerShape(20.dp), spotColor = Color.Red.copy(0.5f))
                        .background(
                            Brush.radialGradient(listOf(Color(0xFFFF1A1A), Color(0xFFCC0000))),
                            RoundedCornerShape(20.dp)
                        )
                        .border(3.dp, Color.White, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("NUNO", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                }

                Spacer(modifier = Modifier.height(28.dp))

                // PLAY Button - CODE DRAWN, no image
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(72.dp)
                        .shadow(20.dp, RoundedCornerShape(16.dp), spotColor = Color(0xFFE53935).copy(0.6f))
                        .background(
                            Brush.verticalGradient(listOf(Color(0xFFFF3A3A), Color(0xFFCC0000))),
                            RoundedCornerShape(16.dp)
                        )
                        .border(2.dp, Color(0xFFFFD700).copy(0.6f), RoundedCornerShape(16.dp))
                        .clickable { onPlay() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("▶", color = Color(0xFFFFD700), fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("PLAY", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("◀", color = Color(0xFFFFD700), fontSize = 14.sp)
                        }
                        Text("Quick Match", color = Color.White.copy(0.8f), fontSize = 10.sp, fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Card pedestal - use ORIGINAL small asset that has proper transparency (54KB, not white bg)
                Image(
                    painter = painterResource(id = R.drawable.ic_card_pedestal_3d),
                    contentDescription = "NUNO Cards",
                    modifier = Modifier.size(width = 200.dp, height = 130.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Daily reward - CODE, no image
                Row(
                    modifier = Modifier
                        .background(Color(0xFF1B1F3D).copy(0.9f), RoundedCornerShape(12.dp))
                        .border(1.dp, GameColors.Gold.copy(0.3f), RoundedCornerShape(12.dp))
                        .clickable { onDailyReward() }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🎁", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Daily Reward", color = GameColors.Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Bottom nav - like reference
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .align(Alignment.BottomCenter)
                .background(Color(0xFF0F1228))
                .border(1.dp, Color(0xFF1E2340))
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(label = "Home", icon = Icons.Default.Home, selected = true) { onNavigate("home") }
                BottomNavItem(label = "Friends", icon = Icons.Default.Group, selected = false) { onNavigate("friends") }
                BottomNavItem(label = "Leaderboard", icon = Icons.Default.EmojiEvents, selected = false) { onNavigate("leaderboard") }
                BottomNavItem(label = "Shop", icon = Icons.Default.ShoppingCart, selected = false) { onNavigate("store") }
                BottomNavItem(label = "Profile", icon = Icons.Default.Person, selected = false) { onNavigate("profile") }
            }
        }

        selectedFriendForAction?.let { friend ->
            FriendActionDialog(
                friend = friend,
                onSendDm = {
                    activeDmFriendData = FriendData(friend.userId, friend.username, friend.status, isOnline = friend.isOnline)
                    selectedFriendForAction = null
                },
                onInvite = {
                    onInviteFriend(friend.userId)
                    selectedFriendForAction = null
                },
                onDismiss = { selectedFriendForAction = null }
            )
        }

        activeDmFriendData?.let { fd ->
            DirectMessageDialog(friend = fd, onDismiss = { activeDmFriendData = null })
        }
    }
}

@Composable
private fun BottomNavItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, selected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier.clickable { onClick() }.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, null, tint = if (selected) Color(0xFFFFC107) else Color(0xFF5A607F), modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(label, color = if (selected) Color(0xFFFFC107) else Color(0xFF5A607F), fontSize = 9.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
private fun FriendActionDialog(friend: OnlineFriendData, onSendDm: () -> Unit, onInvite: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .background(Color(0xFF1E2248), RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFF2C3159), RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                GameAvatar(username = friend.username, size = 48.dp, borderColor = GameColors.Cyan)
                Spacer(modifier = Modifier.height(10.dp))
                Text(friend.username, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onSendDm, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A3F8A))) { Text("Message", color = Color.White) }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onInvite, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = GameColors.Green)) { Text("Invite", color = Color.White) }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Cancel", color = Color.White) }
            }
        }
    }
}

private fun formatNum(n: Int): String = when {
    n >= 1_000_000 -> String.format(Locale.US, "%.1fM", n / 1_000_000.0)
    n >= 1_000 -> String.format(Locale.US, "%.1fK", n / 1_000.0)
    else -> n.toString()
}

private fun Int.ifZero(default: Int): Int = if (this == 0) default else this
