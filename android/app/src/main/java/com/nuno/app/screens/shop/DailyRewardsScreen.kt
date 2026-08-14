package com.nuno.app.screens.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.R
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.ButtonStyle
import com.nuno.app.core.designsystem.components.GameButton
import com.nuno.app.core.designsystem.components.GamePanel
import com.nuno.app.screens.home.PremiumCosmicParticles
import com.nuno.app.screens.home.PremiumGameTableBackground

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
    Box(modifier = Modifier.fillMaxSize().background(GameColors.Background)) {
        PremiumGameTableBackground()
        PremiumCosmicParticles()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(0.08f), RoundedCornerShape(12.dp))
                            .border(1.dp, Color.White.copy(0.12f), RoundedCornerShape(12.dp))
                            .clickable { onBack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("DAILY REWARDS", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                        Text("Login every day for bonuses!", color = Color(0xFF8B92C0), fontSize = 11.sp)
                    }
                }

                Box(
                    modifier = Modifier
                        .background(GameColors.Gold.copy(0.15f), RoundedCornerShape(10.dp))
                        .border(1.dp, GameColors.Gold.copy(0.3f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("STREAK: 3 DAYS 🔥", color = GameColors.Gold, fontSize = 10.sp, fontWeight = FontWeight.Black)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Chest highlight with generated 3D asset
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(20.dp, RoundedCornerShape(20.dp), spotColor = GameColors.Gold.copy(0.25f))
                    .background(Brush.verticalGradient(listOf(Color(0xFF1E2248), Color(0xFF131636))), RoundedCornerShape(20.dp))
                    .border(1.dp, GameColors.Gold.copy(0.3f), RoundedCornerShape(20.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_daily_reward_chest_gold),
                        contentDescription = "Daily Chest",
                        modifier = Modifier.size(64.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Today's Bonus", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
                        Text("Come back daily to keep your streak!", color = Color(0xFF8B92C0), fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rewards.forEach { reward ->
                    DayCardPremium(reward = reward, modifier = Modifier.weight(1f), onClaim = if (reward.isToday && !reward.isClaimed) onClaim else null)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (rewards.any { it.isToday && !it.isClaimed }) {
                GameButton(text = "CLAIM TODAY'S REWARD 🎁", onClick = onClaim, style = ButtonStyle.GOLD, modifier = Modifier.fillMaxWidth().height(56.dp))
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(0.06f), RoundedCornerShape(14.dp))
                        .border(1.dp, Color.White.copy(0.08f), RoundedCornerShape(14.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Come back tomorrow for your next reward! ⏰", color = Color(0xFF8B92C0), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
private fun DayCardPremium(reward: DailyRewardData, modifier: Modifier = Modifier, onClaim: (() -> Unit)?) {
    val borderColor = when {
        reward.isToday -> GameColors.Gold
        reward.isClaimed -> GameColors.Green
        else -> Color.White.copy(0.08f)
    }

    val bgGradient = when {
        reward.isToday -> listOf(Color(0xFFFFC71F).copy(0.22f), Color(0xFF1E2248))
        reward.isClaimed -> listOf(Color(0xFF00E676).copy(0.14f), Color(0xFF131636))
        else -> listOf(Color(0xFF1E2248), Color(0xFF131636))
    }

    Box(
        modifier = modifier
            .height(124.dp)
            .shadow(12.dp, RoundedCornerShape(16.dp), spotColor = borderColor.copy(0.25f))
            .background(Brush.verticalGradient(bgGradient), RoundedCornerShape(16.dp))
            .border(if (reward.isToday) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClaim?.invoke() }
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Box(
                modifier = Modifier
                    .background(
                        if (reward.isToday) GameColors.Gold.copy(0.18f) else Color.White.copy(0.06f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text("DAY ${reward.day}", color = if (reward.isToday) GameColors.Gold else Color(0xFF8B92C0), fontSize = 8.sp, fontWeight = FontWeight.Black, letterSpacing = 0.8.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(reward.icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text("${reward.amount}", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Black)
            if (reward.isClaimed) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .background(GameColors.Green, RoundedCornerShape(20.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("✓ CLAIMED", color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.Black)
                }
            } else if (reward.isToday) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .background(GameColors.Gold, RoundedCornerShape(20.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("CLAIM", color = Color.Black, fontSize = 7.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}


