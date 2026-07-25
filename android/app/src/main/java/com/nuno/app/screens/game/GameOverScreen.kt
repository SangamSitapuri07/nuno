package com.nuno.app.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.*

data class GameOverPlayerScore(
    val username: String,
    val score: Int,
    val isWinner: Boolean = false
)

@Composable
fun GameOverScreen(
    winnerName: String,
    scores: List<GameOverPlayerScore>,
    onPlayAgain: () -> Unit,
    onLobby: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        GamePanel(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(24.dp),
            borderColor = GameColors.Gold
        ) {
            Column(
                modifier = Modifier.padding(GameDimens.paddingXl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Winner avatar
                GameAvatar(
                    username = winnerName,
                    size = 80.dp,
                    borderColor = GameColors.Gold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = winnerName,
                    color = GameColors.Gold,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Score list
                scores.forEach { player ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (player.isWinner) GameColors.Gold.copy(alpha = 0.1f) else Color.Transparent,
                                RoundedCornerShape(GameDimens.radiusSm)
                            )
                            .padding(horizontal = GameDimens.paddingMd, vertical = GameDimens.paddingSm),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            GameAvatar(
                                username = player.username,
                                size = 28.dp,
                                borderColor = if (player.isWinner) GameColors.Gold else GameColors.Blue
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = player.username,
                                color = if (player.isWinner) GameColors.Gold else GameColors.TextWhite,
                                fontSize = 13.sp,
                                fontWeight = if (player.isWinner) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                        Text(
                            text = player.score.toString(),
                            color = GameColors.TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GameButton(
                        text = "PLAY AGAIN",
                        onClick = onPlayAgain,
                        style = ButtonStyle.PRIMARY,
                        modifier = Modifier.weight(1f)
                    )
                    GameButton(
                        text = "LOBBY",
                        onClick = onLobby,
                        style = ButtonStyle.SECONDARY,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}