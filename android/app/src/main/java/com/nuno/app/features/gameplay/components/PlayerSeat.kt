package com.nuno.app.features.gameplay.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*

@Composable
fun PlayerSeat(
    username: String,
    cardCount: Int,
    isCurrentTurn: Boolean,
    isMyself: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "seat")
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = "glow"
    )

    val borderColor = if (isCurrentTurn) AccentGold else BorderPurple.copy(alpha = 0.4f)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .then(if (isCurrentTurn) Modifier.shadow(12.dp, CircleShape, spotColor = AccentGold.copy(alpha = glow)) else Modifier)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            if (isCurrentTurn) AccentGold.copy(alpha = 0.5f) else PrimaryBlue,
                            PrimaryDark
                        )
                    ),
                    shape = CircleShape
                )
                .border(if (isCurrentTurn) 3.dp else 2.dp, borderColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = username.firstOrNull()?.uppercase() ?: "P",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = username, color = TextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)

        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(1.dp, AccentCyan)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🎴", fontSize = 10.sp)
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = cardCount.toString(),
                    color = AccentCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}