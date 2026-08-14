package com.nuno.app.core.designsystem.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens

@Composable
fun GameAvatar(
    username: String,
    modifier: Modifier = Modifier,
    size: Dp = GameDimens.avatarMd,
    borderColor: Color = GameColors.Gold,
    isOnline: Boolean = false,
    showGlow: Boolean = true,
    level: Int? = null
) {
    val initial = username.firstOrNull()?.uppercase() ?: "?"
    val avatarGradient = Brush.radialGradient(
        colors = listOf(
            Color(0xFF6C5CFF),
            Color(0xFF4A3ECC),
            Color(0xFF2E2466)
        )
    )

    val infinite = rememberInfiniteTransition(label = "avatarGlow")
    val glowAlpha by infinite.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(modifier = modifier.size(size)) {
        if (showGlow) {
            Box(
                modifier = Modifier
                    .size(size)
                    .shadow(
                        elevation = 12.dp,
                        shape = CircleShape,
                        spotColor = borderColor.copy(alpha = 0.6f)
                    )
                    .background(Color.Transparent, CircleShape)
            )
        }

        Box(
            modifier = Modifier
                .size(size)
                .background(avatarGradient, CircleShape)
                .border(
                    width = when {
                        size >= GameDimens.avatarXl -> 3.dp
                        size >= GameDimens.avatarMd -> 2.5.dp
                        else -> 2.dp
                    },
                    brush = Brush.linearGradient(
                        listOf(
                            borderColor,
                            borderColor.copy(alpha = 0.6f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial,
                color = Color.White,
                fontSize = (size.value * 0.42f).sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )
        }

        if (isOnline) {
            Box(
                modifier = Modifier
                    .size(size * 0.28f)
                    .background(Color(0xFF0A0C22), CircleShape)
                    .align(Alignment.BottomEnd)
                    .border(1.5.dp, GameColors.Background, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size((size * 0.28f) - 4.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    GameColors.Online,
                                    GameColors.GreenDark
                                )
                            ),
                            CircleShape
                        )
                        .shadow(4.dp, CircleShape, spotColor = GameColors.Online.copy(alpha = glowAlpha))
                )
            }
        }

        level?.let {
            Box(
                modifier = Modifier
                    .size(size * 0.38f)
                    .background(
                        Brush.linearGradient(listOf(GameColors.Gold, GameColors.Orange)),
                        CircleShape
                    )
                    .border(1.5.dp, Color.White.copy(alpha = 0.8f), CircleShape)
                    .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = it.toString(),
                    color = Color.Black,
                    fontSize = (size.value * 0.16f).sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}
