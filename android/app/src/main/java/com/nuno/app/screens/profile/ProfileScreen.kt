package com.nuno.app.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

    Box(modifier = Modifier.fillMaxSize().background(GameColors.Background)) {
        PremiumGameTableBackground()

        Row(modifier = Modifier.fillMaxSize().statusBarsPadding().padding(bottom = GameDimens.bottomNavHeight)) {
            // Sidebar
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .fillMaxHeight()
                    .background(Brush.verticalGradient(listOf(Color(0xFF121535).copy(0.98f), Color(0xFF0A0C22).copy(0.98f))))
                    .border(1.dp, Color.White.copy(0.06f), RoundedCornerShape(0.dp))
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.White.copy(0.08f), RoundedCornerShape(10.dp))
                                .border(1.dp, Color.White.copy(0.12f), RoundedCornerShape(10.dp))
                                .clickable { onBack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("PROFILE", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    tabs.forEachIndexed { index, tab ->
                        PremiumProfileTab(label = tab, isSelected = selectedTab == index) { selectedTab = index }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(0.06f), RoundedCornerShape(14.dp))
                            .border(1.dp, Color.White.copy(0.08f), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("🪙 $coins", color = GameColors.Gold, fontSize = 12.sp, fontWeight = FontWeight.Black)
                                Text("💎 $gems", color = GameColors.Cyan, fontSize = 12.sp, fontWeight = FontWeight.Black)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Premium Member", color = Color(0xFF8B92C0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                when (selectedTab) {
                    0 -> ProfileOverviewPremium(username, level, rank, stats)
                    1 -> StatsViewPremium(stats)
                    2 -> AchievementsViewPremium()
                    3 -> HistoryViewPremium()
                    4 -> TitlesViewPremium()
                }
            }
        }

        BottomNavBar(selectedRoute = "profile", onNavigate = onNavigate, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun PremiumProfileTab(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .shadow(if (isSelected) 8.dp else 0.dp, RoundedCornerShape(12.dp), spotColor = GameColors.Blue.copy(0.3f))
            .background(
                if (isSelected) Brush.linearGradient(listOf(Color(0xFF3B6BFF).copy(0.25f), Color(0xFF7B5CFF).copy(0.15f)))
                else Brush.linearGradient(listOf(Color.Transparent, Color.Transparent)),
                RoundedCornerShape(12.dp)
            )
            .border(1.dp, if (isSelected) GameColors.Blue.copy(0.35f) else Color.Transparent, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(if (isSelected) GameColors.Cyan else Color(0xFF5A6488), CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, color = if (isSelected) Color.White else Color(0xFF8B92C0), fontSize = 13.sp, fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium)
        }
    }
}

@Composable
private fun ProfileOverviewPremium(username: String, level: Int, rank: String, stats: Map<String, String>) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(
            modifier = Modifier
                .width(220.dp)
                .shadow(20.dp, RoundedCornerShape(20.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF1E2248), Color(0xFF131636))), RoundedCornerShape(20.dp))
                .border(1.dp, Color.White.copy(0.08f), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                GameAvatar(username = username, size = 84.dp, borderColor = GameColors.Gold, level = level)
                Spacer(modifier = Modifier.height(14.dp))
                Text(username, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black)
                Text("Level $level • Premium", color = GameColors.Cyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0A0C22), RoundedCornerShape(10.dp))
                        .padding(8.dp)
                ) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("XP Progress", color = Color(0xFF8B92C0), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            Text("420 / 1000", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { 0.42f },
                            modifier = Modifier.fillMaxWidth().height(6.dp),
                            color = GameColors.Gold,
                            trackColor = Color(0xFF1E2249)
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .width(200.dp)
                .shadow(20.dp, RoundedCornerShape(20.dp), spotColor = GameColors.Gold.copy(0.25f))
                .background(Brush.verticalGradient(listOf(Color(0xFF1E2248), Color(0xFF131636))), RoundedCornerShape(20.dp))
                .border(1.2.dp, GameColors.Gold.copy(0.35f), RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("CURRENT RANK", color = Color(0xFF8B92C0), fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .shadow(16.dp, CircleShape, spotColor = GameColors.Gold.copy(0.5f))
                        .background(Brush.radialGradient(listOf(GameColors.Gold, Color(0xFFFF8A00))), CircleShape)
                        .border(2.dp, Color.White.copy(0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏆", fontSize = 30.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(rank.uppercase(), color = GameColors.Gold, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .background(GameColors.Blue.copy(0.12f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Next: SILVER • 250 pts", color = GameColors.Cyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(18.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        QuickStatPremium("TOTAL WINS", stats["Matches Won"] ?: "0", GameColors.Green, modifier = Modifier.weight(1f))
        QuickStatPremium("MATCHES", stats["Matches Played"] ?: "0", GameColors.Blue, modifier = Modifier.weight(1f))
        QuickStatPremium("WIN RATE", stats["Win Rate"] ?: "0%", GameColors.Gold, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun QuickStatPremium(label: String, value: String, accent: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFF1A1F4A), Color(0xFF131636))), RoundedCornerShape(16.dp))
            .border(1.dp, accent.copy(0.25f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(value, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, color = accent, fontSize = 9.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
        }
    }
}

@Composable
private fun StatsViewPremium(stats: Map<String, String>) {
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

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("STATISTICS", color = GameColors.Gold, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 2.5.sp)
        Spacer(modifier = Modifier.height(6.dp))
        displayStats.forEach { (key, value) ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF131636), RoundedCornerShape(14.dp))
                    .border(1.dp, Color.White.copy(0.06f), RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(key, color = Color(0xFF8B92C0), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Text(value, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun AchievementsViewPremium() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("ACHIEVEMENTS", color = GameColors.Gold, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 2.5.sp)
        AchievementItemPremium("First Win", "Win your first match", 1f, true)
        AchievementItemPremium("NUNO Master", "Declare NUNO 50 times", 0.7f, false, "35/50")
        AchievementItemPremium("Winner", "Win 100 matches", 0.6f, false, "60/100")
    }
}

@Composable
private fun AchievementItemPremium(title: String, desc: String, progress: Float, unlocked: Boolean, progressText: String = "") {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(14.dp))
            .background(
                Brush.verticalGradient(
                    if (unlocked) listOf(GameColors.Gold.copy(0.12f), Color(0xFF131636))
                    else listOf(Color(0xFF131636), Color(0xFF0E1130))
                ),
                RoundedCornerShape(14.dp)
            )
            .border(1.dp, if (unlocked) GameColors.Gold.copy(0.35f) else Color.White.copy(0.06f), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (unlocked) Brush.linearGradient(listOf(GameColors.Gold, Color(0xFFFF8A00)))
                        else Brush.linearGradient(listOf(Color(0xFF1E2249), Color(0xFF131636))),
                        CircleShape
                    )
                    .border(1.dp, if (unlocked) Color.White.copy(0.3f) else Color.White.copy(0.06f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(if (unlocked) "🏆" else "🔒", fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = if (unlocked) GameColors.Gold else Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(desc, color = Color(0xFF8B92C0), fontSize = 11.sp)
                if (!unlocked && progressText.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(4.dp), color = GameColors.Green, trackColor = Color(0xFF0A0C22))
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(progressText, color = Color(0xFF5A6488), fontSize = 9.sp)
                }
            }
        }
    }
}

@Composable
private fun HistoryViewPremium() {
    Text("MATCH HISTORY", color = GameColors.Gold, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 2.5.sp)
    Spacer(modifier = Modifier.height(12.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF131636), RoundedCornerShape(14.dp))
            .border(1.dp, Color.White.copy(0.06f), RoundedCornerShape(14.dp))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🎮", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("No matches yet", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("Play your first game to see history", color = Color(0xFF5A6488), fontSize = 11.sp)
        }
    }
}

@Composable
private fun TitlesViewPremium() {
    Column {
        Text("TITLES", color = GameColors.Gold, fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 2.5.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF131636), RoundedCornerShape(14.dp))
                .border(1.dp, Color.White.copy(0.06f), RoundedCornerShape(14.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No titles unlocked • Keep playing to earn titles!", color = Color(0xFF5A6488), fontSize = 12.sp)
        }
    }
}
