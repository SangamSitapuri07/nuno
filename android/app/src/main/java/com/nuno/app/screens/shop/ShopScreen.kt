package com.nuno.app.screens.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.R
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.*
import com.nuno.app.screens.home.PremiumGameTableBackground

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

    Box(modifier = Modifier.fillMaxSize().background(GameColors.Background)) {
        PremiumGameTableBackground()

        Row(modifier = Modifier.fillMaxSize().statusBarsPadding().padding(bottom = GameDimens.bottomNavHeight)) {
            // Sidebar
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .fillMaxHeight()
                    .background(Brush.verticalGradient(listOf(Color(0xFF121535).copy(0.98f), Color(0xFF0A0C22))))
                    .border(1.dp, Color.White.copy(0.06f))
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.White.copy(0.08f), RoundedCornerShape(10.dp))
                                .border(1.dp, Color.White.copy(0.12f), RoundedCornerShape(10.dp))
                                .clickable { onBack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("SHOP", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    categories.forEachIndexed { index, category ->
                        ShopCategoryPremium(label = category, isSelected = selectedCategory == index) { selectedCategory = index }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0E1130), RoundedCornerShape(12.dp))
                            .border(1.dp, GameColors.Gold.copy(0.25f), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("YOUR COINS", color = Color(0xFF8B92C0), fontSize = 8.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("🪙 $coins", color = GameColors.Gold, fontSize = 14.sp, fontWeight = FontWeight.Black)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("💎 $gems", color = GameColors.Cyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Content
            Column(modifier = Modifier.weight(1f).fillMaxHeight().padding(18.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("PREMIUM STORE", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
                        Text("Unlock exclusive items", color = Color(0xFF8B92C0), fontSize = 11.sp)
                    }
                    CurrencyBar(coins = coins, gems = gems)
                }

                Spacer(modifier = Modifier.height(18.dp))

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
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🛒", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No items in this category", color = Color(0xFF8B92C0), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredItems) { item ->
                            ShopItemCardPremium(item = item, onPurchase = { onPurchase(item) })
                        }
                    }
                }
            }
        }

        BottomNavBar(selectedRoute = "store", onNavigate = onNavigate, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun ShopCategoryPremium(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .shadow(if (isSelected) 8.dp else 0.dp, RoundedCornerShape(12.dp), spotColor = GameColors.Blue.copy(0.3f))
            .background(
                if (isSelected) Brush.linearGradient(listOf(Color(0xFF3B6BFF).copy(0.22f), Color(0xFF7B5CFF).copy(0.14f)))
                else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
                RoundedCornerShape(12.dp)
            )
            .border(1.dp, if (isSelected) GameColors.Blue.copy(0.35f) else Color.Transparent, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 11.dp)
    ) {
        Text(label, color = if (isSelected) Color.White else Color(0xFF8B92C0), fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold, letterSpacing = 0.8.sp)
    }
}

@Composable
private fun ShopItemCardPremium(item: ShopItemData, onPurchase: () -> Unit) {
    val rarityColor = when (item.rarity) {
        "COMMON" -> Color(0xFF8B92C0)
        "RARE" -> GameColors.Cyan
        "EPIC" -> GameColors.Purple
        "LEGENDARY" -> GameColors.Gold
        else -> Color(0xFF8B92C0)
    }

    val rarityGradient = when (item.rarity) {
        "RARE" -> listOf(Color(0xFF00D9FF).copy(0.25f), Color(0xFF3B6BFF).copy(0.15f))
        "EPIC" -> listOf(Color(0xFF7B5CFF).copy(0.3f), Color(0xFF651FFF).copy(0.15f))
        "LEGENDARY" -> listOf(Color(0xFFFFC71F).copy(0.3f), Color(0xFFFF8A00).copy(0.15f))
        else -> listOf(Color.White.copy(0.06f), Color.White.copy(0.02f))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(18.dp), spotColor = rarityColor.copy(0.25f))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF1D2148), Color(0xFF131636))),
                RoundedCornerShape(18.dp)
            )
            .border(1.2.dp, rarityColor.copy(0.5f), RoundedCornerShape(18.dp))
            .clickable { onPurchase() }
            .padding(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(92.dp)
                    .background(Brush.radialGradient(rarityGradient), RoundedCornerShape(14.dp))
                    .border(1.dp, rarityColor.copy(0.2f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Use generated 3D pack images where available
                val imageRes = when {
                    item.name.contains("Classic", ignoreCase = true) -> R.drawable.ic_store_classic_pack
                    item.name.contains("Gold", ignoreCase = true) -> R.drawable.ic_store_gold_pack
                    item.name.contains("Neon", ignoreCase = true) -> R.drawable.ic_store_neon_pack
                    item.name.contains("Diamond", ignoreCase = true) -> R.drawable.ic_store_diamond_pack
                    item.name.contains("Emote", ignoreCase = true) -> R.drawable.ic_emote_pack
                    item.name.contains("Trophy", ignoreCase = true) -> R.drawable.ic_trophy_champion
                    else -> null
                }

                if (imageRes != null) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = item.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(64.dp)
                    )
                } else {
                    Text(item.icon, fontSize = 40.sp)
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .background(rarityColor.copy(0.18f), RoundedCornerShape(6.dp))
                        .border(1.dp, rarityColor.copy(0.35f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(item.rarity, color = rarityColor, fontSize = 7.sp, fontWeight = FontWeight.Black, letterSpacing = 0.5.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(item.name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 1)

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0E1130), RoundedCornerShape(10.dp))
                    .border(1.dp, GameColors.Gold.copy(0.2f), RoundedCornerShape(10.dp))
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🪙", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("${item.price}", color = GameColors.Gold, fontSize = 13.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
