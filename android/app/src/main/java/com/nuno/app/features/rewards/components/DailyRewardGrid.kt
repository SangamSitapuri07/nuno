package com.nuno.app.features.rewards.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.rewards.models.DailyReward

@Composable
fun DailyRewardGrid(rewards: List<DailyReward>, onClaim: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rewards.chunked(7).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { reward ->
                    DailyRewardCell(
                        reward = reward,
                        onClaim = { onClaim(reward.day) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyRewardCell(
    reward: DailyReward,
    onClaim: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (bgColor, borderColor) = when {
        reward.isClaimed -> SurfaceCard to SuccessGreen
        reward.isToday -> SurfaceLight to AccentGold
        else -> SurfaceCard to BorderPurple.copy(alpha = 0.3f)
    }

    Card(
        modifier = modifier.height(100.dp),
        onClick = if (reward.isToday && !reward.isClaimed) onClaim else { {} },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(if (reward.isToday) 2.dp else 1.dp, borderColor)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(
                if (reward.isToday) Brush.verticalGradient(colors = listOf(AccentGold.copy(alpha = 0.2f), SurfaceLight))
                else Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Transparent))
            ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "DAY ${reward.day}", color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = reward.icon, fontSize = 24.sp)
                Text(
                    text = "+${reward.amount}",
                    color = AccentGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black
                )
            }
            if (reward.isClaimed) {
                Icon(
                    Icons.Default.Check,
                    null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(32.dp).align(Alignment.Center)
                )
            }
        }
    }
}