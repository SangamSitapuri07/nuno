package com.nuno.app.screens.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.*

data class ShopItemData(
    val id: String,
    val name: String,
    val price: Int,
    val currency: String = "COINS",
    val rarity: String = "COMMON",
    val type: String = "CARD_PACK",
    val icon: String = "🎴"
)

@Composable
fun ShopScreen(
    coins: Int,
    gems: Int,
    items: List<ShopItemData>,
    onBack: () -> Unit,
    onPurchase: (ShopItemData) -> Unit,
    onNavigate: (String) -> Unit
) {
    var selectedCategory by remember { mutableIntStateOf(0) }
    val categories = listOf("FEATURED", "CARDS", "TABLES", "EMOTES", "AVATARS", "TITLES", "BADGES")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = GameDimens.bottomNavHeight)
        ) {
            // Left categories
            Column(
                modifier = Modifier
                    .width(140.dp)
                    .fillMaxHeight()
                    .background(GameColors.BackgroundDark)
                    .padding(GameDimens.paddingMd)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GameColors.TextWhite, modifier = Modifier.size(18.dp))
                    }
                    Text("SHOP", color = GameColors.TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Black)
                }

                Spacer(modifier = Modifier.height(16.dp))

                categories.forEachIndexed { index, category ->
                    ShopCategoryItem(
                        label = category,
                        isSelected = selectedCategory == index,
                        onClick = { selectedCategory = index }
                    )
                }
            }

            // Right content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(GameDimens.paddingLg)
            ) {
                // Currency bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    CurrencyBar(coins = coins, gems = gems)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Items grid (#23 in reference)
                val filteredItems = when (selectedCategory) {
                    0 -> items
                    1 -> items.filter { it.type in listOf("CARD_BACK", "CARD_THEME") }
                    2 -> items.filter { it.type == "TABLE_THEME" }
                    3 -> items.filter { it.type == "EMOTE" }
                    4 -> items.filter { it.type == "AVATAR" }
                    5 -> items.filter { it.type == "TITLE" }
                    6 -> items.filter { it.type in listOf("BADGE", "PROFILE_BANNER") }
                    else -> items
                }

                if (filteredItems.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No items in this category", color = GameColors.TextGray, fontSize = 14.sp)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredItems) { item ->
                            ShopItemCard(item = item, onPurchase = { onPurchase(item) })
                        }
                    }
                }
            }
        }

        BottomNavBar(
            selectedRoute = "store",
            onNavigate = onNavigate,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ShopCategoryItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) GameColors.Blue.copy(alpha = 0.3f) else Color.Transparent,
                RoundedCornerShape(GameDimens.radiusSm)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) GameColors.Gold else GameColors.TextGray,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun ShopItemCard(item: ShopItemData, onPurchase: () -> Unit) {
    val rarityColor = when (item.rarity) {
        "COMMON" -> GameColors.TextGray
        "RARE" -> GameColors.Cyan
        "EPIC" -> GameColors.Purple
        "LEGENDARY" -> GameColors.Gold
        else -> GameColors.TextGray
    }

    GamePanel(
        borderColor = rarityColor.copy(alpha = 0.5f),
        onClick = onPurchase
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(GameDimens.paddingSm),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(
                        Brush.radialGradient(listOf(rarityColor.copy(alpha = 0.2f), GameColors.Surface)),
                        RoundedCornerShape(GameDimens.radiusSm)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.icon, fontSize = 36.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.name,
                color = GameColors.TextWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GameColors.Surface, RoundedCornerShape(GameDimens.radiusSm))
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🪙 ${item.price}",
                    color = GameColors.Gold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}