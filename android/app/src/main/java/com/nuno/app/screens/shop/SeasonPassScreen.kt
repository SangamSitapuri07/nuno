package com.nuno.app.screens.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.*
import com.nuno.app.screens.home.PremiumGameTableBackground

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
    Box(modifier = Modifier.fillMaxSize().background(GameColors.Background)) {
        PremiumGameTableBackground()

        Column(modifier = Modifier.fillMaxSize().statusBarsPadding().padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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
                        Text(seasonName.uppercase(), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                        Text("Ends in: $endsIn", color = GameColors.Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Box(
                    modifier = Modifier
                        .background(Color(0xFF1E2249), RoundedCornerShape(12.dp))
                        .border(1.dp, GameColors.Gold.copy(0.35f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🏆", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("$currentLevel / $maxLevel", color = GameColors.Gold, fontSize = 13.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // XP progress premium
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF131636), RoundedCornerShape(16.dp))
                    .border(1.dp, Color.White.copy(0.06f), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("SEASON PROGRESS", color = Color(0xFF8B92C0), fontSize = 10.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
                        Text("${(xpProgress * 100).toInt()}% • Level $currentLevel", color = GameColors.Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { xpProgress },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = GameColors.Gold,
                        trackColor = Color(0xFF0E1130)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("REWARDS TRACK", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(rewards) { index, reward ->
                    SeasonRewardCardPremium(reward = reward, isCurrentLevel = reward.level == currentLevel)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(20.dp, RoundedCornerShape(18.dp), spotColor = GameColors.Gold.copy(0.4f))
                    .background(Brush.linearGradient(listOf(Color(0xFFFFD23F), Color(0xFFFF9A00))), RoundedCornerShape(18.dp))
                    .border(1.5.dp, Color.White.copy(0.3f), RoundedCornerShape(18.dp))
                    .clickable { onPremiumPass() }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⭐", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("PREMIUM PASS", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
                        Text("Unlock 2x rewards & exclusive skins", color = Color.Black.copy(0.7f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .background(Color.Black.copy(0.15f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("$4.99", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
        }
    }
}

@Composable
private fun SeasonRewardCardPremium(reward: SeasonRewardData, isCurrentLevel: Boolean) {
    val borderColor = when {
        isCurrentLevel -> GameColors.Gold
        reward.isClaimed -> GameColors.Green
        reward.isPremium -> GameColors.Purple
        else -> Color.White.copy(0.08f)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .background(
                    if (isCurrentLevel) GameColors.Gold.copy(0.18f) else Color(0xFF1E2249),
                    RoundedCornerShape(8.dp)
                )
                .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text("${reward.level}", color = if (isCurrentLevel) GameColors.Gold else Color(0xFF8B92C0), fontSize = 10.sp, fontWeight = FontWeight.Black)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .size(78.dp)
                .shadow(10.dp, RoundedCornerShape(16.dp), spotColor = borderColor.copy(0.3f))
                .background(
                    if (reward.isPremium) Brush.verticalGradient(listOf(Color(0xFF7B5CFF).copy(0.25f), Color(0xFF131636)))
                    else Brush.verticalGradient(listOf(Color(0xFF1D2148), Color(0xFF131636))),
                    RoundedCornerShape(16.dp)
                )
                .border(if (isCurrentLevel) 2.dp else 1.dp, borderColor, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (reward.isClaimed) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(GameColors.Green, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✓", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black)
                }
            } else {
                Text(reward.icon, fontSize = 30.sp)
            }

            if (isCurrentLevel) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-4).dp)
                        .background(GameColors.Gold, CircleShape)
                        .padding(4.dp)
                ) {
                    Box(modifier = Modifier.size(6.dp).background(Color.White, CircleShape))
                }
            }
        }

        if (reward.isPremium) {
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .background(Color(0xFF7B5CFF).copy(0.18f), RoundedCornerShape(6.dp))
                    .border(1.dp, GameColors.Purple.copy(0.3f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text("PREMIUM", color = GameColors.Purple, fontSize = 7.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}
