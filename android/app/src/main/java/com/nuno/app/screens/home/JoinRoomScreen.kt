package com.nuno.app.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.GameDimens
import com.nuno.app.core.designsystem.components.GameButton
import com.nuno.app.core.designsystem.components.ButtonStyle

@Composable
fun JoinRoomScreen(
    onBack: () -> Unit,
    onJoin: (code: String) -> Unit
) {
    var code by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(GameDimens.paddingLg)
        ) {
            // Top bar
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GameColors.TextWhite)
                }
                Text(
                    text = "JOIN ROOM",
                    color = GameColors.TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
            }

            // Content
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(400.dp)
                ) {
                    Icon(
                        Icons.Default.Group,
                        null,
                        tint = GameColors.Cyan,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "ENTER ROOM CODE",
                        color = GameColors.TextWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 3.sp
                    )

                    Text(
                        text = "Ask your friend for the room code",
                        color = GameColors.TextGray,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = code,
                        onValueChange = { newValue ->
                            val filtered = newValue.filter { it.isLetterOrDigit() }.take(6).uppercase()
                            code = filtered
                        },
                        placeholder = {
                            Text(
                                "AB12C3",
                                color = GameColors.TextDark,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 6.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        textStyle = TextStyle(
                            color = GameColors.Gold,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 6.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(GameDimens.radiusMd),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GameColors.Cyan,
                            unfocusedBorderColor = GameColors.BorderPurple,
                            cursorColor = GameColors.Gold
                        ),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${code.length}/6 characters",
                        color = if (code.length >= 5) GameColors.Green else GameColors.TextGray,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    GameButton(
                        text = "JOIN ROOM",
                        onClick = {
                            if (code.isNotEmpty()) onJoin(code)
                        },
                        style = ButtonStyle.GREEN,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = code.isNotEmpty()
                    )
                }
            }
        }
    }
}