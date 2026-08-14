package com.nuno.app.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.common.UiState
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.core.designsystem.components.GameButton
import com.nuno.app.core.designsystem.components.ButtonStyle
import com.nuno.app.core.utils.showToast
import com.nuno.app.screens.home.PremiumGalaxyBackground
import com.nuno.app.screens.home.PremiumCosmicParticles

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val loginState by viewModel.loginState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is UiState.Success -> {
                context.showToast("Welcome back!")
                onLoginSuccess()
                viewModel.resetLoginState()
            }
            is UiState.Error -> {
                context.showToast(state.message)
                viewModel.resetLoginState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Background)
    ) {
        PremiumGalaxyBackground()
        PremiumCosmicParticles()

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LEFT - Brand
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    GameColors.Purple.copy(0.35f),
                                    GameColors.Blue.copy(0.15f),
                                    Color.Transparent
                                )
                            ),
                            CircleShape
                        )
                        .blur(24.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.offset(y = (-160).dp)
                ) {
                    LogoLetter("N", GameColors.CardRed)
                    LogoLetter("U", GameColors.CardBlue)
                    LogoLetter("N", GameColors.CardGreen)
                    LogoLetter("O", GameColors.CardYellow, Color.Black)
                }

                Spacer(modifier = Modifier.height((-130).dp))

                Text(
                    text = "PLAY • COMPETE • WIN",
                    color = Color.White.copy(0.5f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 4.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .background(Color.White.copy(0.06f), RoundedCornerShape(16.dp))
                        .border(1.dp, Color.White.copy(0.08f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Column {
                        Text(
                            text = "Welcome Back",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Sign in to continue your NUNO journey",
                            color = Color(0xFF8B92C0),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(32.dp))

            // RIGHT - Login Form Premium
            Box(
                modifier = Modifier
                    .weight(1f)
                    .shadow(32.dp, RoundedCornerShape(24.dp), spotColor = Color.Black.copy(0.8f))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF1C2148).copy(0.92f), Color(0xFF131636).copy(0.97f))
                        ),
                        RoundedCornerShape(24.dp)
                    )
                    .border(1.dp, Color.White.copy(0.10f), RoundedCornerShape(24.dp))
                    .padding(28.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(0.04f), RoundedCornerShape(14.dp))
                            .border(1.dp, Color.White.copy(0.06f), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                "LOGIN",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Enter your credentials",
                                color = Color(0xFF8B92C0),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = GameColors.Gold, modifier = Modifier.size(18.dp)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0E1130),
                            unfocusedContainerColor = Color(0xFF0E1130),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White.copy(0.9f),
                            focusedBorderColor = GameColors.Gold.copy(0.8f),
                            unfocusedBorderColor = Color.White.copy(0.08f),
                            focusedLabelColor = GameColors.Gold,
                            unfocusedLabelColor = Color(0xFF5A6488)
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = GameColors.Gold, modifier = Modifier.size(18.dp)) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    null,
                                    tint = Color(0xFF8B92C0),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0E1130),
                            unfocusedContainerColor = Color(0xFF0E1130),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White.copy(0.9f),
                            focusedBorderColor = GameColors.Gold.copy(0.8f),
                            unfocusedBorderColor = Color.White.copy(0.08f),
                            focusedLabelColor = GameColors.Gold,
                            unfocusedLabelColor = Color(0xFF5A6488)
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    GameButton(
                        text = if (loginState is UiState.Loading) "SIGNING IN..." else "LOGIN",
                        onClick = { viewModel.login(email.trim(), password) },
                        style = ButtonStyle.GOLD,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        enabled = loginState !is UiState.Loading && email.isNotBlank() && password.isNotBlank()
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Don't have an account? ",
                            color = Color(0xFF8B92C0),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Sign Up",
                            color = GameColors.Gold,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .background(GameColors.Gold.copy(0.12f), RoundedCornerShape(8.dp))
                                .border(1.dp, GameColors.Gold.copy(0.3f), RoundedCornerShape(8.dp))
                                .clickable { onNavigateToRegister() }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LogoLetter(letter: String, bg: Color, textColor: Color = Color.White) {
    Box(
        modifier = Modifier
            .size(width = 52.dp, height = 70.dp)
            .shadow(12.dp, RoundedCornerShape(8.dp), spotColor = bg.copy(0.6f))
            .background(Color.Black, RoundedCornerShape(8.dp))
            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bg, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = letter,
                color = textColor,
                fontSize = 34.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}
