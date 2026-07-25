package com.nuno.app.features.friends

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.common.UiState
import com.nuno.app.core.theme.*
import com.nuno.app.core.utils.showToast
import com.nuno.app.features.friends.components.FriendCard
import com.nuno.app.features.friends.components.FriendRequestCard
import com.nuno.app.features.friends.components.SearchPlayerCard
import com.nuno.app.features.home.LeftSidebar

enum class FriendsTab(val label: String, val icon: ImageVector) {
    FRIENDS("Friends", Icons.Default.Group),
    REQUESTS("Requests", Icons.Default.Notifications),
    ADD("Add Player", Icons.Default.PersonAdd)
}

@Composable
fun FriendsScreen(
    onBack: () -> Unit,
    onNavigateToPlay: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: FriendsViewModel = hiltViewModel()
) {
    val friendsState by viewModel.friendsState.collectAsState()
    val requestsState by viewModel.requestsState.collectAsState()
    val searchState by viewModel.searchState.collectAsState()
    val actionMessage by viewModel.actionMessage.collectAsState()
    val context = LocalContext.current

    var selectedTab by remember { mutableStateOf(FriendsTab.FRIENDS) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(actionMessage) {
        actionMessage?.let {
            context.showToast(it)
            viewModel.clearMessage()
        }
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.length >= 2) viewModel.searchPlayers(searchQuery)
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(BackgroundDark, BackgroundDarker)))
    ) {
        LeftSidebar(
            username = "Player", level = 1, coins = 0,
            selectedRoute = "friends",
            onPlayClick = onNavigateToPlay,
            onProfileClick = onNavigateToProfile,
            onLeaderboardClick = onNavigateToLeaderboard,
            onStoreClick = onNavigateToShop,
            onSettingsClick = onNavigateToSettings,
            onLogout = onLogout
        )

        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            // Fixed header
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FRIENDS",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                )
            }

            // Tabs
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FriendsTab.entries.forEach { tab ->
                    TabButton(
                        tab = tab,
                        isSelected = selectedTab == tab,
                        badge = if (tab == FriendsTab.REQUESTS) (requestsState as? UiState.Success)?.data?.size else null,
                        onClick = { selectedTab = tab },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            Box(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                when (selectedTab) {
                    FriendsTab.FRIENDS -> FriendsContent(state = friendsState, onRemove = { viewModel.removeFriend(it) })
                    FriendsTab.REQUESTS -> RequestsContent(state = requestsState, onAccept = { viewModel.acceptRequest(it) }, onReject = { viewModel.rejectRequest(it) })
                    FriendsTab.ADD -> AddContent(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        state = searchState,
                        onAdd = { viewModel.sendRequest(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TabButton(
    tab: FriendsTab,
    isSelected: Boolean,
    badge: Int?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PrimaryPurple else SurfaceCard
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                tab.icon,
                null,
                tint = if (isSelected) TextPrimary else TextSecondary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = tab.label,
                color = if (isSelected) TextPrimary else TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            if (badge != null && badge > 0) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .background(DangerRed, RoundedCornerShape(999.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = badge.toString(), color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun FriendsContent(state: UiState<List<Friend>>, onRemove: (String) -> Unit) {
    when (state) {
        is UiState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(color = AccentCyan, modifier = Modifier.align(Alignment.Center))
        }
        is UiState.Success -> {
            if (state.data.isEmpty()) EmptyState("👥", "No friends yet", "Add friends to play together!")
            else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.data) { friend ->
                    FriendCard(friend = friend, onInvite = {}, onChat = {}, onMore = { onRemove(friend.userId) })
                }
            }
        }
        is UiState.Error -> Text(state.message, color = DangerRed)
        else -> {}
    }
}

@Composable
private fun RequestsContent(state: UiState<List<FriendRequest>>, onAccept: (String) -> Unit, onReject: (String) -> Unit) {
    when (state) {
        is UiState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(color = AccentCyan, modifier = Modifier.align(Alignment.Center))
        }
        is UiState.Success -> {
            if (state.data.isEmpty()) EmptyState("📬", "No pending requests", "Requests will appear here")
            else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.data) { request ->
                    FriendRequestCard(request = request, onAccept = { onAccept(request.id) }, onReject = { onReject(request.id) })
                }
            }
        }
        is UiState.Error -> Text(state.message, color = DangerRed)
        else -> {}
    }
}

@Composable
private fun AddContent(
    query: String,
    onQueryChange: (String) -> Unit,
    state: UiState<List<PlayerSearchResult>>,
    onAdd: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search by username...", color = TextTertiary) },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = AccentCyan) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = AccentCyan,
                unfocusedBorderColor = BorderPurple
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        when (state) {
            is UiState.Loading -> CircularProgressIndicator(color = AccentCyan)
            is UiState.Success -> {
                if (state.data.isEmpty() && query.length >= 2) {
                    Text("No players found", color = TextTertiary)
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(state.data) { player ->
                            SearchPlayerCard(player = player, onAdd = { onAdd(player.id) })
                        }
                    }
                }
            }
            is UiState.Idle -> Text("Type at least 2 characters to search", color = TextTertiary)
            else -> {}
        }
    }
}

@Composable
private fun EmptyState(icon: String, title: String, subtitle: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = icon, fontSize = 64.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = title, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(text = subtitle, color = TextSecondary, fontSize = 12.sp)
    }
}