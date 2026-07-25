package com.nuno.app.screens.social

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.GamePanel

data class NotificationData(
    val id: String,
    val icon: String,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean
)

@Composable
fun NotificationsScreen(
    notifications: List<NotificationData>,
    onBack: () -> Unit,
    onMarkAllRead: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(GameDimens.paddingLg)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GameColors.TextWhite)
                    }
                    Text("NOTIFICATIONS", color = GameColors.TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                }
                TextButton(onClick = onMarkAllRead) {
                    Text("Mark all read", color = GameColors.Blue, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(notifications) { notif ->
                    NotificationCard(notif)
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(notif: NotificationData) {
    GamePanel(
        backgroundColor = if (notif.isRead) GameColors.SurfaceCard else GameColors.SurfaceLight
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(GameDimens.paddingMd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = notif.icon, fontSize = 28.sp)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notif.title,
                    color = GameColors.TextWhite,
                    fontSize = 13.sp,
                    fontWeight = if (notif.isRead) FontWeight.Normal else FontWeight.Bold
                )
                Text(text = notif.message, color = GameColors.TextGray, fontSize = 11.sp)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = notif.time, color = GameColors.TextDark, fontSize = 10.sp)
                if (!notif.isRead) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(GameColors.Blue, CircleShape)
                    )
                }
            }
        }
    }
}