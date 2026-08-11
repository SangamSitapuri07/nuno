package com.nuno.app.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

    Box(modifier = Modifier.fillMaxSize()) {
        // Background — Animated Space Galaxy Image Asset (R.drawable.bg_galaxy_spiral)
        PremiumGalaxyBackground()

        // ═══════════════════════════════════════
        // TOP ROW: Left = Profile & Currencies | Right = Friends Panel
        // ═══════════════════════════════════════
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // TOP LEFT HEADER (Profile Pill + Gold + Gems + Settings - Notification Removed)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TopLeftProfilePill(username = username, level = level, rank = rank)
                MetallicCurrencyPill("🪙", formatNum(coins), GameColors.Gold)
                MetallicCurrencyPill("💎", formatNum(gems), GameColors.Cyan)
                MetallicSettingsButton { onNavigate("settings") }
            }

            // TOP RIGHT: Friends Panel (Stretched towards bottom)
            FrostedFriendsPanel(
                onlineCount = onlineFriends.count { it.isOnline },
                friends = onlineFriends,
                onFriendClick = { friend -> selectedFriendForAction = friend },
                onInvite = onInviteFriend,
                onAddFriendClick = { onNavigate("friends") },
                modifier = Modifier
                    .width(235.dp)
                    .height(160.dp)
            )
        }

        // ═══════════════════════════════════════
        // CENTER ROW: Left = Pedestal Cards Asset | Center = Chest Asset | Right = PLAY Button Asset
        // ═══════════════════════════════════════
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp, bottom = 52.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LEFT-CENTER AREA: 5-Card NUNO Fan Pedestal Image Asset (Shifted slightly left)
            Box(
                modifier = Modifier
                    .weight(0.50f)
                    .fillMaxHeight()
                    .offset(x = (-12).dp),
                contentAlignment = Alignment.Center
            ) {
                SpinningCardWheel()
            }

            // MIDDLE-CENTER AREA: Daily Gift Treasure Chest Image Asset (Animated floating bounce)
            Box(
                modifier = Modifier
                    .weight(0.22f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                TreasureChest(onClick = onDailyReward)
            }

            // RIGHT-CENTER AREA: PLAY Button Image Asset (Positioned at Bottom Right with floating bounce)
            Box(
                modifier = Modifier
                    .weight(0.28f)
                    .fillMaxHeight()
                    .padding(end = 12.dp, bottom = 8.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                PremiumPlayButton(onClick = onPlay)
            }
        }

        // ═══════════════════════════════════════
        // BOTTOM BAR (Left Half Only - 0.48f width)
        // ═══════════════════════════════════════
        PremiumNavBar(
            selectedRoute = "home",
            onNavigate = onNavigate,
            modifier = Modifier
                .fillMaxWidth(0.48f)
                .align(Alignment.BottomStart)
        )

        // FRIEND ACTION DIALOG (Ask if user wants to DM or Invite)
        selectedFriendForAction?.let { friend ->
            FriendActionDialog(
                friend = friend,
                onSendDm = {
                    activeDmFriendData = FriendData(
                        userId = friend.userId,
                        username = friend.username,
                        status = friend.status,
                        isOnline = friend.isOnline
                    )
                    selectedFriendForAction = null
                },
                onInvite = {
                    onInviteFriend(friend.userId)
                    selectedFriendForAction = null
                },
                onDismiss = { selectedFriendForAction = null }
            )
        }

        // DIRECT MESSAGE DIALOG ON HOME SCREEN
        activeDmFriendData?.let { friendData ->
            DirectMessageDialog(
                friend = friendData,
                onDismiss = { activeDmFriendData = null }
            )
        }
    }
}

// ═══════════════════════════════════════
// FRIEND ACTION MODAL DIALOG
// ═══════════════════════════════════════

@Composable
private fun FriendActionDialog(
    friend: OnlineFriendData,
    onSendDm: () -> Unit,
    onInvite: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .shadow(16.dp, RoundedCornerShape(18.dp), spotColor = Color(0xFF4CC9F0))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1E2548), Color(0xFF121630))
                    ),
                    RoundedCornerShape(18.dp)
                )
                .border(1.5.dp, Color(0xFF4CC9F0).copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                .padding(18.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                GameAvatar(username = friend.username, size = 44.dp, borderColor = Color(0xFF4CC9F0))
                Spacer(modifier = Modifier.height(8.dp))
                Text(friend.username, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Status: ${friend.status}", color = Color(0xFF4CC9F0), fontSize = 11.sp)

                Spacer(modifier = Modifier.height(16.dp))

                Text("Choose an action:", color = Color(0xFFA0B0D0), fontSize = 12.sp, fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.height(14.dp))

                // Action 1: Send Direct Message
                Button(
                    onClick = onSendDm,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4361EE)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(40.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Chat, null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Send Direct Message", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action 2: Invite to Room
                if (friend.isOnline && friend.status != "In Game") {
                    Button(
                        onClick = onInvite,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(40.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Gamepad, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Invite to Game", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Cancel Button
                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth().height(38.dp)
                ) {
                    Text("Cancel", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

// ═══════════════════════════════════════
// TREASURE CHEST (3D Asset loaded from drawable with animated bounce)
// ═══════════════════════════════════════

@Composable
private fun TreasureChest(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "chestImageAnim")

    val scalePulse by infiniteTransition.animateFloat(
        1f, 1.04f,
        infiniteRepeatable(tween(1600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "scale"
    )

    val floatBounce by infiniteTransition.animateFloat(
        0f, -4f,
        infiniteRepeatable(tween(1400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "bounce"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .scale(scalePulse)
                .offset(y = floatBounce.dp)
                .size(width = 130.dp, height = 110.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_treasure_chest_3d),
                contentDescription = "Treasure Chest",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

private fun formatNum(n: Int): String = when {
    n >= 1_000_000 -> String.format("%.1fM", n / 1_000_000.0)
    n >= 1_000 -> String.format("%.1fK", n / 1_000.0)
    else -> n.toString()
}