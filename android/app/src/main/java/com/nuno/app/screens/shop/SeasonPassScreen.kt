package com.nuno.app.screens.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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

data class SeasonRewardData(
    val level: Int,
    val icon: String,
    val isClaimed: Boolean,
    val isPremium: Boolean = false
)

@Composable
fun SeasonPassScreen(
    seasonName: String,
    endsIn: String,
    currentLevel: Int,
    maxLevel: Int,
    xpProgress: Float,
    rewards: List<SeasonRewardData>,
    onBack: () -> Unit,
    onPremiumPass: () -> Unit
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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GameColors.TextWhite)
                    }
                    Column {
                        Text(seasonName, color = GameColors.TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Black)
                        Text("Ends in: $endsIn", color = GameColors.Gold, fontSize = 11.sp)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🏆", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$currentLevel / $maxLevel",
                        color = GameColors.Gold,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // XP Progress
            LinearProgressIndicator(
                progress = { xpProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = GameColors.Gold,
                trackColor = GameColors.Surface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Reward track
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(rewards) { index, reward ->
                    SeasonRewardCard(reward = reward, isCurrentLevel = reward.level == currentLevel)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Premium pass button
            GameButton(
                text = "PREMIUM PASS - Unlock premium rewards",
                onClick = onPremiumPass,
                style = ButtonStyle.GOLD,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SeasonRewardCard(reward: SeasonRewardData, isCurrentLevel: Boolean) {
    val borderColor = when {
        isCurrentLevel -> GameColors.Gold
        reward.isClaimed -> GameColors.Green
        reward.isPremium -> GameColors.Purple
        else -> GameColors.BorderPurple.copy(alpha = 0.3f)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Level number
        Text(
            text = "${reward.level}",
            color = if (isCurrentLevel) GameColors.Gold else GameColors.TextGray,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Reward card
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(
                    if (reward.isPremium) GameColors.Purple.copy(alpha = 0.2f) else GameColors.SurfaceCard,
                    RoundedCornerShape(GameDimens.radiusMd)
                )
                .border(
                    width = if (isCurrentLevel) 2.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(GameDimens.radiusMd)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (reward.isClaimed) {
                Text(text = "✓", color = GameColors.Green, fontSize = 24.sp, fontWeight = FontWeight.Black)
            } else {
                Text(text = reward.icon, fontSize = 28.sp)
            }
        }

        if (reward.isPremium) {
            Text(text = "⭐", fontSize = 12.sp)
        }
    }
}