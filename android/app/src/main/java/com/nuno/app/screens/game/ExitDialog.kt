package com.nuno.app.screens.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.*

@Composable
fun ExitGameDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Dialog(onDismissRequest = onCancel) {
        GamePanel(
            modifier = Modifier.width(300.dp),
            borderColor = GameColors.Red
        ) {
            Column(
                modifier = Modifier.padding(GameDimens.paddingXl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "⚠️", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "EXIT GAME?",
                    color = GameColors.TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Are you sure you want to leave the game?",
                    color = GameColors.TextGray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GameButton(
                        text = "CANCEL",
                        onClick = onCancel,
                        style = ButtonStyle.SECONDARY,
                        modifier = Modifier.weight(1f)
                    )
                    GameButton(
                        text = "EXIT",
                        onClick = onConfirm,
                        style = ButtonStyle.DANGER,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}