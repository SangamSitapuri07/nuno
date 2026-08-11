package com.nuno.app.screens.social

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.*

data class LeaderboardPlayerData(
    val rank: Int,
    val username: String,
    val points: Int,
    val isYou: Boolean = false
)

@Composable
fun LeaderboardScreen(
    players: List<LeaderboardPlayerData>,
    seasonInfo: String,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = GameDimens.bottomNavHeight)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(GameDimens.paddingLg),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GameColors.TextWhite)
                    }
                    Text("LEADERBOARD", color = GameColors.TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                }
                Text(text = seasonInfo, color = GameColors.Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            // Tabs (#22 in reference)
            Row(
                modifier = Modifier.padding(horizontal = GameDimens.paddingXl),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("GLOBAL", "COUNTRY", "FRIENDS").forEachIndexed { index, label ->
                    LeaderTab(label, selectedTab == index) { selectedTab = index }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = GameDimens.paddingXl),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("RANK", color = GameColors.TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.width(50.dp))
                Text("PLAYER", color = GameColors.TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("POINTS", color = GameColors.TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = GameDimens.paddingXl),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                itemsIndexed(players) { index, player ->
                    LeaderboardRow(player = player)
                }
            }
        }

        BottomNavBar(
            selectedRoute = "leaderboard",
            onNavigate = onNavigate,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun LeaderTab(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                if (isSelected) GameColors.Blue else GameColors.Surface,
                RoundedCornerShape(GameDimens.radiusFull)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) GameColors.TextWhite else GameColors.TextGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun LeaderboardRow(player: LeaderboardPlayerData) {
    val rankColor = when (player.rank) {
        1 -> GameColors.Gold
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> GameColors.TextGray
    }

    GamePanel(
        borderColor = if (player.isYou) GameColors.Gold.copy(alpha = 0.5f) else Color.Transparent,
        backgroundColor = if (player.isYou) GameColors.Gold.copy(alpha = 0.1f) else GameColors.SurfaceCard
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = GameDimens.paddingMd, vertical = GameDimens.paddingSm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank
            Box(modifier = Modifier.width(40.dp)) {
                if (player.rank <= 3) {
                    Text(
                        text = when (player.rank) { 1 -> "🥇"; 2 -> "🥈"; else -> "🥉" },
                        fontSize = 20.sp
                    )
                } else {
                    Text(
                        text = "#${player.rank}",
                        color = rankColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            GameAvatar(
                username = player.username,
                size = 36.dp,
                borderColor = if (player.isYou) GameColors.Gold else GameColors.Blue
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = player.username + if (player.isYou) " (You)" else "",
                color = if (player.isYou) GameColors.Gold else GameColors.TextWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = player.points.toString(),
                color = GameColors.Gold,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}