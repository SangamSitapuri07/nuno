package com.nuno.app.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.components.GameAvatar

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
            .shadow(8.dp, RoundedCornerShape(22.dp), spotColor = color.copy(0.25f))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF252A52), Color(0xFF181B3A))
                ),
                RoundedCornerShape(22.dp)
            )
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(Color.White.copy(0.18f), Color.Transparent)
                ),
                RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(
                        Brush.radialGradient(listOf(color, color.copy(0.7f))),
                        RoundedCornerShape(6.dp)
                    )
                    .border(0.5.dp, Color.White.copy(0.5f), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 10.sp)
            }
            Spacer(modifier = Modifier.width(7.dp))
            Text(value, color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.Black, letterSpacing = 0.2.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(Color.White.copy(alpha = 0.10f), CircleShape)
                    .border(1.dp, Color.White.copy(0.15f), CircleShape)
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
            .shadow(14.dp, RoundedCornerShape(22.dp), spotColor = Color.Black.copy(0.6f))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1C2148).copy(0.96f), Color(0xFF121531).copy(0.98f))
                ),
                RoundedCornerShape(22.dp)
            )
            .border(
                1.dp,
                Brush.linearGradient(listOf(Color.White.copy(0.16f), Color.White.copy(0.04f))),
                RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .shadow(6.dp, CircleShape, spotColor = GameColors.Purple.copy(0.5f))
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF7B5CFF), Color(0xFF3A2A8A))
                        ),
                        CircleShape
                    )
                    .border(1.5.dp, Color(0xFFFFC71F).copy(0.9f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    username.firstOrNull()?.uppercase() ?: "S",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(username, color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.2.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Lv. $level", color = GameColors.Cyan, fontSize = 9.sp, fontWeight = FontWeight.Black)
                    Spacer(modifier = Modifier.width(5.dp))
                    Box(
                        modifier = Modifier
                            .width(34.dp)
                            .height(4.dp)
                            .background(Color(0xFF252A52), RoundedCornerShape(2.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.42f)
                                .background(
                                    Brush.horizontalGradient(listOf(GameColors.Cyan, GameColors.Blue)),
                                    RoundedCornerShape(2.dp)
                                )
                        )
                    }
                    Spacer(modifier = Modifier.width(7.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFC71F).copy(0.14f), RoundedCornerShape(6.dp))
                            .border(0.8.dp, Color(0xFFFFC71F).copy(0.5f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("◆", fontSize = 7.sp, color = GameColors.Gold)
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(rank.uppercase(), color = GameColors.Gold, fontSize = 7.5.sp, fontWeight = FontWeight.Black, letterSpacing = 0.5.sp)
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
            .shadow(20.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(0.6f))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF1A1F4A).copy(alpha = 0.92f),
                        Color(0xFF111433).copy(alpha = 0.96f)
                    )
                ),
                RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(Color.White.copy(0.14f), Color.White.copy(0.04f))
                ),
                RoundedCornerShape(20.dp)
            )
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                Brush.linearGradient(listOf(Color(0xFF7B5CFF), Color(0xFF4A6BFF))),
                                RoundedCornerShape(6.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👥", fontSize = 10.sp)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "FRIENDS",
                        color = Color.White,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(GameColors.Green.copy(0.15f), RoundedCornerShape(6.dp))
                            .border(1.dp, GameColors.Green.copy(0.3f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text("$onlineCount", color = GameColors.Green, fontSize = 9.sp, fontWeight = FontWeight.Black)
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .background(Color.White.copy(0.08f), RoundedCornerShape(8.dp))
                            .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(8.dp))
                            .clickable { onAddFriendClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("＋", color = GameColors.Cyan, fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .background(
                                if (isSearchActive) GameColors.Gold.copy(0.15f) else Color.White.copy(0.06f),
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                1.dp,
                                if (isSearchActive) GameColors.Gold.copy(0.5f) else Color.White.copy(0.08f),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { isSearchActive = !isSearchActive },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⌕", color = if (isSearchActive) GameColors.Gold else Color(0xFF8B92C0), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (isSearchActive) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0E1130), RoundedCornerShape(10.dp))
                        .border(1.dp, GameColors.Cyan.copy(0.35f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    androidx.compose.material3.TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search friends...", color = Color(0xFF5A6488), fontSize = 10.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 11.sp),
                        colors = androidx.compose.material3.TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(GameColors.Green, CircleShape)
                        .shadow(4.dp, CircleShape, spotColor = GameColors.Green)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("ONLINE", color = GameColors.Green, fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text("($onlineCount)", color = Color(0xFF5A6488), fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(6.dp))

            val baseFriends = if (friends.isNotEmpty()) friends else listOf(
                OnlineFriendData("1", "Trexy5", "Online", true)
            )
            val filteredFriends = if (searchQuery.isBlank()) baseFriends
            else baseFriends.filter { it.username.contains(searchQuery, ignoreCase = true) }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(filteredFriends.size) { i ->
                    val friend = filteredFriends[i]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF1E2249).copy(0.8f), Color(0xFF161A38).copy(0.9f))
                                ),
                                RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.White.copy(0.06f), RoundedCornerShape(12.dp))
                            .clickable { onFriendClick(friend) }
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            GameAvatar(
                                username = friend.username,
                                size = 30.dp,
                                borderColor = GameColors.Green,
                                isOnline = false,
                                showGlow = false
                            )
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .background(Color(0xFF0A0C22), CircleShape)
                                    .align(Alignment.BottomEnd)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .background(GameColors.Green, CircleShape)
                                        .align(Alignment.Center)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(9.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(friend.username, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("Online • In lobby", color = GameColors.Green, fontSize = 8.5.sp, fontWeight = FontWeight.Medium)
                        }
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .shadow(6.dp, RoundedCornerShape(8.dp), spotColor = GameColors.Green.copy(0.4f))
                                .background(
                                    Brush.verticalGradient(listOf(Color(0xFF2ECC71), Color(0xFF27AE60))),
                                    RoundedCornerShape(8.dp)
                                )
                                .border(1.dp, Color.White.copy(0.25f), RoundedCornerShape(8.dp))
                                .clickable { onInvite(friend.userId) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0E1130).copy(0.6f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(5.dp).background(Color(0xFF5A6488), CircleShape))
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("OFFLINE", color = Color(0xFF5A6488), fontSize = 8.5.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("(0)", color = Color(0xFF3A425F), fontSize = 8.5.sp)
                }
                Text("⌄", color = Color(0xFF5A6488), fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun MetallicSettingsButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp), spotColor = Color.Black.copy(0.5f))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF252A52), Color(0xFF181B3A))),
                RoundedCornerShape(12.dp)
            )
            .border(1.dp, Color.White.copy(0.10f), RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Default.Settings, null, tint = Color(0xFF8B92C0), modifier = Modifier.size(18.dp))
    }
}

@Composable
fun PremiumNavBar(selectedRoute: String, onNavigate: (String) -> Unit, modifier: Modifier = Modifier) {
    val items: List<Triple<String, ImageVector, String>> = listOf(
        Triple("HOME", Icons.Filled.Home, "home"),
        Triple("RANK", Icons.Filled.EmojiEvents, "leaderboard"),
        Triple("SHOP", Icons.Filled.ShoppingCart, "store"),
        Triple("PROFILE", Icons.Filled.Person, "profile")
    )

    Box(
        modifier = modifier
            .height(56.dp)
            .shadow(24.dp, RoundedCornerShape(topEnd = 20.dp, bottomEnd = 0.dp, topStart = 0.dp, bottomStart = 0.dp), spotColor = Color.Black.copy(0.8f))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF161A3A).copy(0.97f), Color(0xFF0D0F28).copy(0.98f))
                ),
                RoundedCornerShape(topEnd = 20.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(listOf(Color.White.copy(0.12f), Color.Transparent)),
                shape = RoundedCornerShape(topEnd = 20.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Brush.horizontalGradient(listOf(Color.Transparent, Color.White.copy(0.10f), Color.Transparent)))
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for ((label, icon, route) in items) {
                val isSelected = selectedRoute == route

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigate(route) }
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 28.dp else 24.dp)
                            .background(
                                if (isSelected) Brush.linearGradient(listOf(Color(0xFF4A6BFF), Color(0xFF7B5CFF)))
                                else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
                                CircleShape
                            )
                            .border(
                                if (isSelected) 1.dp else 0.dp,
                                Color.White.copy(0.2f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            icon,
                            null,
                            tint = if (isSelected) Color.White else Color(0xFF5A6488),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        label,
                        color = if (isSelected) Color.White else Color(0xFF5A6488),
                        fontSize = 8.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    if (isSelected) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Box(
                            modifier = Modifier
                                .size(width = 20.dp, height = 2.dp)
                                .background(Color(0xFF4A6BFF), RoundedCornerShape(2.dp))
                                .shadow(4.dp, RoundedCornerShape(2.dp), spotColor = Color(0xFF4A6BFF))
                        )
                    }
                }
            }
        }
    }
}
