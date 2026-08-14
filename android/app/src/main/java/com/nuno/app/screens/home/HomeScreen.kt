package com.nuno.app.screens.home

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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
        Column(modifier = Modifier.fillMaxSize()) {
            // TOP BAR - tight, no waste
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .background(Color(0xFF0F1228), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    GameAvatar(username = username.ifEmpty { "Sangam" }, size = 32.dp, borderColor = Color(0xFFFFC107))
                    Text(username.ifEmpty { "Sangam" }, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("Lv.$level", color = Color(0xFF00E5FF), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    PillCoin(text = "🪙 ${formatNum(coins.ifZero(700))}", onPlus = { onNavigate("store") })
                    PillGem(text = "💎 ${formatNum(gems.ifZero(230))}", onPlus = { onNavigate("store") })
                    IconBox(icon = Icons.Default.Notifications, onClick = onNotifications)
                    IconBox(icon = Icons.Default.Settings, onClick = { onNavigate("settings") })
                }
            }

            // MIDDLE STAGE - 3 columns tight, no wasted padding
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // LEFT: CARDS DECK
                Box(
                    modifier = Modifier
                        .weight(0.32f)
                        .fillMaxHeight()
                        .background(Color(0xFF11142E), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(12.dp))
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🃏", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("CARDS DECK", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        // Cards deck visual - code only, no image asset waste
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(
                                    Brush.verticalGradient(listOf(Color(0xFF1E2A5A), Color(0xFF11142E))),
                                    RoundedCornerShape(10.dp)
                                )
                                .border(1.dp, Color(0xFF2A3A6B).copy(0.5f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy((-10).dp)) {
                                MiniCard(color = Color(0xFFE53935), value = "1")
                                MiniCard(color = Color(0xFFFFC107), value = "3")
                                MiniCard(color = Color(0xFF1E88E5), value = "8", isNuno = true)
                                MiniCard(color = Color(0xFF43A047), value = "2")
                                MiniCard(color = Color(0xFF000000), value = "+4", isWild = true)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF00E5FF).copy(0.1f), RoundedCornerShape(6.dp))
                                .border(1.dp, Color(0xFF00E5FF).copy(0.25f), RoundedCornerShape(6.dp))
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("NUNO • 108 Cards", color = Color(0xFF00E5FF), fontSize = 7.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // CENTER: NUNO BANNER - tight
                Box(
                    modifier = Modifier
                        .weight(0.36f)
                        .fillMaxHeight()
                        .background(Color(0xFF11142E), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(12.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
                        // NUNO Banner - code only
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .shadow(8.dp, RoundedCornerShape(10.dp), spotColor = Color(0xFFE53935).copy(0.4f))
                                .background(
                                    Brush.horizontalGradient(listOf(Color(0xFFD32F2F), Color(0xFFB71C1C))),
                                    RoundedCornerShape(10.dp)
                                )
                                .border(2.dp, Color(0xFFFFC107), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("NUNO", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
                                Text("ULTIMATE CARD BATTLE", color = Color(0xFFFFD700), fontSize = 6.sp, fontWeight = FontWeight.Black)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Tier info compact
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF1B1F3D), RoundedCornerShape(8.dp))
                                .padding(6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🏆", fontSize = 10.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Column {
                                    Text("Diamond I", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    Text("12,450 • Top 5%", color = Color(0xFFFFC107), fontSize = 7.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFC107).copy(0.15f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("PRO", color = Color(0xFFFFC107), fontSize = 7.sp, fontWeight = FontWeight.Black)
                            }
                        }

                        // Daily reward compact - no waste
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF1B1F3D).copy(0.7f), RoundedCornerShape(8.dp))
                                .border(1.dp, GameColors.Gold.copy(0.15f), RoundedCornerShape(8.dp))
                                .clickable { onDailyReward() }
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🎁", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Daily Reward", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text("500 coins", color = Color(0xFF5A607F), fontSize = 7.sp)
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

                // RIGHT: INVITE FRIENDS
                Box(
                    modifier = Modifier
                        .weight(0.32f)
                        .fillMaxHeight()
                        .background(Color(0xFF11142E), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(12.dp))
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Default.Group, null, tint = Color(0xFF00E5FF), modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("INVITE FRIENDS", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black)
                            Spacer(modifier = Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(Color.White.copy(0.08f), CircleShape)
                                    .clickable { onNavigate("friends") },
                                contentAlignment = Alignment.Center
                            ) { Text("+", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF1E2340)))
                        Spacer(modifier = Modifier.height(6.dp))

                        SlotRow(
                            slot = 1,
                            friend = onlineFriends.getOrNull(0),
                            onInvite = { onInviteFriend(onlineFriends.getOrNull(0)?.userId ?: "") },
                            onAdd = { onNavigate("friends") }
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        SlotRow(
                            slot = 2,
                            friend = onlineFriends.getOrNull(1),
                            onInvite = { onInviteFriend(onlineFriends.getOrNull(1)?.userId ?: "") },
                            onAdd = { onNavigate("friends") }
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF0E1130), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(5.dp).background(Color(0xFF00E676), CircleShape))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${onlineFriends.count { it.isOnline }.ifZero(3)} Online", color = Color(0xFF00E676), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // SPLIT BOTTOM LAYER - tight, no waste
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.58f)
                        .height(52.dp)
                        .background(Color(0xFF0F1228), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TabItem(label = "Home", icon = Icons.Default.Home, selected = true) { onNavigate("home") }
                        TabItem(label = "Leaderboard", icon = Icons.Default.EmojiEvents, selected = false) { onNavigate("leaderboard") }
                        TabItem(label = "Shop", icon = Icons.Default.ShoppingCart, selected = false) { onNavigate("store") }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(0.42f)
                        .height(52.dp)
                        .shadow(10.dp, RoundedCornerShape(12.dp), spotColor = Color(0xFFE53935).copy(0.4f))
                        .background(Brush.horizontalGradient(listOf(Color(0xFFFF2D2D), Color(0xFFB71C1C))), RoundedCornerShape(12.dp))
                        .border(2.dp, Color(0xFFFFC107), RoundedCornerShape(12.dp))
                        .clickable { onPlay() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PlayArrow, null, tint = Color(0xFFFFD700), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("PLAY", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    }
                }
            }
        }

        selectedFriendForAction?.let { friend ->
            FriendDialog(
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
private fun MiniCard(color: Color, value: String, isNuno: Boolean = false, isWild: Boolean = false) {
    Box(
        modifier = Modifier
            .size(width = 28.dp, height = 40.dp)
            .background(Color.Black, RoundedCornerShape(4.dp))
            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
            .padding(1.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color, RoundedCornerShape(3.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isNuno) "N" else if (isWild) "+4" else value,
                color = if (color == Color(0xFFFFC107) || color == Color.White) Color.Black else Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Composable
private fun PillCoin(text: String, onPlus: () -> Unit) {
    Row(
        modifier = Modifier
            .background(Color(0xFF1B1F3D), RoundedCornerShape(20.dp))
            .border(1.dp, Color(0xFF2C3159), RoundedCornerShape(20.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text.substringBefore(" "), fontSize = 10.sp)
        Spacer(modifier = Modifier.width(3.dp))
        Text(text.substringAfter(" ").substringBefore(" "), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(Color.White.copy(0.12f), CircleShape)
                .clickable { onPlus() },
            contentAlignment = Alignment.Center
        ) { Text("+", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun PillGem(text: String, onPlus: () -> Unit) {
    Row(
        modifier = Modifier
            .background(Color(0xFF1B1F3D), RoundedCornerShape(20.dp))
            .border(1.dp, Color(0xFF2C3159), RoundedCornerShape(20.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("💎", fontSize = 10.sp)
        Spacer(modifier = Modifier.width(3.dp))
        Text(text.substringAfter(" ").substringBefore(" "), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(Color.White.copy(0.12f), CircleShape)
                .clickable { onPlus() },
            contentAlignment = Alignment.Center
        ) { Text("+", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun IconBox(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .background(Color(0xFF1B1F3D), CircleShape)
            .border(1.dp, Color(0xFF2C3159), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = Color(0xFF8A8FA8), modifier = Modifier.size(14.dp))
    }
}

@Composable
private fun SlotRow(slot: Int, friend: OnlineFriendData?, onInvite: () -> Unit, onAdd: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(Color(0xFF1B1F3D).copy(0.7f), RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF2C3159).copy(0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (friend == null) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(24.dp).background(Color.White.copy(0.05f), CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.PersonAdd, null, tint = Color(0xFF5A607F), modifier = Modifier.size(12.dp))
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Slot $slot", color = Color(0xFF5A607F), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(Color(0xFF2A3F8A), RoundedCornerShape(6.dp))
                        .clickable { onAdd() },
                    contentAlignment = Alignment.Center
                ) { Text("➕", color = Color.White, fontSize = 9.sp) }
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(Brush.radialGradient(listOf(Color(0xFF7B5CFF), Color(0xFF3A2A8A))), CircleShape)
                        .border(1.dp, Color(0xFF00E676), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(friend.username.first().uppercase(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(friend.username, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text("Offline", color = Color(0xFF00E676), fontSize = 7.sp)
                }
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(Color(0xFF00C853), RoundedCornerShape(6.dp))
                        .clickable { onInvite() },
                    contentAlignment = Alignment.Center
                ) { Text("✓", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black) }
            }
        }
    }
}

@Composable
private fun TabItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, selected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, null, tint = if (selected) Color(0xFFFFC107) else Color(0xFF5A607F), modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(label, color = if (selected) Color(0xFFFFC107) else Color(0xFF5A607F), fontSize = 8.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
private fun FriendDialog(friend: OnlineFriendData, onSendDm: () -> Unit, onInvite: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .width(260.dp)
                .background(Color(0xFF1B1F3D), RoundedCornerShape(14.dp))
                .border(1.dp, Color(0xFF2C3159), RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Brush.radialGradient(listOf(Color(0xFF7B5CFF), Color(0xFF3A2A8A))), CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text(friend.username.first().uppercase(), color = Color.White, fontWeight = FontWeight.Black) }
                Spacer(modifier = Modifier.height(8.dp))
                Text(friend.username, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = onSendDm, modifier = Modifier.fillMaxWidth().height(36.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A3F8A))) { Text("Message", color = Color.White, fontSize = 11.sp) }
                Spacer(modifier = Modifier.height(6.dp))
                Button(onClick = onInvite, modifier = Modifier.fillMaxWidth().height(36.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))) { Text("Invite", color = Color.White, fontSize = 11.sp) }
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth().height(32.dp)) { Text("Cancel", color = Color.White, fontSize = 11.sp) }
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
