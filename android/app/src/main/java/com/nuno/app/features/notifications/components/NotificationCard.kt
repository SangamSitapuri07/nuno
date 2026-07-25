package com.nuno.app.features.notifications.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.notifications.Notification

@Composable
fun NotificationCard(notification: Notification) {
    val infiniteTransition = rememberInfiniteTransition(label = "notif")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = "glow"
    )

    val typeColor = when (notification.type) {
        "GAME" -> PrimaryBlue
        "FRIEND" -> SuccessGreen
        "REWARD" -> AccentGold
        else -> AccentCyan
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (!notification.read) {
                    Modifier.shadow(6.dp, RoundedCornerShape(12.dp), spotColor = typeColor.copy(alpha = glowAlpha))
                } else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.read) SurfaceCard else SurfaceLight
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, typeColor.copy(alpha = if (notification.read) 0.3f else 0.8f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(typeColor.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (notification.type) {
                        "GAME" -> Icons.Default.SportsEsports
                        "FRIEND" -> Icons.Default.Group
                        "REWARD" -> Icons.Default.CardGiftcard
                        "SYSTEM" -> Icons.Default.Settings
                        else -> Icons.Default.Notifications
                    },
                    contentDescription = null,
                    tint = typeColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = if (notification.read) FontWeight.Normal else FontWeight.Bold
                )
                Text(
                    text = notification.message,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Text(
                    text = "Just now",
                    color = TextTertiary,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            if (!notification.read) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(typeColor, CircleShape)
                )
            }
        }
    }
}