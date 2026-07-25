package com.nuno.app.features.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    var startAnimation by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500)

        // Navigate based on login status
        when (isLoggedIn) {
            true -> onNavigateToHome()
            false -> onNavigateToLogin()
            null -> {
                delay(500)
                if (viewModel.isLoggedIn.value == true) {
                    onNavigateToHome()
                } else {
                    onNavigateToLogin()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(PrimaryBlue, PrimaryPurple, BackgroundDark)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "NUNO",
                fontSize = 96.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.scale(scale)
            )

            Spacer(modifier = Modifier.height(Dimensions.SpaceSmall))

            Text(
                text = "Play. Compete. Win.",
                fontSize = 16.sp,
                color = TextPrimary.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.scale(scale)
            )

            Spacer(modifier = Modifier.height(Dimensions.SpaceXLarge))

            CircularProgressIndicator(
                color = AccentCyan,
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = "v1.0.0",
            fontSize = 12.sp,
            color = TextPrimary.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}