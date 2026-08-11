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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors

@Composable
fun MetallicCurrencyPill(
    icon: String,
    value: String,
    color: Color,
    onAdd: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(16.dp), spotColor = Color(0xFF1A2040))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF2A3055), Color(0xFF1A2040), Color(0xFF252D50))
                ),
                RoundedCornerShape(16.dp)
            )
            .border(1.dp, Color(0xFF4A5580).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 13.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(Color.White.copy(alpha = 0.15f), CircleShape)
                    .clickable { onAdd() },
                contentAlignment = Alignment.Center
            ) {
                Text("+", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
fun TopLeftProfilePill(username: String, level: Int, rank: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(18.dp), spotColor = Color(0xFF1A2040))
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF182042).copy(alpha = 0.95f), Color(0xFF0F142D).copy(alpha = 0.95f))
                ),
                RoundedCornerShape(18.dp)
            )
            .border(1.5.dp, Color(0xFF384570), RoundedCornerShape(18.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        Brush.radialGradient(listOf(Color(0xFF6C38FF), Color(0xFF3A1C99))),
                        CircleShape
                    )
                    .border(1.5.dp, Color(0xFFCD7F32), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    username.firstOrNull()?.uppercase() ?: "S",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(username, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Lv. $level", color = Color(0xFF4CC9F0), fontSize = 8.5.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .width(28.dp)
                            .height(3.dp)
                            .background(Color(0xFF1A2040), RoundedCornerShape(2.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.4f)
                                .background(Color(0xFF4CC9F0), RoundedCornerShape(2.dp))
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFCD7F32).copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                            .border(0.8.dp, Color(0xFFCD7F32), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🛡️", fontSize = 7.sp)
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(rank, color = Color(0xFFFFB300), fontSize = 7.5.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FrostedFriendsPanel(
    onlineCount: Int,
    friends: List<OnlineFriendData>,
    onFriendClick: (OnlineFriendData) -> Unit,
    onInvite: (String) -> Unit,
    onAddFriendClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(14.dp), spotColor = Color(0xFF1A2040))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF161C38).copy(alpha = 0.92f),
                        Color(0xFF0F142D).copy(alpha = 0.95f)
                    )
                ),
                RoundedCornerShape(14.dp)
            )
            .border(1.2.dp, Color(0xFF2E3A60), RoundedCornerShape(14.dp))
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Row: FRIENDS + Functional Add Friend icon + Search icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("👥", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("FRIENDS", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "👤+",
                        color = Color(0xFF4CC9F0),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onAddFriendClick() }
                    )
                    Text(
                        "🔍",
                        color = if (isSearchActive) Color(0xFFFFD700) else Color(0xFF8899BB),
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { isSearchActive = !isSearchActive }
                    )
                }
            }

            if (isSearchActive) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F142D), RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFF4CC9F0).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    androidx.compose.material3.OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search friends...", color = Color(0xFF667799), fontSize = 9.sp) },
                        singleLine = true,
                        modifier = Modifier.weight(1f).height(32.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 9.5.sp)
                    )
                    if (searchQuery.isNotEmpty()) {
                        Text(
                            "✕",
                            color = Color.Gray,
                            fontSize = 10.sp,
                            modifier = Modifier.clickable { searchQuery = "" }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Section Header: ONLINE
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("●", color = GameColors.Green, fontSize = 8.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text("ONLINE ($onlineCount)", color = GameColors.Green, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Friend Row List (Scrollable)
            val baseFriends = if (friends.isNotEmpty()) friends else listOf(
                OnlineFriendData("1", "Trexy5", "Online", true)
            )

            val filteredFriends = if (searchQuery.isBlank()) baseFriends
            else baseFriends.filter { it.username.contains(searchQuery, ignoreCase = true) }

            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(filteredFriends.size) { i ->
                    val friend = filteredFriends[i]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0D1226).copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                            .clickable { onFriendClick(friend) }
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            com.nuno.app.core.designsystem.components.GameAvatar(
                                username = friend.username,
                                size = 26.dp,
                                borderColor = GameColors.Green
                            )
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(GameColors.Green, CircleShape)
                                    .align(Alignment.BottomEnd)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(friend.username, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text("Online", color = GameColors.Green, fontSize = 8.sp)
                        }

                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .shadow(4.dp, RoundedCornerShape(6.dp), spotColor = Color(0xFF4CAF50))
                                .background(
                                    Brush.verticalGradient(listOf(Color(0xFF66BB6A), Color(0xFF2E7D32))),
                                    RoundedCornerShape(6.dp)
                                )
                                .border(1.dp, Color(0xFFA5D6A7), RoundedCornerShape(6.dp))
                                .clickable { onInvite(friend.userId) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Section Header: OFFLINE
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("●", color = Color(0xFF667799), fontSize = 8.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("OFFLINE (0)", color = Color(0xFF667799), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
                Text("˅", color = Color(0xFF667799), fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun MetallicSettingsButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .shadow(4.dp, RoundedCornerShape(10.dp))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF2A3055), Color(0xFF1A2040))),
                RoundedCornerShape(10.dp)
            )
            .border(1.dp, Color(0xFF4A5580).copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Settings, null, tint = Color(0xFF8899BB), modifier = Modifier.size(16.dp))
    }
}

@Composable
fun PremiumNavBar(selectedRoute: String, onNavigate: (String) -> Unit, modifier: Modifier = Modifier) {
    val items = listOf(
        Triple("HOME", Icons.Default.Home, "home"),
        Triple("RANK", Icons.Default.EmojiEvents, "leaderboard"),
        Triple("SHOP", Icons.Default.ShoppingCart, "store"),
        Triple("PROFILE", Icons.Default.Person, "profile")
    )

    Box(
        modifier = modifier
            .height(48.dp)
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF121833).copy(alpha = 0.95f), Color(0xFF0A0E24))
                ),
                RoundedCornerShape(topEnd = 16.dp)
            )
            .border(
                1.dp,
                Color(0xFF2A3660),
                RoundedCornerShape(topEnd = 16.dp)
            )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { (label, icon, route) ->
                val isSelected = selectedRoute == route
                val color = if (isSelected) Color.White else Color(0xFF667799)

                Column(
                    modifier = Modifier.weight(1f).clickable { onNavigate(route) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(label, color = color, fontSize = 8.sp, fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold)

                    if (isSelected) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .size(width = 28.dp, height = 2.5.dp)
                                .background(Color(0xFF1E88E5), RoundedCornerShape(2.dp))
                                .shadow(4.dp, RoundedCornerShape(2.dp), spotColor = Color(0xFF1E88E5))
                        )
                    }
                }
            }
        }
    }
}