package com.nuno.app.features.store

import androidx.compose.animation.core.*
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
import com.nuno.app.core.common.UiState
import com.nuno.app.core.theme.*
import com.nuno.app.core.utils.showToast
import com.nuno.app.features.home.LeftSidebar
import com.nuno.app.features.store.components.CurrencyBar
import com.nuno.app.features.store.components.ProductCard

@Composable
fun StoreScreen(
    onBack: () -> Unit,
    onNavigateToPlay: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToFriends: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: StoreViewModel = hiltViewModel()
) {
    val storeState by viewModel.storeState.collectAsState()
    val balance by viewModel.balanceState.collectAsState()
    val purchaseMessage by viewModel.purchaseMessage.collectAsState()
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(ShopCategory.FEATURED) }

    LaunchedEffect(purchaseMessage) {
        purchaseMessage?.let {
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
            username = "Player", level = 1, coins = balance,
            selectedRoute = "shop",
            onPlayClick = onNavigateToPlay,
            onProfileClick = onNavigateToProfile,
            onFriendsClick = onNavigateToFriends,
            onLeaderboardClick = onNavigateToLeaderboard,
            onSettingsClick = onNavigateToSettings,
            onLogout = onLogout
        )

        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            // Header with currency bar
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "SHOP",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                )

                CurrencyBar(coins = balance, gems = 0)
            }

            Row(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                // Category sidebar
                Column(
                    modifier = Modifier.width(160.dp).fillMaxHeight().padding(end = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ShopCategory.entries.forEach { category ->
                        CategoryChip(
                            category = category,
                            isSelected = selectedCategory == category,
                            onClick = { selectedCategory = category }
                        )
                    }
                }

                // Products grid
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    when (val state = storeState) {
                        is UiState.Loading -> CircularProgressIndicator(
                            color = AccentCyan,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        is UiState.Success -> {
                            if (state.data.isEmpty()) {
                                EmptyStore()
                            } else {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(4),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(state.data) { item ->
                                        ProductCard(
                                            item = item,
                                            canAfford = balance >= item.price,
                                            onPurchase = { viewModel.purchaseItem(item) }
                                        )
                                    }
                                }
                            }
                        }
                        is UiState.Error -> Text(state.message, color = DangerRed)
                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(category: ShopCategory, isSelected: Boolean, onClick: () -> Unit) {
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
        Text(text = category.icon, fontSize = 18.sp)
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = category.label,
            color = if (isSelected) TextPrimary else TextSecondary,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun EmptyStore() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "🛍️", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "No items available", color = TextSecondary, fontSize = 16.sp)
    }
}