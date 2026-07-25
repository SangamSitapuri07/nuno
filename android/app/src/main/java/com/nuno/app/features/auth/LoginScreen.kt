package com.nuno.app.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Brush
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
import com.nuno.app.core.utils.showToast

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
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        GameColors.BackgroundLight,
                        GameColors.Background,
                        GameColors.BackgroundDark
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LEFT - Logo
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    LogoLetter("N", GameColors.CardRed)
                    LogoLetter("U", GameColors.CardBlue)
                    LogoLetter("N", GameColors.CardGreen)
                    LogoLetter("O", GameColors.CardYellow, android.graphics.Color.BLACK.let { androidx.compose.ui.graphics.Color.Black })
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Play. Compete. Win.",
                    color = GameColors.TextGray,
                    fontSize = 14.sp,
                    letterSpacing = 4.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Welcome Back",
                    color = GameColors.TextWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Sign in to continue playing",
                    color = GameColors.TextGray,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.width(32.dp))

            // RIGHT - Login Form
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = GameColors.TextWhite,
                        unfocusedTextColor = GameColors.TextWhite,
                        focusedBorderColor = GameColors.Gold,
                        unfocusedBorderColor = GameColors.BorderPurple,
                        focusedLabelColor = GameColors.Gold,
                        unfocusedLabelColor = GameColors.TextGray,
                        focusedLeadingIconColor = GameColors.Gold,
                        unfocusedLeadingIconColor = GameColors.TextGray
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                null
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = GameColors.TextWhite,
                        unfocusedTextColor = GameColors.TextWhite,
                        focusedBorderColor = GameColors.Gold,
                        unfocusedBorderColor = GameColors.BorderPurple,
                        focusedLabelColor = GameColors.Gold,
                        unfocusedLabelColor = GameColors.TextGray,
                        focusedLeadingIconColor = GameColors.Gold,
                        unfocusedLeadingIconColor = GameColors.TextGray
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Login Button
                Button(
                    onClick = { viewModel.login(email.trim(), password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GameColors.Gold),
                    enabled = loginState !is UiState.Loading
                ) {
                    if (loginState is UiState.Loading) {
                        CircularProgressIndicator(
                            color = androidx.compose.ui.graphics.Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "LOGIN",
                            color = androidx.compose.ui.graphics.Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Register link
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Don't have an account? ",
                        color = GameColors.TextGray,
                        fontSize = 13.sp
                    )
                    TextButton(onClick = onNavigateToRegister) {
                        Text(
                            text = "Sign Up",
                            color = GameColors.Gold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LogoLetter(letter: String, bg: androidx.compose.ui.graphics.Color, textColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.White) {
    Box(
        modifier = Modifier
            .size(width = 48.dp, height = 64.dp)
            .background(bg, RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            color = textColor,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black
        )
    }
}