package com.nuno.app.features.inventory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.theme.*
import com.nuno.app.core.utils.showToast
import com.nuno.app.features.home.LeftSidebar
import com.nuno.app.features.inventory.components.InventoryCard
import com.nuno.app.features.inventory.models.*

@Composable
fun InventoryScreen(
    onBack: () -> Unit,
    onNavigateToPlay: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToFriends: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            context.showToast(it)
            viewModel.clearMessage()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(BackgroundDark, BackgroundDarker)))
    ) {
        LeftSidebar(
            username = "Player", level = 1, coins = 0,
            selectedRoute = "inventory",
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "INVENTORY",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                )
                Text(
                    text = "${uiState.items.size} ITEMS",
                    color = AccentCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                // Left - Tabs
                Column(
                    modifier = Modifier.width(140.dp).fillMaxHeight().padding(end = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    InventoryTab.entries.forEach { tab ->
                        TabItem(
                            label = tab.label,
                            icon = tab.icon,
                            isSelected = uiState.selectedTab == tab,
                            onClick = { viewModel.setTab(tab) }
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    // Filters & Sort
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        InventoryFilter.entries.forEach { filter ->
                            FilterChip(
                                label = filter.label,
                                isSelected = uiState.selectedFilter == filter,
                                onClick = { viewModel.setFilter(filter) }
                            )
                        }
                    }

                    // Grid
                    val filteredItems = viewModel.getFilteredItems()
                    if (filteredItems.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "📦", fontSize = 64.sp)
                                Text(text = "No items", color = TextSecondary, fontSize = 14.sp)
                            }
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(5),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(filteredItems) { item ->
                                InventoryCard(
                                    item = item,
                                    onEquip = { viewModel.equipItem(item.id) },
                                    onFavorite = { viewModel.toggleFavorite(item.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabItem(label: String, icon: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) PrimaryPurple.copy(alpha = 0.3f) else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            color = if (isSelected) TextPrimary else TextSecondary,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun FilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) AccentCyan else SurfaceCard
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) BackgroundDark else TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}