package com.nuno.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavBar(
    selectedRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem("Home", Icons.Default.Home, "home"),
        NavItem("Friends", Icons.Default.Group, "friends"),
        NavItem("Leaderboard", Icons.Default.EmojiEvents, "leaderboard"),
        NavItem("Shop", Icons.Default.ShoppingCart, "store"),
        NavItem("Profile", Icons.Default.Person, "profile")
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(GameDimens.bottomNavHeight)
            .background(GameColors.NavBackground)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = selectedRoute == item.route
                val color = if (isSelected) GameColors.NavActive else GameColors.NavInactive

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigate(item.route) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(width = 40.dp, height = 3.dp)
                                .background(GameColors.NavActive, RoundedCornerShape(2.dp))
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        tint = color,
                        modifier = Modifier.size(if (isSelected) 26.dp else 22.dp)
                    )
                    Text(
                        text = item.label,
                        color = color,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}