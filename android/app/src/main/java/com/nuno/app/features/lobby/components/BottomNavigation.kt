package com.nuno.app.features.lobby.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
            .height(72.dp)
            .shadow(24.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), spotColor = Color.Black.copy(0.8f))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF161A3A).copy(0.98f), Color(0xFF0A0C22).copy(0.99f))),
                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .border(1.dp, Color.White.copy(0.10f), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItemPremium(Icons.Default.Person, "Profile", onProfile)
            NavItemPremium(Icons.Default.ShoppingCart, "Shop", onShop)
            NavItemPremium(Icons.Default.Group, "Friends", onFriends)
            NavItemPremium(Icons.Default.EmojiEvents, "Rank", onLeaderboard)
            NavItemPremium(Icons.Default.Settings, "Settings", onSettings)
        }
    }
}

@Composable
private fun NavItemPremium(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color.White.copy(0.06f), CircleShape)
                .border(1.dp, Color.White.copy(0.08f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color(0xFF8B92C0), modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = Color(0xFF8B92C0), fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
    }
}
