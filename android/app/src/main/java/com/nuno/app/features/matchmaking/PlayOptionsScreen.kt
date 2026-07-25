package com.nuno.app.features.matchmaking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.components.GameScreenScaffold
import com.nuno.app.core.theme.*

@Composable
fun PlayOptionsScreen(
    onBack: () -> Unit,
    onCasual: () -> Unit,
    onRanked: () -> Unit,
    onCreateRoom: () -> Unit,
    onJoinRoom: () -> Unit,
    onNavigateToProfile: () -> Unit = {},
    onNavigateToFriends: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    GameScreenScaffold(
        title = "CHOOSE GAME MODE",
        selectedRoute = "play",
        onBack = onBack,
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToFriends = onNavigateToFriends,
        onNavigateToLeaderboard = onNavigateToLeaderboard,
        onNavigateToShop = onNavigateToShop,
        onNavigateToSettings = onNavigateToSettings,
        onLogout = onLogout
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PlayModeCard(
                    icon = Icons.Default.SportsEsports,
                    title = "CASUAL",
                    description = "Play with random players • No rating impact",
                    gradient = listOf(PrimaryBlue, AccentCyan),
                    onClick = onCasual,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                PlayModeCard(
                    icon = Icons.Default.EmojiEvents,
                    title = "RANKED",
                    description = "Competitive play • Affects your rating",
                    gradient = listOf(PrimaryPurple, AccentPink),
                    onClick = onRanked,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }

            Text(
                text = "PLAY WITH FRIENDS",
                color = AccentCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 3.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PlayModeCard(
                    icon = Icons.Default.Add,
                    title = "CREATE ROOM",
                    description = "Create a private room and invite friends",
                    gradient = listOf(SuccessGreen, AccentCyan),
                    onClick = onCreateRoom,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                PlayModeCard(
                    icon = Icons.Default.Group,
                    title = "JOIN WITH CODE",
                    description = "Enter a room code to join friends",
                    gradient = listOf(WarningOrange, AccentPink),
                    onClick = onJoinRoom,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun PlayModeCard(
    icon: ImageVector,
    title: String,
    description: String,
    gradient: List<Color>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        brush = Brush.linearGradient(colors = gradient),
                        shape = RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = TextPrimary, modifier = Modifier.size(40.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = title, color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                color = TextSecondary,
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}