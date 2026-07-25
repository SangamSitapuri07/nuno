package com.nuno.app.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import androidx.compose.ui.graphics.Color
import com.nuno.app.core.designsystem.GameDimens

@Composable
fun GameAvatar(
    username: String,
    modifier: Modifier = Modifier,
    size: Dp = GameDimens.avatarMd,
    borderColor: Color = GameColors.Gold,
    isOnline: Boolean = false
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(size)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(GameColors.Blue, GameColors.Purple)
                    ),
                    shape = CircleShape
                )
                .border(2.dp, borderColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = username.firstOrNull()?.uppercase() ?: "?",
                color = GameColors.TextWhite,
                fontSize = (size.value * 0.4f).sp,
                fontWeight = FontWeight.Black
            )
        }

        if (isOnline) {
            Box(
                modifier = Modifier
                    .size(size / 4)
                    .background(GameColors.Online, CircleShape)
                    .border(2.dp, GameColors.Background, CircleShape)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}

