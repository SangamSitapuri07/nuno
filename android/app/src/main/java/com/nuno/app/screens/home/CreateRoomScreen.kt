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

@Composable
fun CreateRoomScreen(
    onBack: () -> Unit,
    onCreate: (roomName: String, maxPlayers: Int, rules: Map<String, Boolean>) -> Unit
) {
    var roomName by remember { mutableStateOf("Sangam's Room") }
    var maxPlayers by remember { mutableIntStateOf(4) }
    var stacking by remember { mutableStateOf(false) }
    var jumpIn by remember { mutableStateOf(false) }
    var sevenZero by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0A0D1E))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header like reference
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF1B1F3D), RoundedCornerShape(10.dp))
                        .border(1.dp, Color(0xFF2C3159), RoundedCornerShape(10.dp))
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text("CREATE ROOM", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Left - Room Name & Max Players like reference 4
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF12152E), RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text("ROOM NAME", color = Color(0xFF5A607F), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = roomName,
                        onValueChange = { roomName = it },
                        placeholder = { Text("Sangam's Room", color = Color(0xFF5A607F), fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0A0D1E),
                            unfocusedContainerColor = Color(0xFF0A0D1E),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GameColors.Gold,
                            unfocusedBorderColor = Color(0xFF2A325A)
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text("MAX PLAYERS", color = Color(0xFF5A607F), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (count in 2..7) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        if (maxPlayers == count) GameColors.Gold else Color(0xFF1B1F3D),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .border(
                                        1.dp,
                                        if (maxPlayers == count) GameColors.Gold else Color(0xFF2A325A),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { maxPlayers = count },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("$count", color = if (maxPlayers == count) Color.Black else Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Right - Game Rules like reference
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFF12152E), RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFF1E2340), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text("GAME RULES", color = Color(0xFF5A607F), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    RuleRow("Stacking", stacking) { stacking = it }
                    RuleRow("Jump In", jumpIn) { jumpIn = it }
                    RuleRow("Seven Zero", sevenZero) { sevenZero = it }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            onCreate(
                                roomName,
                                maxPlayers,
                                mapOf("stacking" to stacking, "jumpIn" to jumpIn, "sevenZero" to sevenZero)
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GameColors.Gold)
                    ) {
                        Text("CREATE ROOM", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 12.sp, letterSpacing = 1.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun RuleRow(title: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = Color.White, fontSize = 13.sp)
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = GameColors.Gold,
                uncheckedThumbColor = Color(0xFF5A607F),
                uncheckedTrackColor = Color(0xFF1B1F3D)
            )
        )
    }
}
