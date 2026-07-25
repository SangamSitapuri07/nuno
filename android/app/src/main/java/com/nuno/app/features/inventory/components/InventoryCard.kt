package com.nuno.app.features.inventory.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.inventory.models.InventoryItem

@Composable
fun InventoryCard(
    item: InventoryItem,
    onEquip: () -> Unit,
    onFavorite: () -> Unit
) {
    val rarityColor = when (item.rarity) {
        "COMMON" -> NeutralGray400
        "RARE" -> AccentCyan
        "EPIC" -> PrimaryPurple
        "LEGENDARY" -> AccentGold
        "MYTHIC" -> AccentPink
        else -> NeutralGray400
    }

    val infiniteTransition = rememberInfiniteTransition(label = "inv")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = "glow"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                6.dp,
                RoundedCornerShape(12.dp),
                spotColor = if (item.rarity in listOf("LEGENDARY", "MYTHIC")) rarityColor.copy(alpha = glowAlpha) else Color.Transparent
            ),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, rarityColor.copy(alpha = 0.7f))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        brush = Brush.radialGradient(colors = listOf(rarityColor.copy(alpha = 0.3f), SurfaceDark)),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (item.cosmeticType) {
                        "AVATAR" -> "👤"
                        "CARD_BACK" -> "🎴"
                        "EMOTE" -> "😀"
                        "FRAME" -> "🖼️"
                        "TITLE" -> "🏆"
                        "THEME" -> "🎨"
                        else -> "🎁"
                    },
                    fontSize = 40.sp
                )

                // Equipped badge
                if (item.isEquipped) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                            .background(SuccessGreen, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "EQUIPPED", color = TextPrimary, fontSize = 8.sp, fontWeight = FontWeight.Black)
                    }
                }

                // Favorite icon
                IconButton(
                    onClick = onFavorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(28.dp)
                        .background(SurfaceDark.copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(
                        if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        null,
                        tint = if (item.isFavorite) DangerRed else TextTertiary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.name,
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = item.rarity,
                color = rarityColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = onEquip,
                enabled = !item.isEquipped,
                modifier = Modifier.fillMaxWidth().height(28.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (item.isEquipped) SuccessGreen else PrimaryBlue
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                if (item.isEquipped) {
                    Icon(Icons.Default.Check, null, tint = TextPrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("EQUIPPED", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                } else {
                    Text("EQUIP", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}