package com.nuno.app.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.GameButton
import com.nuno.app.core.designsystem.components.GamePanel
import com.nuno.app.core.designsystem.components.ButtonStyle

@Composable
fun ColorPickerPopup(
    isDrawFour: Boolean = false,
    onColorSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedColor by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        GamePanel(
            modifier = Modifier.width(280.dp),
            borderColor = GameColors.Gold,
            borderWidth = GameDimens.borderMedium
        ) {
            Column(
                modifier = Modifier.padding(GameDimens.paddingXl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "CHOOSE ACTION",
                    color = GameColors.TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Color buttons
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ColorOption("Red", GameColors.CardRed, selectedColor == "RED") {
                        selectedColor = "RED"
                    }
                    ColorOption("Blue", GameColors.CardBlue, selectedColor == "BLUE") {
                        selectedColor = "BLUE"
                    }
                    ColorOption("Green", GameColors.CardGreen, selectedColor == "GREEN") {
                        selectedColor = "GREEN"
                    }
                    ColorOption("Yellow", GameColors.CardYellow, selectedColor == "YELLOW") {
                        selectedColor = "YELLOW"
                    }
                }

                if (isDrawFour) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Draw 4 Cards",
                        color = GameColors.Red,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                GameButton(
                    text = "PLAY CARD",
                    onClick = {
                        selectedColor?.let { onColorSelected(it) }
                    },
                    style = ButtonStyle.GOLD,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedColor != null
                )
            }
        }
    }
}

@Composable
private fun ColorOption(
    label: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isSelected) color.copy(alpha = 0.2f) else Color.Transparent,
                RoundedCornerShape(GameDimens.radiusMd)
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) color else GameColors.BorderPurple.copy(alpha = 0.3f),
                shape = RoundedCornerShape(GameDimens.radiusMd)
            )
            .clickable { onClick() }
            .padding(horizontal = GameDimens.paddingMd, vertical = GameDimens.paddingSm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .shadow(4.dp, CircleShape, spotColor = color)
                .background(color, CircleShape)
                .border(2.dp, Color.White, CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            color = if (isSelected) color else GameColors.TextWhite,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )

        Spacer(modifier = Modifier.weight(1f))

        if (isSelected) {
            Text(text = "✓", color = color, fontSize = 18.sp, fontWeight = FontWeight.Black)
        }
    }
}

// ─────────────────────────────────────────
// UNO DECLARED POPUP
// ─────────────────────────────────────────

@Composable
fun UnoDeclaRedPopup(
    username: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        GamePanel(
            modifier = Modifier.width(280.dp),
            borderColor = GameColors.Gold,
            borderWidth = GameDimens.borderThick
        ) {
            Column(
                modifier = Modifier.padding(GameDimens.paddingXl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "UNO!", fontSize = 48.sp, fontWeight = FontWeight.Black, color = GameColors.Gold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "You have declared UNO",
                    color = GameColors.TextGray,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                GameButton(
                    text = "OK",
                    onClick = onDismiss,
                    style = ButtonStyle.GOLD,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// ─────────────────────────────────────────
// DRAW PENALTY POPUP
// ─────────────────────────────────────────

@Composable
fun DrawPenaltyPopup(
    cardCount: Int,
    username: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        GamePanel(
            modifier = Modifier.width(300.dp),
            borderColor = GameColors.Red
        ) {
            Column(
                modifier = Modifier.padding(GameDimens.paddingXl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "DRAW $cardCount CARDS",
                    color = GameColors.Red,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "You have to draw $cardCount cards",
                    color = GameColors.TextGray,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                GameButton(
                    text = "OK",
                    onClick = onDismiss,
                    style = ButtonStyle.DANGER,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}