package com.nuno.app.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.GameButton
import com.nuno.app.core.designsystem.components.GamePanel
import com.nuno.app.core.designsystem.components.ButtonStyle

@Composable
fun CreateRoomScreen(
    onBack: () -> Unit,
    onCreate: (roomName: String, maxPlayers: Int, rules: Map<String, Boolean>) -> Unit
) {
    var roomName by remember { mutableStateOf("") }
    var maxPlayers by remember { mutableIntStateOf(4) }
    var stacking by remember { mutableStateOf(false) }
    var jumpIn by remember { mutableStateOf(false) }
    var drawUntilMatch by remember { mutableStateOf(false) }
    var forcePlay by remember { mutableStateOf(false) }
    var sevenZero by remember { mutableStateOf(false) }
    var challengeDrawFour by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Background)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Left side - Back + Title
            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .padding(GameDimens.paddingLg)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GameColors.TextWhite)
                    }
                    Text(
                        text = "CREATE ROOM",
                        color = GameColors.TextWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Room Name
                Text(text = "ROOM NAME", color = GameColors.TextGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = roomName,
                    onValueChange = { roomName = it },
                    placeholder = { Text("Sangam's Room", color = GameColors.TextDark) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(GameDimens.radiusMd),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = GameColors.TextWhite,
                        unfocusedTextColor = GameColors.TextWhite,
                        focusedBorderColor = GameColors.Gold,
                        unfocusedBorderColor = GameColors.BorderPurple
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Max Players
                Text(text = "MAX PLAYERS", color = GameColors.TextGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (count in 2..7) {
                        PlayerCountChip(
                            count = count,
                            isSelected = maxPlayers == count,
                            onClick = { maxPlayers = count }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Create Button
                GameButton(
                    text = "CREATE ROOM",
                    onClick = {
                        onCreate(
                            roomName.ifBlank { "Room" },
                            maxPlayers,
                            mapOf(
                                "stacking" to stacking,
                                "jumpIn" to jumpIn,
                                "drawUntilMatch" to drawUntilMatch,
                                "forcePlay" to forcePlay,
                                "sevenZero" to sevenZero,
                                "challengeDrawFour" to challengeDrawFour
                            )
                        )
                    },
                    style = ButtonStyle.GREEN,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Right side - Game Rules
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight()
                    .padding(GameDimens.paddingLg)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "GAME RULES",
                    color = GameColors.Gold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                RuleToggle("Stacking", "Allow stacking +2 on +2", stacking) { stacking = it }
                RuleToggle("Jump In", "Play matching card out of turn", jumpIn) { jumpIn = it }
                RuleToggle("Draw Until Match", "Keep drawing until playable card", drawUntilMatch) { drawUntilMatch = it }
                RuleToggle("Force Play", "Must play drawn card if possible", forcePlay) { forcePlay = it }
                RuleToggle("Seven Zero", "7 swaps hands, 0 rotates", sevenZero) { sevenZero = it }
                RuleToggle("Challenge +4", "Challenge Wild Draw Four", challengeDrawFour) { challengeDrawFour = it }
            }
        }
    }
}

@Composable
private fun PlayerCountChip(count: Int, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .background(
                color = if (isSelected) GameColors.Gold else GameColors.Surface,
                shape = RoundedCornerShape(GameDimens.radiusSm)
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) GameColors.Gold else GameColors.BorderPurple.copy(alpha = 0.3f),
                shape = RoundedCornerShape(GameDimens.radiusSm)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = count.toString(),
            color = if (isSelected) Color.Black else GameColors.TextWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
private fun RuleToggle(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    GamePanel(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = GameDimens.paddingMd, vertical = GameDimens.paddingSm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = GameColors.TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = description, color = GameColors.TextGray, fontSize = 10.sp)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = GameColors.Gold,
                    checkedTrackColor = GameColors.GoldDark.copy(alpha = 0.5f),
                    uncheckedThumbColor = GameColors.TextDark,
                    uncheckedTrackColor = GameColors.Surface
                )
            )
        }
    }
}