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
import com.nuno.app.core.designsystem.components.GamePanel

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
            .background(GameColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = GameDimens.bottomNavHeight)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(GameDimens.paddingLg),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GameColors.TextWhite)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PLAY MENU",
                    color = GameColors.TextWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                )
            }

            // Menu Grid - 2x2
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = GameDimens.paddingXl),
                horizontalArrangement = Arrangement.spacedBy(GameDimens.paddingLg)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(GameDimens.paddingLg)
                ) {
                    PlayMenuItem(
                        icon = Icons.Default.FlashOn,
                        title = "QUICK MATCH",
                        description = "Find match with random players",
                        gradient = listOf(Color(0xFF2A3055), Color(0xFF1A2040)),
                        iconBg = Color(0xFF4361EE),
                        onClick = onQuickMatch,
                        modifier = Modifier.weight(1f)
                    )
                    PlayMenuItem(
                        icon = Icons.Default.Login,
                        title = "JOIN ROOM",
                        description = "Join with room code",
                        gradient = listOf(Color(0xFF2A3055), Color(0xFF1A2040)),
                        iconBg = Color(0xFF4361EE),
                        onClick = onJoinRoom,
                        modifier = Modifier.weight(1f)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(GameDimens.paddingLg)
                ) {
                    PlayMenuItem(
                        icon = Icons.Default.AddHome,
                        title = "CREATE ROOM",
                        description = "Create a room and invite friends",
                        gradient = listOf(Color(0xFF2A3055), Color(0xFF1A2040)),
                        iconBg = Color(0xFF4361EE),
                        onClick = onCreateRoom,
                        modifier = Modifier.weight(1f)
                    )
                    PlayMenuItem(
                        icon = Icons.Default.History,
                        title = "MATCH HISTORY",
                        description = "View your recent matches",
                        gradient = listOf(Color(0xFF2A3055), Color(0xFF1A2040)),
                        iconBg = Color(0xFF4361EE),
                        onClick = onMatchHistory,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        BottomNavBar(
            selectedRoute = "home",
            onNavigate = onNavigate,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun PlayMenuItem(
    icon: ImageVector,
    title: String,
    description: String,
    gradient: List<Color>,
    iconBg: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GamePanel(
        modifier = modifier.fillMaxWidth(),
        borderColor = Color(0xFF4A5580).copy(alpha = 0.5f),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(GameDimens.paddingLg),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .shadow(8.dp, RoundedCornerShape(12.dp), spotColor = iconBg)
                    .background(
                        color = iconBg,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = Color.White, modifier = Modifier.size(26.dp))
            }

            Spacer(modifier = Modifier.width(GameDimens.paddingLg))

            Column {
                Text(
                    text = title,
                    color = GameColors.TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    color = GameColors.TextGray,
                    fontSize = 11.sp
                )
            }
        }
    }
}