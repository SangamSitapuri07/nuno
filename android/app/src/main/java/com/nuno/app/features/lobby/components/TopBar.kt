package com.nuno.app.features.lobby.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopRightBar(onNotifications: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .shadow(8.dp, CircleShape, spotColor = Color(0xFF4CC9F0))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF1A1D33), Color(0xFF0F1123))
                    ),
                    shape = CircleShape
                )
                .border(2.dp, Color(0xFF6247AA), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = onNotifications) {
                Icon(Icons.Default.Notifications, null, tint = Color(0xFF00E5FF))
            }
        }
    }
}