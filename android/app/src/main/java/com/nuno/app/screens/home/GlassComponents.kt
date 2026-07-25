package com.nuno.app.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors

// ═══════════════════════════════════════
// METALLIC CURRENCY PILLS
// ═══════════════════════════════════════

@Composable
fun MetallicCurrencyPill(icon: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color(0xFF1A2040))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF2A3055), Color(0xFF1A2040), Color(0xFF252D50))
                ),
                RoundedCornerShape(20.dp)
            )
            .border(1.dp, Color(0xFF4A5580).copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(5.dp))
            Text(value, color = color, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MetallicNotificationPill(badge: Int = 0, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF2A3055), Color(0xFF1A2040))),
                RoundedCornerShape(12.dp)
            )
            .border(1.dp, Color(0xFF4A5580).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text("🔔", fontSize = 16.sp)
        if (badge > 0) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(Color(0xFF4361EE), CircleShape)
                    .border(1.5.dp, Color(0xFF1A2040), CircleShape)
                    .align(Alignment.TopEnd)
                    .offset(x = 3.dp, y = (-3).dp),
                contentAlignment = Alignment.Center
            ) {
                Text(badge.toString(), color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

// ═══════════════════════════════════════
// METALLIC RANK CARD (Bronze border)
// ═══════════════════════════════════════

@Composable
fun MetallicRankCard(rank: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(10.dp), spotColor = Color(0xFFCD7F32).copy(alpha = 0.3f))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1E2545).copy(alpha = 0.9f), Color(0xFF151B38).copy(alpha = 0.95f))
                ),
                RoundedCornerShape(10.dp)
            )
            .border(
                1.5.dp,
                Brush.linearGradient(
                    listOf(Color(0xFFCD7F32), Color(0xFF8B5A2B), Color(0xFFCD7F32), Color(0xFFDDA15E))
                ),
                RoundedCornerShape(10.dp)
            )
    ) {
        // Scan line texture effect
        Canvas(modifier = Modifier.fillMaxSize()) {
            for (i in 0..20) {
                val y = i * (size.height / 20)
                drawLine(Color.White.copy(alpha = 0.02f), Offset(0f, y), Offset(size.width, y), strokeWidth = 0.5f)
            }
        }

        // Corner accents
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Top left corner
            drawLine(Color(0xFF4CC9F0).copy(alpha = 0.3f), Offset(0f, 15f), Offset(15f, 0f), strokeWidth = 1f)
            // Bottom right corner
            drawLine(Color(0xFF4CC9F0).copy(alpha = 0.3f), Offset(size.width, size.height - 15f), Offset(size.width - 15f, size.height), strokeWidth = 1f)
        }

        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🏆", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text("RANK", color = Color(0xFF8899BB), fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text(rank, color = GameColors.Gold, fontSize = 13.sp, fontWeight = FontWeight.Black)
            }
        }

        // Bottom metallic line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, Color(0xFFCD7F32).copy(alpha = 0.5f), Color.Transparent)
                    )
                )
        )
    }
}

// ═══════════════════════════════════════
// METALLIC SEASON CARD
// ═══════════════════════════════════════

@Composable
fun MetallicSeasonCard(season: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(10.dp), spotColor = Color(0xFF4CC9F0).copy(alpha = 0.2f))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1E2545).copy(alpha = 0.9f), Color(0xFF151B38).copy(alpha = 0.95f))
                ),
                RoundedCornerShape(10.dp)
            )
            .border(
                1.5.dp,
                Brush.linearGradient(
                    listOf(Color(0xFF4A5580), Color(0xFF3A4570), Color(0xFF4CC9F0).copy(alpha = 0.3f), Color(0xFF4A5580))
                ),
                RoundedCornerShape(10.dp)
            )
    ) {
        // Scan lines
        Canvas(modifier = Modifier.fillMaxSize()) {
            for (i in 0..20) {
                val y = i * (size.height / 20)
                drawLine(Color.White.copy(alpha = 0.02f), Offset(0f, y), Offset(size.width, y), strokeWidth = 0.5f)
            }
        }

        // Metallic corner accents
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(Color(0xFF4CC9F0).copy(alpha = 0.2f), 180f, 90f, false, topLeft = Offset(size.width - 20f, 0f), size = androidx.compose.ui.geometry.Size(20f, 20f), style = Stroke(1f))
            drawArc(Color(0xFF4CC9F0).copy(alpha = 0.2f), 0f, 90f, false, topLeft = Offset(0f, size.height - 20f), size = androidx.compose.ui.geometry.Size(20f, 20f), style = Stroke(1f))
        }

        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("⭐", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text("SEASON", color = Color(0xFF8899BB), fontSize = 8.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text(season, color = Color(0xFF4CC9F0), fontSize = 13.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

// ═══════════════════════════════════════
// FROSTED GLASS FRIENDS PANEL
// ═══════════════════════════════════════

@Composable
fun FrostedFriendsPanel(
    onlineCount: Int,
    friends: List<OnlineFriendData>,
    onInvite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "friends")
    val sparkleAlpha by infiniteTransition.animateFloat(
        0.3f, 0.8f,
        infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = "sparkle"
    )

    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = Color(0xFF4CC9F0).copy(alpha = 0.1f))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF1A2040).copy(alpha = 0.45f),
                        Color(0xFF151830).copy(alpha = 0.5f),
                        Color(0xFF1A2040).copy(alpha = 0.4f)
                    )
                ),
                RoundedCornerShape(14.dp)
            )
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
    ) {
        // Top subtle highlight line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(listOf(Color.Transparent, Color.White.copy(alpha = 0.06f), Color.Transparent))
                )
        )

        Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("FRIENDS", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Text("$onlineCount online", color = GameColors.Green, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (friends.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Grid-style friend icon box
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .background(
                                    Brush.radialGradient(
                                        listOf(Color(0xFF4361EE).copy(alpha = 0.15f), Color.Transparent)
                                    ),
                                    RoundedCornerShape(12.dp)
                                )
                                .border(1.dp, Color(0xFF4CC9F0).copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            // Grid dots
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                for (x in 0..4) {
                                    for (y in 0..4) {
                                        drawCircle(
                                            Color(0xFF4CC9F0).copy(alpha = 0.15f),
                                            radius = 1f,
                                            center = Offset(
                                                size.width * 0.2f + x * size.width * 0.15f,
                                                size.height * 0.2f + y * size.height * 0.15f
                                            )
                                        )
                                    }
                                }
                            }
                            Icon(
                                Icons.Default.Group,
                                null,
                                tint = Color(0xFF4CC9F0).copy(alpha = 0.7f),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Add friends", color = Color(0xFF8899BB), fontSize = 12.sp)
                    }
                }
            } else {
                androidx.compose.foundation.lazy.LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(friends.size) { i ->
                        FriendItemRow(friends[i]) { onInvite(friends[i].userId) }
                    }
                }
            }
        }

        // Bottom right diamond sparkle
        Canvas(
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.BottomEnd)
                .offset(x = (-12).dp, y = (-12).dp)
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            // 4-pointed star
            drawLine(Color.White.copy(alpha = sparkleAlpha * 0.6f), Offset(center.x, center.y - 8f), Offset(center.x, center.y + 8f), strokeWidth = 1f)
            drawLine(Color.White.copy(alpha = sparkleAlpha * 0.6f), Offset(center.x - 8f, center.y), Offset(center.x + 8f, center.y), strokeWidth = 1f)
            drawLine(Color.White.copy(alpha = sparkleAlpha * 0.3f), Offset(center.x - 5f, center.y - 5f), Offset(center.x + 5f, center.y + 5f), strokeWidth = 0.5f)
            drawLine(Color.White.copy(alpha = sparkleAlpha * 0.3f), Offset(center.x + 5f, center.y - 5f), Offset(center.x - 5f, center.y + 5f), strokeWidth = 0.5f)
        }
    }
}

@Composable
private fun FriendItemRow(friend: OnlineFriendData, onInvite: () -> Unit) {
    val statusColor = when {
        friend.status == "In Game" -> GameColors.Gold
        friend.status == "In Lobby" -> GameColors.Cyan
        friend.isOnline -> GameColors.Green
        else -> GameColors.Offline
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.03f), RoundedCornerShape(8.dp))
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        com.nuno.app.core.designsystem.components.GameAvatar(username = friend.username, size = 26.dp, borderColor = statusColor)
        Spacer(modifier = Modifier.width(6.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(friend.username, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(if (friend.isOnline) friend.status else "Offline", color = statusColor, fontSize = 8.sp)
        }
        if (friend.isOnline && friend.status != "In Game") {
            Box(modifier = Modifier.size(22.dp).background(GameColors.Green, CircleShape).clickable { onInvite() }, contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(12.dp))
            }
        }
    }
}

// ═══════════════════════════════════════
// PREMIUM AVATAR WITH ENERGY RING
// ═══════════════════════════════════════

@Composable
fun PremiumAvatar(username: String, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "avatar")
    val ringRotation by infiniteTransition.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(8000, easing = LinearEasing)),
        label = "ring"
    )
    val glowPulse by infiniteTransition.animateFloat(
        0.4f, 0.9f,
        infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = "glow"
    )

    Box(modifier = modifier.size(72.dp), contentAlignment = Alignment.Center) {
        // Outer energy ring
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            drawCircle(
                Brush.radialGradient(
                    listOf(Color(0xFF4CC9F0).copy(alpha = glowPulse * 0.3f), Color.Transparent),
                    center = center, radius = size.width * 0.55f
                ),
                radius = size.width * 0.55f, center = center
            )
            drawCircle(Color(0xFF4CC9F0).copy(alpha = glowPulse * 0.7f), radius = size.width * 0.48f, center = center, style = Stroke(2f))
            drawCircle(Color(0xFF9D4EDD).copy(alpha = glowPulse * 0.4f), radius = size.width * 0.45f, center = center, style = Stroke(1f))

            // Orbiting sparkles
            for (i in 0..3) {
                val angle = Math.toRadians((ringRotation + i * 90).toDouble())
                val px = center.x + (kotlin.math.cos(angle) * size.width * 0.47f).toFloat()
                val py = center.y + (kotlin.math.sin(angle) * size.height * 0.47f).toFloat()
                drawCircle(Color(0xFF4CC9F0).copy(alpha = 0.8f), radius = 2f, center = Offset(px, py))
                drawCircle(Color(0xFF4CC9F0).copy(alpha = 0.2f), radius = 5f, center = Offset(px, py))
            }
        }

        // Purple avatar circle
        Box(
            modifier = Modifier
                .size(52.dp)
                .shadow(8.dp, CircleShape, spotColor = Color(0xFF7B4FFF))
                .background(
                    Brush.radialGradient(listOf(Color(0xFF7B4FFF), Color(0xFF4A2FCC))),
                    CircleShape
                )
                .border(3.dp, Color(0xFF9D7BFF).copy(alpha = 0.6f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                username.firstOrNull()?.uppercase() ?: "?",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

// ═══════════════════════════════════════
// GLASS SETTINGS BUTTON
// ═══════════════════════════════════════

@Composable
fun MetallicSettingsButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF2A3055), Color(0xFF1A2040))),
                RoundedCornerShape(12.dp)
            )
            .border(1.dp, Color(0xFF4A5580).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Settings, null, tint = Color(0xFF8899BB), modifier = Modifier.size(18.dp))
    }
}

// ═══════════════════════════════════════
// GLASS NAV BAR
// ═══════════════════════════════════════

@Composable
fun PremiumNavBar(selectedRoute: String, onNavigate: (String) -> Unit, modifier: Modifier = Modifier) {
    val items = listOf(
        Triple("Home", Icons.Default.Home, "home"),
        Triple("Friends", Icons.Default.Group, "friends"),
        Triple("Leaderboard", Icons.Default.EmojiEvents, "leaderboard"),
        Triple("Shop", Icons.Default.ShoppingCart, "store"),
        Triple("Profile", Icons.Default.Person, "profile")
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0D1027).copy(alpha = 0.97f), Color(0xFF080B1A))
                )
            )
            .border(
                1.dp,
                Brush.verticalGradient(listOf(Color(0xFF2A3055).copy(alpha = 0.5f), Color.Transparent)),
                RoundedCornerShape(0.dp)
            )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { (label, icon, route) ->
                val isSelected = selectedRoute == route
                val color = if (isSelected) GameColors.Gold else Color(0xFF5A607F)

                Column(
                    modifier = Modifier.weight(1f).clickable { onNavigate(route) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(width = 28.dp, height = 3.dp)
                                .background(
                                    Brush.horizontalGradient(listOf(Color.Transparent, GameColors.Gold, Color.Transparent)),
                                    RoundedCornerShape(2.dp)
                                )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                    Icon(icon, null, tint = color, modifier = Modifier.size(if (isSelected) 24.dp else 20.dp))
                    Text(label, color = color, fontSize = 9.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                }
            }
        }
    }
}