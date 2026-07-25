package com.nuno.app.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.home.LeftSidebar

@Composable
fun GameScreenScaffold(
    title: String,
    selectedRoute: String,
    onBack: () -> Unit,
    onNavigateToPlay: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToFriends: () -> Unit = {},
    onNavigateToLeaderboard: () -> Unit = {},
    onNavigateToShop: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    topBarActions: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(BackgroundDark, BackgroundDarker)))
    ) {
        LeftSidebar(
            username = "Player",
            level = 1,
            coins = 0,
            selectedRoute = selectedRoute,
            onPlayClick = onNavigateToPlay,
            onProfileClick = onNavigateToProfile,
            onFriendsClick = onNavigateToFriends,
            onLeaderboardClick = onNavigateToLeaderboard,
            onStoreClick = onNavigateToShop,
            onSettingsClick = onNavigateToSettings,
            onLogout = onLogout
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextPrimary)
                    }
                    Text(
                        text = title,
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 3.sp
                    )
                }
                topBarActions()
            }

            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}