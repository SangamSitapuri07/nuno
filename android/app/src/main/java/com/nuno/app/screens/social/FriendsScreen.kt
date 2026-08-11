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
    val roomCode: String? = null,
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
    onRemoveFriend: (String) -> Unit = {},
    onSearch: (String) -> Unit = {},
    onSendFriendRequest: (String) -> Unit = {},
    onNavigate: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var activeDmFriend by remember { mutableStateOf<FriendData?>(null) }

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
                                    onJoin = { onJoin(friend.roomCode ?: "") },
                                    onChat = { activeDmFriend = friend },
                                    onRemove = { onRemoveFriend(friend.userId) }
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

        activeDmFriend?.let { friend ->
            DirectMessageDialog(
                friend = friend,
                onDismiss = { activeDmFriend = null }
            )
        }
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
private fun FriendCard(
    friend: FriendData,
    onInvite: () -> Unit,
    onJoin: () -> Unit,
    onChat: () -> Unit,
    onRemove: () -> Unit
) {
    val statusColor = when (friend.status) {
        "Online" -> GameColors.Online
        "In Game" -> GameColors.InGame
        "In Lobby" -> GameColors.InLobby
        else -> GameColors.Offline
    }

    GamePanel(
        borderColor = statusColor.copy(alpha = 0.3f),
        modifier = Modifier.clickable { onChat() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(GameDimens.paddingMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GameAvatar(username = friend.username, size = 40.dp, borderColor = statusColor, isOnline = friend.isOnline)
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onChat() }
            ) {
                Text(friend.username, color = GameColors.TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(friend.status, color = statusColor, fontSize = 11.sp)
            }
            Text("🏆 ${friend.rating}", color = GameColors.Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onChat, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.AutoMirrored.Filled.Chat, null, tint = GameColors.Cyan, modifier = Modifier.size(18.dp))
                }
                when (friend.status) {
                    "In Lobby" -> GameButton("JOIN", onClick = onJoin, style = ButtonStyle.GREEN, height = 30.dp)
                    "Online" -> GameButton("INVITE", onClick = onInvite, style = ButtonStyle.PRIMARY, height = 30.dp)
                    else -> {}
                }
                IconButton(onClick = onRemove, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.Default.Delete, null, tint = GameColors.Red.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
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
fun DirectMessageDialog(
    friend: FriendData,
    onDismiss: () -> Unit
) {
    val activity = androidx.compose.ui.platform.LocalContext.current as? androidx.activity.ComponentActivity
    val friendsVm = activity?.let { androidx.hilt.navigation.compose.hiltViewModel<com.nuno.app.features.friends.FriendsViewModel>(it) }
    val dmsMap by friendsVm?.dmsState?.collectAsState() ?: remember { mutableStateOf(emptyMap()) }
    val friendMessages = dmsMap[friend.userId] ?: emptyList()

    var textInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                GameAvatar(username = friend.username, size = 36.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(friend.username, color = GameColors.TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(friend.status, color = GameColors.Cyan, fontSize = 11.sp)
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth().height(260.dp)) {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (friendMessages.isEmpty()) {
                        item {
                            Text(
                                "Start a conversation with ${friend.username}!",
                                color = GameColors.TextGray,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 24.dp)
                            )
                        }
                    } else {
                        items(friendMessages.size) { i ->
                            val msg = friendMessages[i]
                            val isMe = msg.isMe
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
                            ) {
                                Column(horizontalAlignment = if (isMe) Alignment.End else Alignment.Start) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                if (isMe) GameColors.Blue else GameColors.Surface,
                                                RoundedCornerShape(12.dp)
                                            )
                                            .padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Text(msg.message, color = Color.White, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    GameTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = "Type message...",
                        modifier = Modifier.weight(1f),
                        height = 40.dp
                    )
                    GameButton(
                        text = "SEND",
                        onClick = {
                            if (textInput.isNotBlank()) {
                                friendsVm?.sendDm(friend.userId, textInput)
                                textInput = ""
                            }
                        },
                        style = ButtonStyle.GREEN,
                        height = 40.dp
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close", color = GameColors.TextWhite) }
        }
    )
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