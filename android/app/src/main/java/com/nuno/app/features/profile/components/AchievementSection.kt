package com.nuno.app.features.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.profile.models.Achievement

@Composable
fun AchievementSection(achievements: List<Achievement>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "ACHIEVEMENTS",
            color = AccentGold,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(achievements) { achievement ->
                AchievementCard(achievement = achievement)
            }
        }
    }
}

@Composable
private fun AchievementCard(achievement: Achievement) {
    val rarityColor = when (achievement.rarity) {
        "COMMON" -> NeutralGray400
        "RARE" -> AccentCyan
        "EPIC" -> PrimaryPurple
        "LEGENDARY" -> AccentGold
        else -> NeutralGray400
    }

    val progress = achievement.currentProgress.toFloat() / achievement.maxProgress.toFloat()

    Card(
        modifier = Modifier.width(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.isUnlocked) SurfaceCard else SurfaceCard.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(if (achievement.isUnlocked) 2.dp else 1.dp, rarityColor.copy(alpha = if (achievement.isUnlocked) 1f else 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = achievement.icon,
                fontSize = 32.sp,
                modifier = Modifier.background(
                    if (achievement.isUnlocked) Color.Transparent else Color.Black.copy(alpha = 0.5f),
                    RoundedCornerShape(8.dp)
                ).padding(4.dp)
            )
            Text(
                text = achievement.title,
                color = if (achievement.isUnlocked) TextPrimary else TextTertiary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = "${achievement.currentProgress}/${achievement.maxProgress}",
                color = TextSecondary,
                fontSize = 9.sp
            )
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(3.dp).padding(top = 4.dp),
                color = rarityColor,
                trackColor = SurfaceDark
            )
        }
    }
}