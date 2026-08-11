package com.nuno.app.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.*
import androidx.compose.material3.CircularProgressIndicator

@Composable
fun ProfileScreen(
    username: String,
    level: Int,
    rank: String,
    coins: Int,
    gems: Int,
    stats: Map<String, String>,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Profile", "Stats", "Achievements", "History", "Titles")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = GameDimens.bottomNavHeight)
        ) {
            // Left sidebar tabs (#19 in reference)
            Column(
                modifier = Modifier
                    .width(160.dp)
                    .fillMaxHeight()
                    .background(GameColors.BackgroundDark)
                    .padding(GameDimens.paddingMd)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GameColors.TextWhite, modifier = Modifier.size(18.dp))
                    }
                    Text("PROFILE", color = GameColors.TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Black)
                }

                Spacer(modifier = Modifier.height(16.dp))

                tabs.forEachIndexed { index, tab ->
                    ProfileTabItem(
                        label = tab,
                        isSelected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Coins + Gems
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("🪙 $coins", color = GameColors.Gold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("💎 $gems", color = GameColors.Cyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Right content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(GameDimens.paddingLg)
                    .verticalScroll(rememberScrollState())
            ) {
                when (selectedTab) {
                    0 -> ProfileOverview(username, level, rank, stats)
                    1 -> StatsView(stats)
                    2 -> AchievementsView()
                    3 -> HistoryView()
                    4 -> TitlesView()
                }
            }
        }

        BottomNavBar(
            selectedRoute = "profile",
            onNavigate = onNavigate,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ProfileTabItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) GameColors.Blue.copy(alpha = 0.3f) else androidx.compose.ui.graphics.Color.Transparent,
                RoundedCornerShape(GameDimens.radiusSm)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "▸ $label",
            color = if (isSelected) GameColors.Gold else GameColors.TextGray,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun ProfileOverview(username: String, level: Int, rank: String, stats: Map<String, String>) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        // Avatar section
        GamePanel(modifier = Modifier.width(200.dp)) {
            Column(
                modifier = Modifier.padding(GameDimens.paddingLg),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GameAvatar(username = username, size = 80.dp, borderColor = GameColors.Gold)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = username, color = GameColors.TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = "Lv. $level", color = GameColors.TextGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { 0.4f },
                    modifier = Modifier.fillMaxWidth().height(6.dp),
                    color = GameColors.Gold,
                    trackColor = GameColors.Surface
                )
                Text(text = "0 / 100", color = GameColors.TextGray, fontSize = 10.sp)
            }
        }

        // Rank section (#19 in reference)
        GamePanel(modifier = Modifier.width(180.dp), borderColor = GameColors.Gold.copy(alpha = 0.5f)) {
            Column(
                modifier = Modifier.padding(GameDimens.paddingLg),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("CURRENT RANK", color = GameColors.TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "🏆", fontSize = 40.sp)
                Text(text = rank, color = GameColors.Gold, fontSize = 20.sp, fontWeight = FontWeight.Black)
                Text(text = "Next Rank: SILVER", color = GameColors.TextGray, fontSize = 10.sp)
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Quick stats
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        QuickStat("TOTAL WINS", stats["Matches Won"] ?: "0", Modifier.weight(1f))
        QuickStat("MATCHES PLAYED", stats["Matches Played"] ?: "0", Modifier.weight(1f))
        QuickStat("WIN RATE", stats["Win Rate"] ?: "0%", Modifier.weight(1f))
    }
}

@Composable
private fun QuickStat(label: String, value: String, modifier: Modifier = Modifier) {
    GamePanel(modifier = modifier) {
        Column(
            modifier = Modifier.padding(GameDimens.paddingMd).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, color = GameColors.TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Black)
            Text(text = label, color = GameColors.TextGray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// Stats view (#20 in reference)
@Composable
private fun StatsView(stats: Map<String, String>) {
    val displayStats = stats.ifEmpty {
        mapOf(
            "Matches Played" to "250",
            "Matches Won" to "160",
            "Win Rate" to "64%",
            "Best Score" to "450",
            "NUNO Declared" to "98",
            "Win Streak" to "12"
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("STATISTICS", color = GameColors.Gold, fontSize = 14.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(4.dp))
        displayStats.forEach { (key, value) ->
            GamePanel {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(GameDimens.paddingMd),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = key, color = GameColors.TextGray, fontSize = 13.sp)
                    Text(text = value, color = GameColors.TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// Achievements view (#21 in reference)
@Composable
private fun AchievementsView() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("ACHIEVEMENTS", color = GameColors.Gold, fontSize = 14.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)

        AchievementItem("First Win", "Win your first match", 1f, true)
        AchievementItem("NUNO Master", "Declare NUNO 50 times", 0.7f, false, "35/50")
        AchievementItem("Winner", "Win 100 matches", 0.6f, false, "60/100")
    }
}

@Composable
private fun AchievementItem(title: String, desc: String, progress: Float, unlocked: Boolean, progressText: String = "") {
    GamePanel(borderColor = if (unlocked) GameColors.Gold else GameColors.BorderPurple.copy(alpha = 0.3f)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(GameDimens.paddingMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (unlocked) "🏆" else "🔒", fontSize = 28.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = if (unlocked) GameColors.Gold else GameColors.TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(text = desc, color = GameColors.TextGray, fontSize = 11.sp)
                if (!unlocked && progressText.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(4.dp),
                        color = GameColors.Green,
                        trackColor = GameColors.Surface
                    )
                    Text(text = progressText, color = GameColors.TextGray, fontSize = 9.sp)
                }
            }
        }
    }
}

@Composable
private fun HistoryView() {
    val profileVm = androidx.hilt.navigation.compose.hiltViewModel<com.nuno.app.features.profile.ProfileViewModel>()
    val historyState by profileVm.historyState.collectAsState()

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("MATCH HISTORY", color = GameColors.Gold, fontSize = 14.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
        Spacer(modifier = Modifier.height(4.dp))

        when (historyState) {
            is com.nuno.app.core.common.UiState.Loading -> {
                CircularProgressIndicator(color = GameColors.Cyan, modifier = Modifier.size(32.dp))
            }
            is com.nuno.app.core.common.UiState.Success -> {
                val matches = (historyState as com.nuno.app.core.common.UiState.Success).data
                if (matches.isEmpty()) {
                    Text("No matches played yet", color = GameColors.TextGray, fontSize = 13.sp)
                } else {
                    matches.forEach { match ->
                        MatchHistoryCard(match)
                    }
                }
            }
            else -> {
                Text("No matches yet", color = GameColors.TextGray, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun MatchHistoryCard(match: com.nuno.app.core.network.MatchHistoryData) {
    val resultColor = if (match.isWinner) GameColors.Green else GameColors.Red

    GamePanel(borderColor = resultColor.copy(alpha = 0.3f)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(GameDimens.paddingMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (match.isWinner) "🏆" else "💔",
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (match.isWinner) "VICTORY" else "DEFEAT",
                    color = resultColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${match.gameMode} • ${match.duration / 60}m ${match.duration % 60}s",
                    color = GameColors.TextGray,
                    fontSize = 10.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (match.ratingChange >= 0) "+${match.ratingChange}" else "${match.ratingChange}",
                    color = if (match.ratingChange >= 0) GameColors.Green else GameColors.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "+${match.xpEarned} XP",
                    color = GameColors.Gold,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun TitlesView() {
    Text("TITLES", color = GameColors.Gold, fontSize = 14.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
    Spacer(modifier = Modifier.height(8.dp))
    Text("No titles unlocked", color = GameColors.TextGray, fontSize = 13.sp)
}