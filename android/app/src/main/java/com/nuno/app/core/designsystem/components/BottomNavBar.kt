package com.nuno.app.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
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
        NavItem("HOME", Icons.Default.Home, "home"),
        NavItem("FRIENDS", Icons.Default.Group, "friends"),
        NavItem("RANK", Icons.Default.EmojiEvents, "leaderboard"),
        NavItem("SHOP", Icons.Default.ShoppingCart, "store"),
        NavItem("PROFILE", Icons.Default.Person, "profile")
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .shadow(24.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp), spotColor = Color.Black.copy(0.8f))
            .background(
                Brush.verticalGradient(
                    listOf(
                        GameColors.NavBackgroundGradientTop.copy(0.97f),
                        GameColors.NavBackground.copy(0.98f)
                    )
                ),
                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .border(
                1.dp,
                Brush.horizontalGradient(
                    listOf(
                        Color.Transparent,
                        Color.White.copy(0.12f),
                        Color.Transparent
                    )
                ),
                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
    ) {
        // Top inner glow line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            Color.White.copy(0.12f),
                            Color.Transparent
                        )
                    )
                )
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = selectedRoute == item.route
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.05f else 1f,
                    label = "scale"
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigate(item.route) }
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 36.dp else 32.dp)
                            .shadow(
                                if (isSelected) 8.dp else 0.dp,
                                CircleShape,
                                spotColor = if (isSelected) GameColors.Blue.copy(0.6f) else Color.Transparent
                            )
                            .background(
                                if (isSelected) Brush.linearGradient(
                                    listOf(
                                        GameColors.Blue,
                                        GameColors.Purple
                                    )
                                ) else Brush.linearGradient(
                                    listOf(Color.Transparent, Color.Transparent)
                                ),
                                CircleShape
                            )
                            .border(
                                if (isSelected) 1.dp else 0.dp,
                                Color.White.copy(0.2f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected) Color.White else GameColors.NavInactive,
                            modifier = Modifier.size(if (isSelected) 20.dp else 18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.label,
                        color = if (isSelected) Color.White else GameColors.NavInactive,
                        fontSize = if (isSelected) 9.sp else 8.5.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        maxLines = 1
                    )

                    if (isSelected) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Box(
                            modifier = Modifier
                                .size(width = 16.dp, height = 2.5.dp)
                                .background(
                                    Brush.horizontalGradient(listOf(GameColors.Cyan, GameColors.Blue)),
                                    RoundedCornerShape(2.dp)
                                )
                                .shadow(4.dp, RoundedCornerShape(2.dp), spotColor = GameColors.Cyan)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(5.5.dp))
                    }
                }
            }
        }
    }
}
