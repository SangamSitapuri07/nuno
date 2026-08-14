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

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF070A1A))) {
        // Galaxy background
        Image(
            painter = painterResource(id = R.drawable.bg_galaxy_spiral),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.28f
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFF0E1230).copy(0.4f), Color(0xFF070A1A).copy(0.92f))))
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // ================= TOP BAR =================
            // [Avatar|Sangam] [🪙 700 +] [💎 230 +] [🔔] [⚙️]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .background(Color(0xFF0F1228).copy(alpha = 0.9f), RoundedCornerShape(14.dp))
                    .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(14.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    GameAvatar(username = username.ifEmpty { "Sangam" }, size = 38.dp, borderColor = Color(0xFFFFC107))
                    Column {
                        Text(username.ifEmpty { "Sangam" }, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Lv. $level", color = Color(0xFF00E5FF), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Coins 700 +
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF1B1F3D), RoundedCornerShape(20.dp))
                            .border(1.dp, Color(0xFF2C3159), RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .background(Color(0xFFFFC107), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🪙", fontSize = 10.sp)
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(formatNum(coins.ifZero(700)), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(Color.White.copy(0.12f), CircleShape)
                                    .clickable { onNavigate("store") },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    // Gems 230 +
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF1B1F3D), RoundedCornerShape(20.dp))
                            .border(1.dp, Color(0xFF2C3159), RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .background(Color(0xFF00E5FF), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("💎", fontSize = 10.sp)
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(formatNum(gems.ifZero(230)), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(Color.White.copy(0.12f), CircleShape)
                                    .clickable { onNavigate("store") },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Bell
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
                    // Settings
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

            // ================= MIDDLE STAGE =================
            // 3 columns: Cards Deck | NUNO Banner | Invite Friends with Slots
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // LEFT: 🃏 CARDS DECK (3D Pedestal) - USING GENERATED IMAGE ASSET
                Box(
                    modifier = Modifier
                        .weight(0.32f)
                        .fillMaxHeight()
                        .background(Color(0xFF11142E).copy(0.85f), RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(16.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🃏", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("CARDS DECK", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 0.8.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        // Generated 3D Pedestal Asset - dark background #070A1A blends, no white bg
                        Image(
                            painter = painterResource(id = R.drawable.ic_cards_deck_pedestal_new),
                            contentDescription = "NUNO 3D Pedestal",
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF00E5FF).copy(0.12f), RoundedCornerShape(6.dp))
                                .border(1.dp, Color(0xFF00E5FF).copy(0.3f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("NUNO • 108 Cards", color = Color(0xFF00E5FF), fontSize = 7.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // CENTER: NUNO BANNER - USING GENERATED IMAGE ASSET
                Box(
                    modifier = Modifier
                        .weight(0.36f)
                        .fillMaxHeight()
                        .background(
                            Brush.verticalGradient(listOf(Color(0xFF151A3A).copy(0.9f), Color(0xFF0E1130).copy(0.95f))),
                            RoundedCornerShape(16.dp)
                        )
                        .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(16.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
                        // Generated NUNO Banner Asset - dark bg #070A1A no white
                        Image(
                            painter = painterResource(id = R.drawable.ic_nuno_banner_new),
                            contentDescription = "NUNO Banner",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp),
                            contentScale = ContentScale.Fit
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF1B1F3D), RoundedCornerShape(10.dp))
                                .border(1.dp, Color(0xFF2C3159).copy(0.5f), RoundedCornerShape(10.dp))
                                .padding(8.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.EmojiEvents, null, tint = Color(0xFFFFC107), modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Current Tier: Diamond I", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Box(modifier = Modifier.background(Color(0xFFFFC107).copy(0.15f), RoundedCornerShape(5.dp)).padding(horizontal = 5.dp, vertical = 2.dp)) {
                                        Text("12,450 🏆", color = Color(0xFFFFC107), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Box(modifier = Modifier.background(Color(0xFF00E5FF).copy(0.15f), RoundedCornerShape(5.dp)).padding(horizontal = 5.dp, vertical = 2.dp)) {
                                        Text("Top 5%", color = Color(0xFF00E5FF), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF1B1F3D).copy(0.6f), RoundedCornerShape(8.dp))
                                .border(1.dp, GameColors.Gold.copy(0.2f), RoundedCornerShape(8.dp))
                                .clickable { onDailyReward() }
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_daily_reward_icon),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Daily Reward", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text("Claim 500", color = Color(0xFF8A8FA8), fontSize = 7.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .background(GameColors.Gold, RoundedCornerShape(5.dp))
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                Text("CLAIM", color = Color.Black, fontSize = 7.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }

                // RIGHT: 👥 INVITE FRIENDS with Slots
                Box(
                    modifier = Modifier
                        .weight(0.32f)
                        .fillMaxHeight()
                        .background(Color(0xFF11142E).copy(0.9f), RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(16.dp))
                        .padding(12.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Group, null, tint = Color(0xFF00E5FF), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("INVITE FRIENDS", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 0.8.sp)
                            Spacer(modifier = Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(Color.White.copy(0.08f), CircleShape)
                                    .clickable { onNavigate("friends") },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFF1E2340))
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Slot 1
                        InviteSlot(
                            slotNumber = 1,
                            friend = onlineFriends.getOrNull(0),
                            onInvite = { onInviteFriend(onlineFriends.getOrNull(0)?.userId ?: "") },
                            onAddFriend = { onNavigate("friends") }
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Slot 2
                        InviteSlot(
                            slotNumber = 2,
                            friend = onlineFriends.getOrNull(1),
                            onInvite = { onInviteFriend(onlineFriends.getOrNull(1)?.userId ?: "") },
                            onAddFriend = { onNavigate("friends") }
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        // Online count
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF0E1130), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(6.dp).background(Color(0xFF00E676), CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("${onlineFriends.count { it.isOnline }.ifZero(3)} Online", color = Color(0xFF00E676), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.weight(1f))
                            Text("View All", color = Color(0xFF5A607F), fontSize = 9.sp, modifier = Modifier.clickable { onNavigate("friends") })
                        }
                    }
                }
            }

            // ================= SPLIT BOTTOM LAYER =================
            // [🏠 Home] [🏆 Leaderboard] [🛒 Shop] | [▶ PLAY] - EXACTLY YOUR ASCII
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Left Bottom: Home, Leaderboard, Shop - dashed border in ASCII = solid border in code
                Box(
                    modifier = Modifier
                        .weight(0.56f)
                        .height(64.dp)
                        .background(Color(0xFF0F1228), RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(16.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BottomTabItem(label = "Home", icon = Icons.Default.Home, selected = true) { onNavigate("home") }
                        BottomTabItem(label = "Leaderboard", icon = Icons.Default.EmojiEvents, selected = false) { onNavigate("leaderboard") }
                        BottomTabItem(label = "Shop", icon = Icons.Default.ShoppingCart, selected = false) { onNavigate("store") }
                        BottomTabItem(label = "Profile", icon = Icons.Default.Person, selected = false, showOnlyWide = true) { onNavigate("profile") }
                    }
                }

                // Right Bottom: PLAY button - USING GENERATED IMAGE ASSET with dark bg no white
                Box(
                    modifier = Modifier
                        .weight(0.44f)
                        .height(64.dp)
                        .shadow(16.dp, RoundedCornerShape(16.dp), spotColor = Color(0xFFE53935).copy(0.5f))
                        .background(Color(0xFF0A0D1E), RoundedCornerShape(16.dp))
                        .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(16.dp))
                        .clickable { onPlay() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_play_button_new),
                        contentDescription = "PLAY",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        // Dialogs
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
private fun InviteSlot(slotNumber: Int, friend: OnlineFriendData?, onInvite: () -> Unit, onAddFriend: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF1B1F3D).copy(0.8f), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFF2C3159).copy(0.5f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        if (friend == null) {
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.White.copy(0.06f), CircleShape)
                            .border(1.dp, Color.White.copy(0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PersonAdd, null, tint = Color(0xFF5A607F), modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Slot $slotNumber", color = Color(0xFF5A607F), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(Color(0xFF2A3F8A), RoundedCornerShape(8.dp))
                        .border(1.dp, Color.White.copy(0.15f), RoundedCornerShape(8.dp))
                        .clickable { onAddFriend() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("➕", color = Color.White, fontSize = 12.sp)
                }
            }
        } else {
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                GameAvatar(username = friend.username, size = 32.dp, borderColor = GameColors.Green, showGlow = false)
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(friend.username, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(friend.status, color = Color(0xFF00E676), fontSize = 9.sp)
                }
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(GameColors.Green, RoundedCornerShape(8.dp))
                        .clickable { onInvite() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun BottomTabItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, selected: Boolean, showOnlyWide: Boolean = false, onClick: () -> Unit) {
    val config = LocalConfiguration.current
    val isWide = config.screenWidthDp > 600
    if (showOnlyWide && !isWide) return

    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, null, tint = if (selected) Color(0xFFFFC107) else Color(0xFF5A607F), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(3.dp))
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
