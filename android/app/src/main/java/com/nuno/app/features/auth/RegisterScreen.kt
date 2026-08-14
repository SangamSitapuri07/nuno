package com.nuno.app.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.nuno.app.core.designsystem.components.ButtonStyle
import com.nuno.app.core.designsystem.components.GameButton
import com.nuno.app.core.utils.showToast
import com.nuno.app.screens.home.PremiumCosmicParticles
import com.nuno.app.screens.home.PremiumGalaxyBackground

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val registerState by viewModel.registerState.collectAsState()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(registerState) {
        when (val state = registerState) {
            is UiState.Success -> {
                context.showToast("Account created! Please login.")
                onRegisterSuccess()
                viewModel.resetRegisterState()
            }
            is UiState.Error -> {
                context.showToast(state.message)
                viewModel.resetRegisterState()
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
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    RegLogoLetter("N", GameColors.CardRed)
                    RegLogoLetter("U", GameColors.CardBlue)
                    RegLogoLetter("N", GameColors.CardGreen)
                    RegLogoLetter("O", GameColors.CardYellow, Color.Black)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Create Account", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
                Text("Join the ultimate NUNO experience", color = Color(0xFF8B92C0), fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.width(32.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .shadow(32.dp, RoundedCornerShape(24.dp), spotColor = Color.Black.copy(0.8f))
                    .background(
                        Brush.verticalGradient(listOf(Color(0xFF1C2148).copy(0.92f), Color(0xFF131636).copy(0.97f))),
                        RoundedCornerShape(24.dp)
                    )
                    .border(1.dp, Color.White.copy(0.10f), RoundedCornerShape(24.dp))
                    .padding(28.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("SIGN UP", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = GameColors.Gold, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0E1130),
                            unfocusedContainerColor = Color(0xFF0E1130),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GameColors.Gold,
                            unfocusedBorderColor = Color.White.copy(0.08f)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Email, null, tint = GameColors.Gold, modifier = Modifier.size(18.dp)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0E1130),
                            unfocusedContainerColor = Color(0xFF0E1130),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GameColors.Gold,
                            unfocusedBorderColor = Color.White.copy(0.08f)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Lock, null, tint = GameColors.Gold, modifier = Modifier.size(18.dp)) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = Color(0xFF8B92C0), modifier = Modifier.size(18.dp))
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
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GameColors.Gold,
                            unfocusedBorderColor = Color.White.copy(0.08f)
                        )
                    )
                    Spacer(modifier = Modifier.height(22.dp))

                    GameButton(
                        text = "CREATE ACCOUNT",
                        onClick = { viewModel.register(username.trim(), email.trim(), password) },
                        style = ButtonStyle.GOLD,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        enabled = registerState !is UiState.Loading
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Already have an account? ", color = Color(0xFF8B92C0), fontSize = 12.sp)
                        Text(
                            "Sign In",
                            color = GameColors.Gold,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .background(GameColors.Gold.copy(0.12f), RoundedCornerShape(8.dp))
                                .border(1.dp, GameColors.Gold.copy(0.3f), RoundedCornerShape(8.dp))
                                .clickable { onNavigateToLogin() }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RegLogoLetter(letter: String, bg: Color, textColor: Color = Color.White) {
    Box(
        modifier = Modifier
            .size(width = 48.dp, height = 64.dp)
            .shadow(10.dp, RoundedCornerShape(8.dp), spotColor = bg.copy(0.5f))
            .background(Color.Black, RoundedCornerShape(8.dp))
            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
            .padding(2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize().background(bg, RoundedCornerShape(6.dp)), contentAlignment = Alignment.Center) {
            Text(letter, color = textColor, fontSize = 30.sp, fontWeight = FontWeight.Black)
        }
    }
}
