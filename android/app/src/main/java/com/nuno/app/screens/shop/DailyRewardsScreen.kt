package com.nuno.app.screens.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.*

data class DailyRewardData(
    val day: Int,
    val icon: String,
    val amount: Int,
    val isClaimed: Boolean,
    val isToday: Boolean
)

@Composable
fun DailyRewardsScreen(
    rewards: List<DailyRewardData>,
    onClaim: () -> Unit,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(GameDimens.paddingLg)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GameColors.TextWhite)
                }
                Text("DAILY REWARDS", color = GameColors.TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text("COME BACK TOMORROW", color = GameColors.Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 7-day reward cards (#24 in reference)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rewards.forEach { reward ->
                    DayCard(
                        reward = reward,
                        modifier = Modifier.weight(1f),
                        onClaim = if (reward.isToday && !reward.isClaimed) onClaim else null
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (rewards.any { it.isToday && !it.isClaimed }) {
                GameButton(
                    text = "CLAIM",
                    onClick = onClaim,
                    style = ButtonStyle.GOLD,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun DayCard(
    reward: DailyRewardData,
    modifier: Modifier = Modifier,
    onClaim: (() -> Unit)?
) {
    val borderColor = when {
        reward.isToday -> GameColors.Gold
        reward.isClaimed -> GameColors.Green
        else -> GameColors.BorderPurple.copy(alpha = 0.3f)
    }

    val bgColor = when {
        reward.isToday -> GameColors.Gold.copy(alpha = 0.15f)
        reward.isClaimed -> GameColors.Green.copy(alpha = 0.1f)
        else -> GameColors.SurfaceCard
    }

    GamePanel(
        modifier = modifier.height(120.dp),
        borderColor = borderColor,
        borderWidth = if (reward.isToday) GameDimens.borderMedium else GameDimens.borderThin,
        backgroundColor = bgColor,
        onClick = onClaim
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(GameDimens.paddingSm),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Day ${reward.day}",
                color = if (reward.isToday) GameColors.Gold else GameColors.TextGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = reward.icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${reward.amount}",
                color = GameColors.Gold,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black
            )
            if (reward.isClaimed) {
                Text(text = "✓", color = GameColors.Green, fontSize = 16.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}