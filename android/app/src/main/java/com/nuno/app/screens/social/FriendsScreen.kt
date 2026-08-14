package com.nuno.app.screens.social

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.*
import com.nuno.app.screens.home.PremiumGameTableBackground

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
        PremiumGameTableBackground()

        Column(modifier = Modifier.fillMaxSize().statusBarsPadding().padding(bottom = GameDimens.bottomNavHeight)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(0.08f), RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White.copy(0.12f), RoundedCornerShape(12.dp))
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("FRIENDS", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .background(GameColors.Green.copy(0.15f), RoundedCornerShape(12.dp))
                        .border(1.dp, GameColors.Green.copy(0.3f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("${friends.count { it.isOnline }} online", color = GameColors.Green, fontSize = 10.sp, fontWeight = FontWeight.Black)
                }
            }

            Row(modifier = Modifier.padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PremiumFriendTab("ALL", selectedTab == 0) { selectedTab = 0 }
                PremiumFriendTab("FRIENDS (${friends.size})", selectedTab == 1) { selectedTab = 1 }
                PremiumFriendTab("REQUESTS", selectedTab == 2, badge = requests.size) { selectedTab = 2 }
                PremiumFriendTab("ADD", selectedTab == 3) { selectedTab = 3 }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0, 1 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .background(Color(0xFF0E1130).copy(0.8f), RoundedCornerShape(14.dp))
                            .border(1.dp, Color.White.copy(0.06f), RoundedCornerShape(14.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Search, null, tint = Color(0xFF5A6488), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            androidx.compose.material3.TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Search friends...", color = Color(0xFF5A6488), fontSize = 12.sp) },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 13.sp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val filtered = if (searchQuery.isBlank()) friends else friends.filter { it.username.contains(searchQuery, true) }

                    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 16.dp)) {
                        if (filtered.isEmpty()) {
                            item { PremiumEmptyState("👥", "No friends yet", "Add friends from the ADD tab") }
                        } else {
                            items(filtered) { friend ->
                                PremiumFriendCard(
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
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (requests.isEmpty()) {
                            item { PremiumEmptyState("📬", "No requests", "Friend requests will appear here") }
                        } else {
                            items(requests) { request ->
                                PremiumRequestCard(request, onAccept = { onAcceptRequest(request.requestId) }, onReject = { onRejectRequest(request.requestId) })
                            }
                        }
                    }
                }

                3 -> {
                    AddFriendsTabPremium(searchResults, onSearch, onSendFriendRequest)
                }
            }
        }

        BottomNavBar(selectedRoute = "friends", onNavigate = onNavigate, modifier = Modifier.align(Alignment.BottomCenter))

        activeDmFriend?.let { friend ->
            DirectMessageDialog(friend = friend, onDismiss = { activeDmFriend = null })
        }
    }
}

@Composable
private fun PremiumFriendTab(label: String, isSelected: Boolean, badge: Int = 0, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .shadow(if (isSelected) 8.dp else 0.dp, RoundedCornerShape(20.dp), spotColor = GameColors.Blue.copy(0.4f))
            .background(
                if (isSelected) Brush.linearGradient(listOf(Color(0xFF3B6BFF), Color(0xFF7B5CFF))) else Brush.linearGradient(listOf(Color(0xFF1E2249), Color(0xFF131636))),
                RoundedCornerShape(20.dp)
            )
            .border(1.dp, if (isSelected) Color.White.copy(0.2f) else Color.White.copy(0.06f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, color = if (isSelected) Color.White else Color(0xFF8B92C0), fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold, letterSpacing = 0.5.sp)
            if (badge > 0) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(modifier = Modifier.background(GameColors.Red, CircleShape).padding(horizontal = 6.dp, vertical = 2.dp)) {
                    Text(badge.toString(), color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun PremiumFriendCard(friend: FriendData, onInvite: () -> Unit, onJoin: () -> Unit, onChat: () -> Unit, onRemove: () -> Unit) {
    val statusColor = when (friend.status) {
        "Online" -> GameColors.Online
        "In Game" -> GameColors.InGame
        "In Lobby" -> GameColors.InLobby
        else -> GameColors.Offline
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFF1A1F4A).copy(0.92f), Color(0xFF131636).copy(0.96f))), RoundedCornerShape(16.dp))
            .border(1.dp, statusColor.copy(0.25f), RoundedCornerShape(16.dp))
            .clickable { onChat() }
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            GameAvatar(username = friend.username, size = 46.dp, borderColor = statusColor, isOnline = friend.isOnline)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(friend.username, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.background(statusColor.copy(0.15f), RoundedCornerShape(6.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text(friend.status.uppercase(), color = statusColor, fontSize = 8.sp, fontWeight = FontWeight.Black, letterSpacing = 0.5.sp)
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text("🏆 ${friend.rating} • Level ${friend.rating / 100 + 1}", color = GameColors.Gold, fontSize = 10.sp, fontWeight = FontWeight.Medium)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF00D9FF).copy(0.12f), CircleShape)
                        .border(1.dp, Color(0xFF00D9FF).copy(0.25f), CircleShape)
                        .clickable { onChat() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Filled.Chat, null, tint = GameColors.Cyan, modifier = Modifier.size(16.dp))
                }
                when (friend.status) {
                    "In Lobby" -> GameButton("JOIN", onClick = onJoin, style = ButtonStyle.GREEN, height = 34.dp)
                    "Online" -> GameButton("INVITE", onClick = onInvite, style = ButtonStyle.PRIMARY, height = 34.dp)
                    else -> {}
                }
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(Color(0xFFFF3B5C).copy(0.10f), CircleShape)
                        .clickable { onRemove() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Delete, null, tint = GameColors.Red.copy(0.7f), modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

@Composable
private fun PremiumRequestCard(request: FriendRequestData, onAccept: () -> Unit, onReject: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFF1E2248), Color(0xFF131636))), RoundedCornerShape(16.dp))
            .border(1.dp, GameColors.Gold.copy(0.25f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            GameAvatar(username = request.username, size = 46.dp, borderColor = GameColors.Gold)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(request.username, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("Wants to be your friend", color = Color(0xFF8B92C0), fontSize = 11.sp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GameButton("ACCEPT", onClick = onAccept, style = ButtonStyle.GREEN, height = 36.dp)
                GameButton("DECLINE", onClick = onReject, style = ButtonStyle.DANGER, height = 36.dp)
            }
        }
    }
}

@Composable
private fun AddFriendsTabPremium(searchResults: List<SearchPlayerData>, onSearch: (String) -> Unit, onSendRequest: (String) -> Unit) {
    var query by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Text("FIND PLAYERS", color = GameColors.Gold, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0E1130).copy(0.8f), RoundedCornerShape(14.dp))
                .border(1.dp, GameColors.Cyan.copy(0.3f), RoundedCornerShape(14.dp))
                .padding(horizontal = 14.dp, vertical = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Search, null, tint = GameColors.Cyan, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                TextField(
                    value = query,
                    onValueChange = {
                        query = it
                        if (it.length >= 2) onSearch(it)
                    },
                    placeholder = { Text("Search by username...", color = Color(0xFF5A6488), fontSize = 12.sp) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 13.sp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (query.length < 2) {
            PremiumEmptyState("🔍", "Search Players", "Type at least 2 characters to search for players")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (searchResults.isEmpty()) {
                    item { Text("No players found for \"$query\"", color = Color(0xFF8B92C0), fontSize = 12.sp, modifier = Modifier.padding(top = 12.dp)) }
                } else {
                    items(searchResults) { player ->
                        SearchResultCardPremium(player, onSendRequest = { onSendRequest(player.userId) })
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultCardPremium(player: SearchPlayerData, onSendRequest: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Color(0xFF1A1F4A), Color(0xFF131636))), RoundedCornerShape(14.dp))
            .border(1.dp, Color.White.copy(0.06f), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            GameAvatar(username = player.username, size = 42.dp, borderColor = GameColors.Blue)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(player.username, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("Rating: ${player.rating}", color = GameColors.Gold, fontSize = 11.sp)
            }
            GameButton(text = "ADD", onClick = onSendRequest, style = ButtonStyle.GREEN, height = 36.dp)
        }
    }
}

@Composable
fun DirectMessageDialog(friend: FriendData, onDismiss: () -> Unit) {
    val activity = androidx.compose.ui.platform.LocalContext.current as? androidx.activity.ComponentActivity
    val friendsVm = activity?.let { androidx.hilt.navigation.compose.hiltViewModel<com.nuno.app.features.friends.FriendsViewModel>(it) }
    val dmsMap by friendsVm?.dmsState?.collectAsState() ?: remember { mutableStateOf(emptyMap()) }
    val friendMessages = dmsMap[friend.userId] ?: emptyList()
    var textInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1A1F4A),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                GameAvatar(username = friend.username, size = 40.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(friend.username, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(friend.status, color = GameColors.Cyan, fontSize = 11.sp)
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth().height(280.dp)) {
                LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth().padding(vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (friendMessages.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().background(Color.White.copy(0.05f), RoundedCornerShape(12.dp)).padding(16.dp), contentAlignment = Alignment.Center) {
                                Text("Start a conversation with ${friend.username}! 💬", color = Color(0xFF8B92C0), fontSize = 12.sp)
                            }
                        }
                    } else {
                        items(friendMessages.size) { i ->
                            val msg = friendMessages[i]
                            val isMe = msg.isMe
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (isMe) Brush.linearGradient(listOf(Color(0xFF3B6BFF), Color(0xFF7B5CFF))) else Brush.linearGradient(listOf(Color(0xFF1E2249), Color(0xFF131636))),
                                            RoundedCornerShape(14.dp)
                                        )
                                        .border(1.dp, if (isMe) Color.White.copy(0.2f) else Color.White.copy(0.08f), RoundedCornerShape(14.dp))
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Text(msg.message, color = Color.White, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFF0E1130), RoundedCornerShape(12.dp))
                            .border(1.dp, Color.White.copy(0.08f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        TextField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            placeholder = { Text("Type message...", color = Color(0xFF5A6488), fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = false,
                            textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 13.sp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .shadow(8.dp, CircleShape, spotColor = GameColors.Blue.copy(0.5f))
                            .background(Brush.linearGradient(listOf(Color(0xFF3B6BFF), Color(0xFF7B5CFF))), CircleShape)
                            .clickable {
                                if (textInput.isNotBlank()) {
                                    friendsVm?.sendDm(friend.userId, textInput)
                                    textInput = ""
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Send, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close", color = Color.White) } }
    )
}

@Composable
private fun PremiumEmptyState(icon: String, title: String, subtitle: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.White.copy(0.05f), CircleShape)
                .border(1.dp, Color.White.copy(0.08f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 36.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(subtitle, color = Color(0xFF8B92C0), fontSize = 12.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}
