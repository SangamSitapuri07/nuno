package com.nuno.app.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors

@Composable
fun JoinRoomScreen(
    onBack: () -> Unit,
    onJoin: (code: String) -> Unit
) {
    var code by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0A0D1E))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
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
                Text("JOIN ROOM", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Code display like reference
            Text("ENTER ROOM CODE", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Enter 6 character code", color = Color(0xFF5A607F), fontSize = 11.sp)

            Spacer(modifier = Modifier.height(20.dp))

            // Code boxes
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (i in 0..5) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF12152E), RoundedCornerShape(10.dp))
                            .border(1.5.dp, if (i < code.length) GameColors.Gold else Color(0xFF2A325A), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (i < code.length) {
                            Text(code[i].toString(), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black)
                        } else {
                            Text("_", color = Color(0xFF2A325A), fontSize = 16.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Numeric keypad like reference screen 6
            Column(verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                for (row in 0..2) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        for (col in 1..3) {
                            val number = row * 3 + col
                            KeypadButton(number = "$number") {
                                if (code.length < 6) code += number
                            }
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Backspace
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color(0xFF1B1F3D), CircleShape)
                            .border(1.dp, Color(0xFF2C3159), CircleShape)
                            .clickable { if (code.isNotEmpty()) code = code.dropLast(1) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Backspace, null, tint = Color.White, modifier = Modifier.size(22.dp))
                    }
                    KeypadButton(number = "0") {
                        if (code.length < 6) code += "0"
                    }
                    // Check / Join
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(if (code.length == 6) GameColors.Green else Color(0xFF1B1F3D), CircleShape)
                            .border(1.dp, if (code.length == 6) GameColors.Green else Color(0xFF2C3159), CircleShape)
                            .clickable { if (code.length == 6) onJoin(code) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            if (code.length == 6) {
                Button(
                    onClick = { onJoin(code) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GameColors.Gold)
                ) {
                    Text("JOIN ROOM", color = Color.Black, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                }
            }
        }
    }
}

@Composable
private fun KeypadButton(number: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(Color(0xFF12152E), CircleShape)
            .border(1.dp, Color(0xFF2A325A), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(number, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}
