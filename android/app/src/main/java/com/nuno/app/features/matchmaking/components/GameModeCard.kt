package com.nuno.app.features.matchmaking.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.nuno.app.features.matchmaking.models.GameMode

@Composable
fun GameModeCard(
    mode: GameMode,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.08f else 1f,
        animationSpec = tween(300),
        label = "scale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "modeGlow")
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = "glow"
    )

    Card(
        modifier = Modifier
            .width(if (isSelected) 240.dp else 200.dp)
            .height(if (isSelected) 320.dp else 280.dp)
            .scale(scale)
            .shadow(
                if (isSelected) 20.dp else 8.dp,
                RoundedCornerShape(20.dp),
                spotColor = mode.gradient[0].copy(alpha = if (isSelected) glow else 0.5f)
            ),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(
            if (isSelected) 3.dp else 1.dp,
            if (isSelected) mode.gradient[0] else BorderPurple.copy(alpha = 0.5f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(mode.gradient[0].copy(alpha = 0.3f), SurfaceCard)
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (mode.isLimitedTime) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.End)
                            .background(WarningOrange, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("LIMITED", color = TextPrimary, fontSize = 8.sp, fontWeight = FontWeight.Black)
                    }
                }

                Text(text = mode.icon, fontSize = 64.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = mode.name, color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = mode.description,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    maxLines = if (isSelected) 3 else 2
                )

                Spacer(modifier = Modifier.weight(1f))

                if (isSelected) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        InfoRow(label = "DIFFICULTY", value = mode.difficulty)
                        InfoRow(label = "DURATION", value = mode.estimatedDuration)
                        InfoRow(label = "PLAYERS", value = mode.playerCount)
                        InfoRow(label = "QUEUE", value = mode.queueTime)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextTertiary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Text(text = value, color = AccentCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}