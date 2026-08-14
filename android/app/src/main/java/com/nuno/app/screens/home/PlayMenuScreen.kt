package com.nuno.app.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.BottomNavBar

@Composable
fun PlayMenuScreen(
    onBack: () -> Unit,
    onQuickMatch: () -> Unit,
    onCreateRoom: () -> Unit,
    onJoinRoom: () -> Unit,
    onMatchHistory: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0D1E))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = GameDimens.bottomNavHeight)
        ) {
            // Top bar like reference
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF1B1F3D), RoundedCornerShape(10.dp))
                        .border(1.dp, Color(0xFF2C3159), RoundedCornerShape(10.dp))
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("PLAY MENU", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
            }

            // 2x2 grid like reference screen 3
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    PlayMenuCard(
                        title = "QUICK MATCH",
                        subtitle = "Find match with\nrandom players",
                        icon = Icons.Default.FlashOn,
                        gradient = listOf(Color(0xFF7B4FFF), Color(0xFF5A35CC)),
                        modifier = Modifier.weight(1f),
                        onClick = onQuickMatch
                    )
                    PlayMenuCard(
                        title = "JOIN ROOM",
                        subtitle = "Join with room code",
                        icon = Icons.Default.Login,
                        gradient = listOf(Color(0xFF2A4B8D), Color(0xFF1E3A6F)),
                        modifier = Modifier.weight(1f),
                        onClick = onJoinRoom
                    )
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    PlayMenuCard(
                        title = "CREATE ROOM",
                        subtitle = "Create a room and\ninvite friends",
                        icon = Icons.Default.AddHome,
                        gradient = listOf(Color(0xFF3A3F5E), Color(0xFF2A2F4A)),
                        modifier = Modifier.weight(1f),
                        onClick = onCreateRoom
                    )
                    PlayMenuCard(
                        title = "MATCH HISTORY",
                        subtitle = "View your recent\nmatches",
                        icon = Icons.Default.History,
                        gradient = listOf(Color(0xFF2A4B8D), Color(0xFF1E3A6F)),
                        modifier = Modifier.weight(1f),
                        onClick = onMatchHistory
                    )
                }
            }
        }

        BottomNavBar(selectedRoute = "home", onNavigate = onNavigate, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun PlayMenuCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    gradient: List<Color>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = Color.Black.copy(0.5f))
            .background(Brush.verticalGradient(gradient), RoundedCornerShape(16.dp))
            .border(1.dp, Color.White.copy(0.08f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White.copy(0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Column {
                Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Black, letterSpacing = 0.5.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtitle, color = Color.White.copy(0.6f), fontSize = 11.sp, lineHeight = 14.sp)
            }
        }
    }
}
