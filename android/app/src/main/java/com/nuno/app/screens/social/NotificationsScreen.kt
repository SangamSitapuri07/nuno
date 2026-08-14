package com.nuno.app.screens.social

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.nuno.app.core.designsystem.components.GamePanel
import com.nuno.app.screens.home.PremiumGameTableBackground

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
    Box(modifier = Modifier.fillMaxSize().background(GameColors.Background)) {
        PremiumGameTableBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                        Text("NOTIFICATIONS", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                        Text("${notifications.count { !it.isRead }} unread", color = GameColors.Cyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFF3B6BFF).copy(0.15f), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFF3B6BFF).copy(0.3f), RoundedCornerShape(12.dp))
                        .clickable { onMarkAllRead() }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text("Mark all read", color = Color(0xFF3B6BFF), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(notifications) { notif ->
                    NotificationCardPremium(notif)
                }
            }
        }
    }
}

@Composable
private fun NotificationCardPremium(notif: NotificationData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .background(
                if (notif.isRead) Brush.verticalGradient(listOf(Color(0xFF131636), Color(0xFF0E1130)))
                else Brush.verticalGradient(listOf(Color(0xFF1E2248), Color(0xFF131636))),
                RoundedCornerShape(16.dp)
            )
            .border(1.dp, if (notif.isRead) Color.White.copy(0.05f) else GameColors.Blue.copy(0.25f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .shadow(8.dp, RoundedCornerShape(12.dp), spotColor = GameColors.Blue.copy(0.3f))
                    .background(
                        if (notif.isRead) Brush.linearGradient(listOf(Color(0xFF1E2249), Color(0xFF131636)))
                        else Brush.linearGradient(listOf(Color(0xFF3B6BFF), Color(0xFF7B5CFF))),
                        RoundedCornerShape(12.dp)
                    )
                    .border(1.dp, Color.White.copy(0.12f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(notif.icon, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(notif.title, color = Color.White, fontSize = 13.sp, fontWeight = if (notif.isRead) FontWeight.Medium else FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(notif.message, color = Color(0xFF8B92C0), fontSize = 11.sp, maxLines = 2)
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(notif.time, color = Color(0xFF5A6488), fontSize = 10.sp, fontWeight = FontWeight.Medium)
                if (!notif.isRead) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(modifier = Modifier.size(8.dp).background(GameColors.Blue, CircleShape).shadow(4.dp, CircleShape, spotColor = GameColors.Blue))
                }
            }
        }
    }
}
