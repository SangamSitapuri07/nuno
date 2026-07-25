package com.nuno.app.features.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.theme.*
import com.nuno.app.features.history.components.MatchRecordCard
import com.nuno.app.features.history.models.HistoryFilter
import com.nuno.app.features.home.LeftSidebar

@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    onNavigateToPlay: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToFriends: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val (total, wins, losses) = viewModel.getStats()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(BackgroundDark, BackgroundDarker)))
    ) {
        LeftSidebar(
            username = "Player", level = 1, coins = 0,
            selectedRoute = "history",
            onPlayClick = onNavigateToPlay,
            onProfileClick = onNavigateToProfile,
            onFriendsClick = onNavigateToFriends,
            onLeaderboardClick = onNavigateToLeaderboard,
            onStoreClick = onNavigateToShop,
            onSettingsClick = onNavigateToSettings,
            onLogout = onLogout
        )

        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MATCH HISTORY",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                )
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // Stats overview
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatsCard(label = "Total Matches", value = total.toString(), color = AccentCyan, modifier = Modifier.weight(1f))
                    StatsCard(label = "Wins", value = wins.toString(), color = SuccessGreen, modifier = Modifier.weight(1f))
                    StatsCard(label = "Losses", value = losses.toString(), color = DangerRed, modifier = Modifier.weight(1f))
                    StatsCard(
                        label = "Win Rate",
                        value = if (total > 0) "${(wins * 100 / total)}%" else "0%",
                        color = AccentGold,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Search + Filters
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = { Text("Search opponent...", color = TextTertiary) },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = AccentCyan) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AccentCyan,
                            unfocusedBorderColor = BorderPurple
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    HistoryFilter.entries.forEach { filter ->
                        FilterChip(
                            label = filter.label,
                            isSelected = uiState.selectedFilter == filter,
                            onClick = { viewModel.setFilter(filter) }
                        )
                    }
                }

                // Match list
                val filteredMatches = viewModel.getFilteredMatches()
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = AccentCyan)
                    }
                } else if (filteredMatches.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "📜", fontSize = 64.sp)
                        Text(text = "No matches yet", color = TextSecondary, fontSize = 16.sp)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filteredMatches) { match ->
                            MatchRecordCard(match = match)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsCard(label: String, value: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, color = color, fontSize = 24.sp, fontWeight = FontWeight.Black)
            Text(text = label, color = TextSecondary, fontSize = 10.sp)
        }
    }
}

@Composable
private fun FilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PrimaryPurple else SurfaceCard
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) TextPrimary else TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}