package com.nuno.app.screens.social

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
        PremiumGameTableBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(bottom = GameDimens.bottomNavHeight)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
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
                        Text("LEADERBOARD", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                        Text(seasonInfo, color = GameColors.Gold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Box(
                    modifier = Modifier
                        .background(GameColors.Gold.copy(0.15f), RoundedCornerShape(12.dp))
                        .border(1.dp, GameColors.Gold.copy(0.35f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("SEASON 12", color = GameColors.Gold, fontSize = 10.sp, fontWeight = FontWeight.Black)
                }
            }

            // Tabs premium
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("GLOBAL", "COUNTRY", "FRIENDS").forEachIndexed { index, label ->
                    PremiumLeaderTab(label, selectedTab == index) { selectedTab = index }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Top 3 podium
            if (players.size >= 3) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    val top3 = players.take(3).sortedBy { it.rank }
                    // 2nd
                    top3.find { it.rank == 2 }?.let { player ->
                        PodiumCard(player, height = 110.dp, modifier = Modifier.weight(1f))
                    }
                    // 1st
                    top3.find { it.rank == 1 }?.let { player ->
                        PodiumCard(player, height = 140.dp, isFirst = true, modifier = Modifier.weight(1.1f))
                    }
                    // 3rd
                    top3.find { it.rank == 3 }?.let { player ->
                        PodiumCard(player, height = 90.dp, modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .background(Color.White.copy(0.05f), RoundedCornerShape(10.dp))
                    .border(1.dp, Color.White.copy(0.06f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("RANK", color = Color(0xFF8B92C0), fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp, modifier = Modifier.width(60.dp))
                Text("PLAYER", color = Color(0xFF8B92C0), fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp, modifier = Modifier.weight(1f))
                Text("POINTS", color = Color(0xFF8B92C0), fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                val remaining = if (players.size > 3) players.drop(3) else players
                itemsIndexed(remaining) { index, player ->
                    PremiumLeaderboardRow(player = player)
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
private fun PremiumLeaderTab(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .shadow(if (isSelected) 8.dp else 0.dp, RoundedCornerShape(20.dp), spotColor = GameColors.Blue.copy(0.4f))
            .background(
                if (isSelected) Brush.linearGradient(listOf(Color(0xFF3B6BFF), Color(0xFF7B5CFF)))
                else Brush.linearGradient(listOf(Color(0xFF1E2249), Color(0xFF131636))),
                RoundedCornerShape(20.dp)
            )
            .border(
                1.dp,
                if (isSelected) Color.White.copy(0.2f) else Color.White.copy(0.06f),
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Text(
            label,
            color = if (isSelected) Color.White else Color(0xFF8B92C0),
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun PodiumCard(player: LeaderboardPlayerData, height: androidx.compose.ui.unit.Dp, isFirst: Boolean = false, modifier: Modifier = Modifier) {
    val podiumColor = when (player.rank) {
        1 -> listOf(Color(0xFFFFD23F), Color(0xFFFF9A00))
        2 -> listOf(Color(0xFFC0C0C0), Color(0xFF8A8A8A))
        else -> listOf(Color(0xFFCD7F32), Color(0xFF9C5A20))
    }

    Box(
        modifier = modifier
            .height(height + 80.dp)
            .shadow(16.dp, RoundedCornerShape(16.dp), spotColor = podiumColor[0].copy(0.4f))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF1E2248), Color(0xFF131636))),
                RoundedCornerShape(16.dp)
            )
            .border(1.5.dp, podiumColor[0].copy(0.6f), RoundedCornerShape(16.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            GameAvatar(username = player.username, size = if (isFirst) 52.dp else 42.dp, borderColor = podiumColor[0])

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                player.username,
                color = Color.White,
                fontSize = if (isFirst) 12.sp else 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .background(Brush.linearGradient(podiumColor), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text("#${player.rank}", color = if (player.rank == 1) Color.Black else Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text("${player.points}", color = podiumColor[0], fontSize = if (isFirst) 16.sp else 14.sp, fontWeight = FontWeight.Black)

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .background(
                        Brush.verticalGradient(listOf(podiumColor[0].copy(0.3f), podiumColor[1].copy(0.15f))),
                        RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                    )
            )
        }
    }
}

@Composable
private fun PremiumLeaderboardRow(player: LeaderboardPlayerData) {
    val rankColor = when (player.rank) {
        1 -> GameColors.Gold
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> Color(0xFF5A6488)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (player.isYou) 12.dp else 4.dp, RoundedCornerShape(14.dp), spotColor = if (player.isYou) GameColors.Gold.copy(0.3f) else Color.Black.copy(0.3f))
            .background(
                if (player.isYou) Brush.verticalGradient(listOf(Color(0xFFFFC71F).copy(0.12f), Color(0xFF1E2249)))
                else Brush.verticalGradient(listOf(Color(0xFF1A1F4A).copy(0.9f), Color(0xFF131636).copy(0.95f))),
                RoundedCornerShape(14.dp)
            )
            .border(
                1.dp,
                if (player.isYou) GameColors.Gold.copy(0.5f) else Color.White.copy(0.06f),
                RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.width(50.dp), contentAlignment = Alignment.Center) {
                if (player.rank <= 3) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Brush.linearGradient(listOf(rankColor, rankColor.copy(0.7f))), CircleShape)
                            .border(1.dp, Color.White.copy(0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (player.rank) { 1 -> "🥇"; 2 -> "🥈"; else -> "🥉" },
                            fontSize = 18.sp
                        )
                    }
                } else {
                    Text("#${player.rank}", color = rankColor, fontSize = 14.sp, fontWeight = FontWeight.Black)
                }
            }

            GameAvatar(username = player.username, size = 36.dp, borderColor = if (player.isYou) GameColors.Gold else GameColors.Blue)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(player.username, color = if (player.isYou) GameColors.Gold else Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    if (player.isYou) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(GameColors.Gold.copy(0.15f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("YOU", color = GameColors.Gold, fontSize = 8.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
                Text("Level ${player.rank * 2 + 10}", color = Color(0xFF5A6488), fontSize = 10.sp)
            }

            Box(
                modifier = Modifier
                    .background(GameColors.Gold.copy(0.12f), RoundedCornerShape(10.dp))
                    .border(1.dp, GameColors.Gold.copy(0.25f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(player.points.toString(), color = GameColors.Gold, fontSize = 14.sp, fontWeight = FontWeight.Black)
            }
        }
    }
}
