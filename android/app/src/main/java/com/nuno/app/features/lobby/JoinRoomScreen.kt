package com.nuno.app.features.lobby

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.theme.*
import com.nuno.app.core.utils.showToast

@Composable
fun JoinRoomScreen(
    onBack: () -> Unit,
    onRoomJoined: () -> Unit,
    viewModel: LobbyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var roomCode by remember { mutableStateOf("") }

    LaunchedEffect(uiState.room) {
        if (uiState.room != null) onRoomJoined()
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            context.showToast(it)
            viewModel.clearError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(BackgroundDark, BackgroundDarker)))
    ) {
        // Back button
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextPrimary)
            }
            Text(
                text = "JOIN ROOM",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 3.sp
            )
        }

        // Content
        Row(
            modifier = Modifier.fillMaxSize().padding(40.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left - Icon
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Group, null, tint = AccentCyan, modifier = Modifier.size(80.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Enter Room Code",
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ask your friend for the room code",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Right - Input + Button
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                OutlinedTextField(
                    value = roomCode,
                    onValueChange = { if (it.length <= 5) roomCode = it.uppercase() },
                    placeholder = { Text("ABX72", color = TextTertiary, fontSize = 28.sp) },
                    textStyle = TextStyle(
                        color = AccentCyan,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 6.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth().height(70.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentCyan,
                        unfocusedBorderColor = BorderPurple
                    ),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
                )

                Button(
                    onClick = {
                        if (roomCode.length == 5) viewModel.joinRoomByCode(roomCode)
                        else context.showToast("Room code must be 5 characters")
                    },
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = TextPrimary, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "JOIN ROOM",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 3.sp
                        )
                    }
                }
            }
        }
    }
}