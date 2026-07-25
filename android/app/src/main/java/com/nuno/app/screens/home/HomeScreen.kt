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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.GameAvatar
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

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
    Box(modifier = Modifier.fillMaxSize()) {
        // Background — PremiumGalaxyBackground (from GalaxyBackground.kt)
        PremiumGalaxyBackground()
        SpaceParticles()

        // Content
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
        ) {
            // LEFT - Spinning Wheel
            Box(
                modifier = Modifier.weight(0.4f).fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                SpinningCardWheel()
            }

            // CENTER - Profile + Play + Gift
            Column(
                modifier = Modifier.weight(0.3f).fillMaxHeight().padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(0.15f))

                PremiumAvatar(username = username)
                Spacer(modifier = Modifier.height(6.dp))
                Text(username, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text("Lv. $level", color = Color(0xFF8899BB), fontSize = 11.sp)

                Spacer(modifier = Modifier.height(14.dp))

                PremiumPlayButton(onClick = onPlay)

                Spacer(modifier = Modifier.height(10.dp))

                TreasureChest(onClick = onDailyReward)

                Spacer(modifier = Modifier.weight(0.1f))
            }

            // RIGHT - Stats + Friends
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 8.dp)
            ) {
                // Currency pills row — coins, gems, notifications only (matches reference: no settings icon here)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    MetallicCurrencyPill("🪙", formatNum(coins), GameColors.Gold)
                    Spacer(modifier = Modifier.width(4.dp))
                    MetallicCurrencyPill("💎", formatNum(gems), GameColors.Cyan)
                    Spacer(modifier = Modifier.width(4.dp))
                    MetallicNotificationPill(badge = 1) { onNotifications() }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Rank + Season side by side (FIXED HEIGHT)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    MetallicRankCard(rank = rank, modifier = Modifier.weight(1f).fillMaxHeight())
                    MetallicSeasonCard(season = "S1", modifier = Modifier.weight(1f).fillMaxHeight())
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Friends panel (takes remaining space)
                FrostedFriendsPanel(
                    onlineCount = onlineFriends.count { it.isOnline },
                    friends = onlineFriends,
                    onInvite = onInviteFriend,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }

        // Nav Bar
        PremiumNavBar(
            selectedRoute = "home",
            onNavigate = onNavigate,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// ═══════════════════════════════════════
// SPACE PARTICLES (ambient dust overlay, kept on top of PremiumGalaxyBackground)
// ═══════════════════════════════════════

@Composable
private fun SpaceParticles() {
    val infiniteTransition = rememberInfiniteTransition(label = "dust")
    val progress by infiniteTransition.animateFloat(
        0f, 1f, infiniteRepeatable(tween(20_000, easing = LinearEasing)), label = "p"
    )

    val particles = remember {
        List(20) {
            DustParticle(
                x = Random.nextFloat(), y = Random.nextFloat(),
                speed = Random.nextFloat() * 0.1f + 0.03f,
                size = Random.nextFloat() * 1.5f + 0.5f,
                color = listOf(Color(0xFFFFD700), Color(0xFF4CC9F0), Color(0xFFB980F5)).random()
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        particles.forEach { p ->
            val y = ((p.y + progress * p.speed) % 1f) * size.height
            val x = p.x * size.width
            val alpha = when {
                (y / size.height) < 0.1f -> (y / size.height) * 10f
                (y / size.height) > 0.9f -> (1f - y / size.height) * 10f
                else -> 0.4f
            }
            drawCircle(p.color.copy(alpha = alpha), p.size, Offset(x, y))
        }
    }
}

private data class DustParticle(val x: Float, val y: Float, val speed: Float, val size: Float, val color: Color)

// ═══════════════════════════════════════
// TREASURE CHEST
// ═══════════════════════════════════════

@Composable
private fun TreasureChest(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "chest")
    val bounce by infiniteTransition.animateFloat(
        0f, -5f,
        infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "bounce"
    )
    val glow by infiniteTransition.animateFloat(
        0.4f, 0.9f,
        infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "glow"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .offset(y = bounce.dp)
                .shadow(12.dp, RoundedCornerShape(8.dp), spotColor = GameColors.Gold.copy(alpha = glow)),
            contentAlignment = Alignment.Center
        ) {
            // NOTE: using 🎁 emoji as a stand-in — the reference screenshot shows an
            // ornate locked chest with a gem, which no emoji matches exactly. A pixel-close
            // version would need to be hand-drawn with Canvas the same way SpinningCardWheel
            // and PremiumPlayButton are. Flagging this rather than pretending it's identical.
            Text("🎁", fontSize = 36.sp)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text("Daily Gift", color = Color(0xFF8899BB), fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }
}

private fun formatNum(n: Int): String = when {
    n >= 1_000_000 -> String.format("%.1fM", n / 1_000_000.0)
    n >= 1_000 -> String.format("%.1fK", n / 1_000.0)
    else -> n.toString()
}