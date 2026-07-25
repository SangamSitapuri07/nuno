package com.nuno.app.features.rewards.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*

@Composable
fun ChestCard(
    name: String,
    rarity: String,
    icon: String,
    onOpen: () -> Unit
) {
    val rarityColor = when (rarity) {
        "COMMON" -> NeutralGray400
        "RARE" -> AccentCyan
        "EPIC" -> PrimaryPurple
        "LEGENDARY" -> AccentGold
        else -> NeutralGray400
    }

    val infiniteTransition = rememberInfiniteTransition(label = "chest")
    val bounce by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = "bounce"
    )
    val rotate by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = "rotate"
    )
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "glow"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(16.dp), spotColor = rarityColor.copy(alpha = glow)),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(3.dp, rarityColor),
        onClick = onOpen
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(rarityColor.copy(alpha = 0.2f), SurfaceCard)
                    )
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 80.sp,
                modifier = Modifier.scale(bounce).rotate(rotate)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = name,
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )
            Text(
                text = rarity,
                color = rarityColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onOpen,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Text("OPEN NOW", color = Color.Black, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
            }
        }
    }
}