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

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF070A1A))) {
        // Background image asset - galaxy
        Image(
            painter = painterResource(id = R.drawable.bg_galaxy_spiral),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.32f
        )

        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .background(Color(0xFF0F1228), RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                GameAvatar(username = username.ifEmpty { "Sangam" }, size = 30.dp, borderColor = Color(0xFFFFC107))
                Text(username.ifEmpty { "Sangam" }, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("Lv.$level", color = Color(0xFF00E5FF), fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(modifier = Modifier.background(Color(0xFF1B1F3D), RoundedCornerShape(16.dp)).border(1.dp, Color(0xFF2C3159), RoundedCornerShape(16.dp)).padding(horizontal = 7.dp, vertical = 3.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(12.dp).background(Color(0xFFFFC107), CircleShape), contentAlignment = Alignment.Center) { Text("🪙", fontSize = 7.sp) }
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("${formatNum(coins.ifZero(700))}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(3.dp))
                        Box(modifier = Modifier.size(12.dp).background(Color.White.copy(0.1f), CircleShape).clickable { onNavigate("store") }, contentAlignment = Alignment.Center) { Text("+", color = Color.White, fontSize = 7.sp) }
                    }
                }
                Box(modifier = Modifier.background(Color(0xFF1B1F3D), RoundedCornerShape(16.dp)).border(1.dp, Color(0xFF2C3159), RoundedCornerShape(16.dp)).padding(horizontal = 7.dp, vertical = 3.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(12.dp).background(Color(0xFF00E5FF), CircleShape), contentAlignment = Alignment.Center) { Text("💎", fontSize = 7.sp) }
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("${formatNum(gems.ifZero(230))}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(3.dp))
                        Box(modifier = Modifier.size(12.dp).background(Color.White.copy(0.1f), CircleShape).clickable { onNavigate("store") }, contentAlignment = Alignment.Center) { Text("+", color = Color.White, fontSize = 7.sp) }
                    }
                }
                Box(modifier = Modifier.size(26.dp).background(Color(0xFF1B1F3D), CircleShape).border(1.dp, Color(0xFF2C3159), CircleShape).clickable { onNotifications() }, contentAlignment = Alignment.Center) { Icon(Icons.Default.Notifications, null, tint = Color(0xFF8A8FA8), modifier = Modifier.size(12.dp)) }
                Box(modifier = Modifier.size(26.dp).background(Color(0xFF1B1F3D), CircleShape).border(1.dp, Color(0xFF2C3159), CircleShape).clickable { onNavigate("settings") }, contentAlignment = Alignment.Center) { Icon(Icons.Default.Settings, null, tint = Color(0xFF8A8FA8), modifier = Modifier.size(12.dp)) }
            }
        }

        // LEFT: CARDS DECK - USING IMAGE ASSET (3D Pedestal)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 8.dp, top = 48.dp)
                .width(140.dp)
                .height(150.dp)
                .background(Color(0xFF11142E), RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(10.dp))
                .padding(6.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🃏", fontSize = 9.sp)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("CARDS DECK", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Black)
                }
                Spacer(modifier = Modifier.height(4.dp))
                // IMAGE ASSET for 3D pedestal - original small asset + new generated with transparent bg
                Image(
                    painter = painterResource(id = R.drawable.ic_card_pedestal_3d),
                    contentDescription = "Cards Deck 3D Pedestal",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF00E5FF).copy(0.1f), RoundedCornerShape(5.dp)).padding(vertical = 2.dp), contentAlignment = Alignment.Center) {
                    Text("NUNO • 108 Cards", color = Color(0xFF00E5FF), fontSize = 6.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // CENTER: NUNO BANNER - USING IMAGE ASSET
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp)
                .width(160.dp)
                .height(72.dp)
                .shadow(8.dp, RoundedCornerShape(10.dp), spotColor = Color(0xFFE53935).copy(0.4f))
                .background(Color(0xFF11142E), RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(10.dp))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            // Using generated NUNO banner image asset with dark bg cleaned to transparent
            Image(
                painter = painterResource(id = R.drawable.ic_nuno_banner_new),
                contentDescription = "NUNO Banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        // RIGHT: INVITE FRIENDS - code + icon asset
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 8.dp, top = 48.dp)
                .width(150.dp)
                .height(180.dp)
                .background(Color(0xFF11142E), RoundedCornerShape(10.dp))
                .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(10.dp))
                .padding(6.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_invite_friends_header),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        alpha = 0.9f
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("INVITE FRIENDS", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Black)
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.size(14.dp).background(Color.White.copy(0.08f), CircleShape).clickable { onNavigate("friends") }, contentAlignment = Alignment.Center) { Text("+", color = Color.White, fontSize = 8.sp) }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFF1E2340)))
                Spacer(modifier = Modifier.height(4.dp))
                SlotRow(slot = 1, friend = onlineFriends.getOrNull(0), onInvite = { onInviteFriend(onlineFriends.getOrNull(0)?.userId ?: "") }, onAdd = { onNavigate("friends") })
                Spacer(modifier = Modifier.height(4.dp))
                SlotRow(slot = 2, friend = onlineFriends.getOrNull(1), onInvite = { onInviteFriend(onlineFriends.getOrNull(1)?.userId ?: "") }, onAdd = { onNavigate("friends") })
                Spacer(modifier = Modifier.weight(1f))
                Row(modifier = Modifier.fillMaxWidth().background(Color(0xFF0E1130), RoundedCornerShape(5.dp)).padding(horizontal = 5.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(4.dp).background(Color(0xFF00E676), CircleShape))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("${onlineFriends.size.ifZero(2)} Online", color = Color(0xFF00E676), fontSize = 7.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // SPLIT BOTTOM LAYER
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Box(
                modifier = Modifier
                    .weight(0.58f)
                    .height(46.dp)
                    .background(Color(0xFF0F1228), RoundedCornerShape(10.dp))
                    .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(10.dp))
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                    BottomTab(label = "Home", icon = Icons.Default.Home, selected = true) { onNavigate("home") }
                    BottomTab(label = "Leaderboard", icon = Icons.Default.EmojiEvents, selected = false) { onNavigate("leaderboard") }
                    BottomTab(label = "Shop", icon = Icons.Default.ShoppingCart, selected = false) { onNavigate("store") }
                }
            }

            // PLAY button - USING IMAGE ASSET with dark bg
            Box(
                modifier = Modifier
                    .weight(0.42f)
                    .height(46.dp)
                    .shadow(10.dp, RoundedCornerShape(10.dp), spotColor = Color(0xFFE53935).copy(0.4f))
                    .background(Color(0xFF0A0D1E), RoundedCornerShape(10.dp))
                    .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(10.dp))
                    .clickable { onPlay() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_play_button_new),
                    contentDescription = "PLAY",
                    modifier = Modifier.fillMaxSize().padding(4.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        selectedFriendForAction?.let { friend ->
            DialogBox(friend = friend, onSendDm = {
                activeDmFriendData = FriendData(friend.userId, friend.username, friend.status, isOnline = friend.isOnline)
                selectedFriendForAction = null
            }, onInvite = {
                onInviteFriend(friend.userId)
                selectedFriendForAction = null
            }, onDismiss = { selectedFriendForAction = null })
        }

        activeDmFriendData?.let { fd ->
            DirectMessageDialog(friend = fd, onDismiss = { activeDmFriendData = null })
        }
    }
}

@Composable
private fun SlotRow(slot: Int, friend: OnlineFriendData?, onInvite: () -> Unit, onAdd: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xFF1B1F3D).copy(0.7f), RoundedCornerShape(6.dp))
            .border(1.dp, Color(0xFF2C3159).copy(0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        if (friend == null) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(18.dp).background(Color.White.copy(0.05f), CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.PersonAdd, null, tint = Color(0xFF5A607F), modifier = Modifier.size(10.dp))
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Slot $slot", color = Color(0xFF5A607F), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.size(18.dp).background(Color(0xFF2A3F8A), RoundedCornerShape(4.dp)).clickable { onAdd() }, contentAlignment = Alignment.Center) {
                    Text("➕", color = Color.White, fontSize = 8.sp)
                }
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(20.dp).background(Brush.radialGradient(listOf(Color(0xFF7B5CFF), Color(0xFF3A2A8A))), CircleShape).border(1.dp, Color(0xFF00E676), CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text(friend.username.first().uppercase(), color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Black) }
                Spacer(modifier = Modifier.width(4.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(friend.username, color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text("Offline", color = Color(0xFF00E676), fontSize = 6.sp)
                }
                Box(modifier = Modifier.size(18.dp).background(Color(0xFF00C853), RoundedCornerShape(4.dp)).clickable { onInvite() }, contentAlignment = Alignment.Center) {
                    Text("✓", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun BottomTab(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, selected: Boolean, onClick: () -> Unit) {
    Column(modifier = Modifier.clickable { onClick() }.padding(horizontal = 6.dp, vertical = 2.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = if (selected) Color(0xFFFFC107) else Color(0xFF5A607F), modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.height(1.dp))
        Text(label, color = if (selected) Color(0xFFFFC107) else Color(0xFF5A607F), fontSize = 7.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
private fun DialogBox(friend: OnlineFriendData, onSendDm: () -> Unit, onInvite: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(modifier = Modifier.width(220.dp).background(Color(0xFF1B1F3D), RoundedCornerShape(12.dp)).border(1.dp, Color(0xFF2C3159), RoundedCornerShape(12.dp)).padding(12.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(friend.username, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onSendDm, modifier = Modifier.fillMaxWidth().height(32.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A3F8A))) { Text("Message", fontSize = 10.sp, color = Color.White) }
                Spacer(modifier = Modifier.height(4.dp))
                Button(onClick = onInvite, modifier = Modifier.fillMaxWidth().height(32.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))) { Text("Invite", fontSize = 10.sp, color = Color.White) }
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth().height(28.dp)) { Text("Cancel", fontSize = 10.sp, color = Color.White) }
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
