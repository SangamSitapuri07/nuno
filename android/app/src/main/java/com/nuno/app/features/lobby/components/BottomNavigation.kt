package com.nuno.app.features.lobby.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavigation(
    onProfile: () -> Unit,
    onShop: () -> Unit,
    onFriends: () -> Unit,
    onLeaderboard: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF3E2723).copy(alpha = 0.85f), Color(0xFF2C1810).copy(alpha = 0.95f))
                )
            )
            .border(
                width = 2.dp,
                brush = Brush.horizontalGradient(colors = listOf(Color(0xFFFFD700), Color(0xFFB8860B), Color(0xFFFFD700))),
                shape = RoundedCornerShape(0.dp)
            )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(Icons.Default.Person, "Profile", onProfile)
            NavItem(Icons.Default.ShoppingCart, "Shop", onShop)
            NavItem(Icons.Default.Group, "Friends", onFriends)
            NavItem(Icons.Default.EmojiEvents, "Leaderboard", onLeaderboard)
            NavItem(Icons.Default.Settings, "Settings", onSettings)
        }
    }
}

@Composable
private fun NavItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.85f else 1f,
        animationSpec = tween(100),
        label = "scale"
    )

    Column(
        modifier = Modifier
            .scale(scale)
            .clickable {
                pressed = true
                onClick()
            }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, null, tint = Color(0xFFFFD700), modifier = Modifier.size(26.dp))
        Text(text = label, color = Color(0xFFFFD700), fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }

    LaunchedEffect(pressed) {
        if (pressed) {
            kotlinx.coroutines.delay(100)
            pressed = false
        }
    }
}