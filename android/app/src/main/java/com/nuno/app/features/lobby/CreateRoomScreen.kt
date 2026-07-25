package com.nuno.app.features.lobby

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.theme.*
import com.nuno.app.core.utils.showToast

@Composable
fun CreateRoomScreen(
    onBack: () -> Unit = {},
    onRoomCreated: () -> Unit,
    viewModel: LobbyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedPlayerCount by remember { mutableIntStateOf(0) }
    var isCreating by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.room) {
        if (uiState.room != null) {
            onRoomCreated()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            context.showToast(it)
            viewModel.clearError()
            isCreating = false
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
                text = "CREATE ROOM",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 3.sp
            )
        }

        if (isCreating) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = AccentCyan)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Creating room...", color = TextSecondary, fontSize = 14.sp)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 80.dp, start = 40.dp, end = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "HOW MANY PLAYERS?",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 3.sp
                )

                Text(
                    text = "Select the number of players for your room",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Player count grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (count in 2..5) {
                        PlayerCountButton(
                            count = count,
                            isSelected = selectedPlayerCount == count,
                            onClick = { selectedPlayerCount = count },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (count in 6..10) {
                        PlayerCountButton(
                            count = count,
                            isSelected = selectedPlayerCount == count,
                            onClick = { selectedPlayerCount = count },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (selectedPlayerCount > 0) {
                    Text(
                        text = "$selectedPlayerCount PLAYERS",
                        color = AccentGold,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Game starts when all players join and ready up",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            isCreating = true
                            viewModel.createRoomWithPlayerCount(selectedPlayerCount)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(colors = listOf(PrimaryBlue, PrimaryPurple)),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "CREATE ROOM",
                                color = TextPrimary,
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
}

@Composable
private fun PlayerCountButton(
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(80.dp)
            .background(
                color = if (isSelected) PrimaryPurple else SurfaceCard,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) AccentCyan else BorderPurple.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = count.toString(),
                color = if (isSelected) AccentCyan else TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "players",
                color = if (isSelected) TextPrimary else TextSecondary,
                fontSize = 10.sp
            )
        }
    }
}