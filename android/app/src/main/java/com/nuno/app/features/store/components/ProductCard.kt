package com.nuno.app.features.store.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.store.StoreItem

@Composable
fun ProductCard(
    item: StoreItem,
    canAfford: Boolean,
    onPurchase: () -> Unit
) {
    val rarityColor = when (item.rarity) {
        "COMMON" -> NeutralGray400
        "RARE" -> AccentCyan
        "EPIC" -> PrimaryPurple
        "LEGENDARY" -> AccentGold
        "MYTHIC" -> AccentPink
        else -> NeutralGray400
    }

    val infiniteTransition = rememberInfiniteTransition(label = "product")
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
                8.dp,
                RoundedCornerShape(12.dp),
                spotColor = if (item.rarity in listOf("LEGENDARY", "MYTHIC")) rarityColor.copy(alpha = glowAlpha) else Color.Transparent
            ),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, rarityColor.copy(alpha = 0.6f))
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
                    text = when (item.type) {
                        "AVATAR" -> "👤"
                        "CARD_BACK" -> "🎴"
                        "CARD_THEME" -> "🎨"
                        "EMOTE" -> "😀"
                        "BADGE" -> "🏅"
                        "TITLE" -> "🏆"
                        "VOICE_PACK" -> "🎙️"
                        "PROFILE_BANNER" -> "🖼️"
                        else -> "🎁"
                    },
                    fontSize = 40.sp
                )

                if (item.discountPercent > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .background(DangerRed, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "-${item.discountPercent}%",
                            color = TextPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                if (item.isLimited) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                            .background(WarningOrange, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "LIMITED",
                            color = TextPrimary,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.name,
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
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
                onClick = onPurchase,
                enabled = canAfford,
                modifier = Modifier.fillMaxWidth().height(30.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (canAfford) PrimaryBlue else NeutralGray600
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                if (item.originalPrice != null) {
                    Text(
                        text = "🪙 ${item.originalPrice}",
                        color = TextTertiary,
                        fontSize = 9.sp,
                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text("🪙 ${item.price}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}