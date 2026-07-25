package com.nuno.app.screens.social

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.*

data class FriendData(
    val userId: String,
    val username: String,
    val status: String,
    val rating: Int = 0,
    val isOnline: Boolean = false
)

data class FriendRequestData(
    val requestId: String,
    val username: String
)

data class SearchPlayerData(
    val userId: String,
    val username: String,
    val rating: Int = 0
)

@Composable
fun FriendsScreen(
    friends: List<FriendData>,
    requests: List<FriendRequestData>,
    searchResults: List<SearchPlayerData> = emptyList(),
    onBack: () -> Unit,
    onInvite: (String) -> Unit,
    onJoin: (String) -> Unit,
    onAcceptRequest: (String) -> Unit,
    onRejectRequest: (String) -> Unit,
    onSearch: (String) -> Unit = {},
    onSendFriendRequest: (String) -> Unit = {},
    onNavigate: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = GameDimens.bottomNavHeight)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = GameDimens.paddingLg, vertical = GameDimens.paddingMd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GameColors.TextWhite)
                }
                Text(
                    "FRIENDS",
                    color = GameColors.TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
            }

            // Tabs
            Row(
                modifier = Modifier.padding(horizontal = GameDimens.paddingXl),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FriendTab("ALL", selectedTab == 0) { selectedTab = 0 }
                FriendTab("FRIENDS", selectedTab == 1) { selectedTab = 1 }
                FriendTab("REQUESTS", selectedTab == 2, badge = requests.size) { selectedTab = 2 }
                FriendTab("ADD", selectedTab == 3) { selectedTab = 3 }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            when (selectedTab) {
                0, 1 -> {
                    // Search within friends
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = GameDimens.paddingXl, vertical = GameDimens.paddingSm),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GameTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = "Search friends...",
                            leadingIcon = {
                                Icon(Icons.Default.Search, null, tint = GameColors.TextGray, modifier = Modifier.size(18.dp))
                            },
                            modifier = Modifier.weight(1f),
                            height = 44.dp
                        )
                    }

                    val filtered = if (searchQuery.isBlank()) friends
                    else friends.filter { it.username.contains(searchQuery, true) }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = GameDimens.paddingXl),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (filtered.isEmpty()) {
                            item {
                                EmptyState("👥", "No friends yet", "Add friends from the ADD tab")
                            }
                        } else {
                            items(filtered) { friend ->
                                FriendCard(
                                    friend = friend,
                                    onInvite = { onInvite(friend.userId) },
                                    onJoin = { onJoin(friend.userId) },
                                    onChat = { }
                                )
                            }
                        }
                    }
                }

                2 -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = GameDimens.paddingXl),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (requests.isEmpty()) {
                            item {
                                EmptyState("📬", "No requests", "Friend requests will appear here")
                            }
                        } else {
                            items(requests) { request ->
                                RequestCard(
                                    request = request,
                                    onAccept = { onAcceptRequest(request.requestId) },
                                    onReject = { onRejectRequest(request.requestId) }
                                )
                            }
                        }
                    }
                }

                3 -> {
                    // ADD FRIENDS - Search by username
                    AddFriendsTab(
                        searchResults = searchResults,
                        onSearch = onSearch,
                        onSendRequest = onSendFriendRequest
                    )
                }
            }
        }

        BottomNavBar(
            selectedRoute = "friends",
            onNavigate = onNavigate,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun AddFriendsTab(
    searchResults: List<SearchPlayerData>,
    onSearch: (String) -> Unit,
    onSendRequest: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = GameDimens.paddingXl)
    ) {
        Text("FIND PLAYERS", color = GameColors.Gold, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GameTextField(
                value = query,
                onValueChange = {
                    query = it
                    if (it.length >= 2) onSearch(it)
                },
                placeholder = "Search by username...",
                leadingIcon = {
                    Icon(Icons.Default.Search, null, tint = GameColors.Cyan, modifier = Modifier.size(18.dp))
                },
                modifier = Modifier.weight(1f),
                height = 44.dp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (query.length < 2) {
            Text("Type at least 2 characters to search", color = GameColors.TextDark, fontSize = 12.sp)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                if (searchResults.isEmpty()) {
                    item {
                        Text("No players found", color = GameColors.TextGray, fontSize = 13.sp)
                    }
                } else {
                    items(searchResults) { player ->
                        SearchResultCard(
                            player = player,
                            onSendRequest = { onSendRequest(player.userId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultCard(player: SearchPlayerData, onSendRequest: () -> Unit) {
    GamePanel(borderColor = GameColors.BorderPurple.copy(alpha = 0.3f)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(GameDimens.paddingMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GameAvatar(username = player.username, size = 40.dp, borderColor = GameColors.Blue)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(player.username, color = GameColors.TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("Rating: ${player.rating}", color = GameColors.Gold, fontSize = 11.sp)
            }
            GameButton(
                text = "ADD",
                onClick = onSendRequest,
                style = ButtonStyle.GREEN,
                height = 32.dp
            )
        }
    }
}

@Composable
private fun FriendTab(label: String, isSelected: Boolean, badge: Int = 0, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) GameColors.Blue else GameColors.Surface,
                RoundedCornerShape(GameDimens.radiusFull)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                label,
                color = if (isSelected) GameColors.TextWhite else GameColors.TextGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            if (badge > 0) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier.background(GameColors.Red, CircleShape).padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(badge.toString(), color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun FriendCard(friend: FriendData, onInvite: () -> Unit, onJoin: () -> Unit, onChat: () -> Unit) {
    val statusColor = when (friend.status) {
        "Online" -> GameColors.Online
        "In Game" -> GameColors.InGame
        "In Lobby" -> GameColors.InLobby
        else -> GameColors.Offline
    }

    GamePanel(borderColor = statusColor.copy(alpha = 0.3f)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(GameDimens.paddingMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GameAvatar(username = friend.username, size = 40.dp, borderColor = statusColor, isOnline = friend.isOnline)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(friend.username, color = GameColors.TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(friend.status, color = statusColor, fontSize = 11.sp)
            }
            Text("🏆 ${friend.rating}", color = GameColors.Gold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(12.dp))
            when (friend.status) {
                "In Lobby" -> GameButton("JOIN", onClick = onJoin, style = ButtonStyle.GREEN, height = 32.dp)
                "Online" -> GameButton("INVITE", onClick = onInvite, style = ButtonStyle.PRIMARY, height = 32.dp)
                else -> {
                    IconButton(onClick = onChat, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.AutoMirrored.Filled.Chat, null, tint = GameColors.Blue)
                    }
                }
            }
        }
    }
}

@Composable
private fun RequestCard(request: FriendRequestData, onAccept: () -> Unit, onReject: () -> Unit) {
    GamePanel(borderColor = GameColors.Gold.copy(alpha = 0.3f)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(GameDimens.paddingMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GameAvatar(username = request.username, size = 40.dp, borderColor = GameColors.Gold)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(request.username, color = GameColors.TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("Wants to be your friend", color = GameColors.TextGray, fontSize = 11.sp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GameButton("ACCEPT", onClick = onAccept, style = ButtonStyle.GREEN, height = 32.dp)
                GameButton("REJECT", onClick = onReject, style = ButtonStyle.DANGER, height = 32.dp)
            }
        }
    }
}

@Composable
private fun EmptyState(icon: String, title: String, subtitle: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, fontSize = 48.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, color = GameColors.TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, color = GameColors.TextGray, fontSize = 12.sp)
    }
}