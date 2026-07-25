package com.nuno.app.features.profile.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*

@Composable
fun AvatarCard(
    username: String,
    level: Int,
    xp: Int,
    isOnline: Boolean = true,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "avatar")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = "glow"
    )

    Card(
        modifier = modifier
            .shadow(12.dp, RoundedCornerShape(16.dp), spotColor = AccentCyan.copy(alpha = glowAlpha)),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, AccentCyan)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(
                            brush = Brush.radialGradient(colors = listOf(PrimaryBlue, PrimaryPurple)),
                            shape = CircleShape
                        )
                        .border(4.dp, AccentCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = username.firstOrNull()?.uppercase() ?: "?",
                        color = TextPrimary,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(AccentGold, CircleShape)
                        .border(2.dp, BackgroundDark, CircleShape)
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "$level", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Black)
                }

                if (isOnline) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(SuccessGreen, CircleShape)
                            .border(3.dp, BackgroundDark, CircleShape)
                            .align(Alignment.TopEnd)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = username, color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = "$xp XP", color = AccentGold, fontSize = 12.sp)
        }
    }
}