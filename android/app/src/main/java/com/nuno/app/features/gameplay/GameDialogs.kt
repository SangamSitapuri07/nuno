package com.nuno.app.features.gameplay

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nuno.app.core.theme.*
import com.nuno.app.core.utils.Constants
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh

// ─────────────────────────────────────────
// COLOR PICKER
// ─────────────────────────────────────────

@Composable
fun ColorPickerDialog(
    onColorSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, AccentGold)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "CHOOSE A COLOR",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ColorPickerButton(color = CardRed, name = "RED", onClick = onColorSelected)
                    ColorPickerButton(color = CardBlue, name = "BLUE", onClick = onColorSelected)
                    ColorPickerButton(color = CardGreen, name = "GREEN", onClick = onColorSelected)
                    ColorPickerButton(color = CardYellow, name = "YELLOW", onClick = onColorSelected)
                }
            }
        }
    }
}

@Composable
private fun ColorPickerButton(color: Color, name: String, onClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .background(color, CircleShape)
            .border(3.dp, TextPrimary, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = { onClick(name) }, modifier = Modifier.fillMaxSize()) { }
    }
}

// ─────────────────────────────────────────
// CHAT OVERLAY
// ─────────────────────────────────────────

@Composable
fun ChatOverlay(
    messages: List<String>,
    onSendMessage: (String) -> Unit,
    onClose: () -> Unit
) {
    var message by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.8f),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, BorderPurple)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "GAME CHAT",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, null, tint = TextPrimary)
                    }
                }

                HorizontalDivider(color = BorderPurple)

                LazyColumn(
                    modifier = Modifier.weight(1f).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(messages) { msg ->
                        Text(text = msg, color = TextPrimary, fontSize = 13.sp)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        placeholder = { Text("Type message...", color = TextTertiary) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AccentCyan,
                            unfocusedBorderColor = BorderPurple
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send)
                    )
                    IconButton(onClick = {
                        if (message.isNotBlank()) {
                            onSendMessage(message)
                            message = ""
                        }
                    }) {
                        Icon(Icons.Default.Send, null, tint = AccentCyan)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────
// QUICK CHAT DIALOG
// ─────────────────────────────────────────

@Composable
fun QuickChatDialog(
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(0.6f),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, SuccessGreen)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "QUICK CHAT",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Constants.QUICK_CHAT_MESSAGES.entries.toList()) { entry ->
                        Card(
                            onClick = { onSelect(entry.key) },
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, BorderPurple.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = entry.value,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────
// EMOTE WHEEL DIALOG
// ─────────────────────────────────────────

@Composable
fun EmoteWheelDialog(
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(0.6f),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, AccentGold)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "EMOTES",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Constants.EMOTES) { emote ->
                        Card(
                            onClick = { onSelect(emote) },
                            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.aspectRatio(1f),
                            border = BorderStroke(1.dp, BorderPurple.copy(alpha = 0.5f))
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = emote, fontSize = 28.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────
// GAME MENU DIALOG
// ─────────────────────────────────────────

@Composable
fun GameMenuDialog(
    onSurrender: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(0.4f),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, BorderPurple)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "GAME MENU",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, null, tint = TextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                MenuButton(
                    icon = Icons.Default.Flag,
                    label = "Surrender",
                    color = DangerRed,
                    onClick = onSurrender
                )
            }
        }
    }
}

@Composable
private fun MenuButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = label, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// ─────────────────────────────────────────
// REPORT PLAYER DIALOG
// ─────────────────────────────────────────

@Composable
fun ReportPlayerDialog(
    playerId: String,
    matchId: String,
    onReport: (String) -> Unit,
    onBlock: () -> Unit,
    onDismiss: () -> Unit
) {
    var showReportOptions by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(0.5f),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, DangerRed)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (showReportOptions) "REPORT PLAYER" else "PLAYER ACTIONS",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, null, tint = TextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!showReportOptions) {
                    MenuButton(
                        icon = Icons.Default.Warning,
                        label = "Report Player",
                        color = WarningOrange,
                        onClick = { showReportOptions = true }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    MenuButton(
                        icon = Icons.Default.Block,
                        label = "Block Player",
                        color = DangerRed,
                        onClick = onBlock
                    )
                } else {
                    Text(
                        text = "Select a reason:",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(Constants.REPORT_REASONS) { reason ->
                            Card(
                                onClick = { onReport(reason) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = reason,
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────
// GAME OVER OVERLAY
// ─────────────────────────────────────────

@Composable
fun GameOverOverlay(
    result: GameResult,
    currentUserId: String?,
    onHome: (() -> Unit)? = null,
    onPlayAgain: (() -> Unit)? = null
) {
    val isWinner = currentUserId != null && result.winner == currentUserId

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(3.dp, if (isWinner) AccentGold else DangerRed)
        ) {
            Column(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = if (isWinner) listOf(AccentGold, PrimaryPurple)
                                else listOf(NeutralGray600, SurfaceDark)
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.EmojiEvents,
                        null,
                        tint = if (isWinner) AccentGold else NeutralGray400,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isWinner) "VICTORY!" else "DEFEAT",
                    color = if (isWinner) AccentGold else DangerRed,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                )

                Text(
                    text = if (isWinner) "Great game!" else "Better luck next time",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatBox("Duration", formatDuration(result.duration))
                    StatBox("Turns", result.totalTurns.toString())
                    StatBox("XP", if (isWinner) "+125" else "+50")
                    StatBox("Coins", if (isWinner) "+50" else "+20")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ACTION BUTTONS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (onHome != null) {
                        OutlinedButton(
                            onClick = onHome,
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(2.dp, TextSecondary)
                        ) {
                            Icon(Icons.Default.Home, null, tint = TextPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("HOME", color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (onPlayAgain != null) {
                        Button(
                            onClick = onPlayAgain,
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) {
                            Icon(Icons.Default.Refresh, null, tint = TextPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("PLAY AGAIN", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatBox(label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, color = TextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(value, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Black)
        }
    }
}

// ─────────────────────────────────────────
// REMATCH DIALOG
// ─────────────────────────────────────────

@Composable
fun RematchDialog(
    otherPlayersRequesting: Set<String>,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Dialog(onDismissRequest = onDecline) {
        Card(
            modifier = Modifier.fillMaxWidth(0.5f),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, AccentGold)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Refresh,
                    null,
                    tint = AccentGold,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "REMATCH?",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                )
                Text(
                    text = "${otherPlayersRequesting.size} player(s) want to rematch",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = onDecline,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("DECLINE", color = DangerRed, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = onAccept,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Text("ACCEPT", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "%d:%02d".format(minutes, secs)
}