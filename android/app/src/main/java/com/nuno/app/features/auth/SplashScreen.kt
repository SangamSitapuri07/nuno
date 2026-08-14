package com.nuno.app.features.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.designsystem.GameColors
import com.nuno.app.screens.home.PremiumCosmicParticles
import com.nuno.app.screens.home.PremiumGalaxyBackground
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    var startAnimation by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition(label = "splash")

    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.4f,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "scale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glow"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2600)
        when (isLoggedIn) {
            true -> onNavigateToHome()
            false -> onNavigateToLogin()
            null -> {
                delay(500)
                if (viewModel.isLoggedIn.value == true) onNavigateToHome() else onNavigateToLogin()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.Background),
        contentAlignment = Alignment.Center
    ) {
        PremiumGalaxyBackground()
        PremiumCosmicParticles()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.scale(scale)
        ) {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                GameColors.Purple.copy(alpha = glowAlpha * 0.4f),
                                GameColors.Blue.copy(alpha = glowAlpha * 0.2f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
                    .blur(24.dp)
            )

            Spacer(modifier = Modifier.height((-140).dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val logoItems: List<Triple<String, Color, String>> = listOf(
                    Triple("N", GameColors.CardRed, "N"),
                    Triple("U", GameColors.CardBlue, "U"),
                    Triple("N", GameColors.CardGreen, "N"),
                    Triple("O", GameColors.CardYellow, "O")
                )
                for ((letter, color) in logoItems.map { it.first to it.second }) {
                    Box(
                        modifier = Modifier
                            .size(width = 56.dp, height = 76.dp)
                            .shadow(16.dp, RoundedCornerShape(10.dp), spotColor = color.copy(0.6f))
                            .background(Color.Black, RoundedCornerShape(10.dp))
                            .border(2.dp, Color.White, RoundedCornerShape(10.dp))
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = letter,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Black,
                                color = if (letter == "O") Color.Black else Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "PLAY • COMPETE • WIN",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 5.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.White.copy(0.06f), CircleShape)
                    .border(1.dp, Color.White.copy(0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = GameColors.Cyan,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp)
                .background(Color.White.copy(0.06f), RoundedCornerShape(20.dp))
                .border(1.dp, Color.White.copy(0.08f), RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = "NUNO v1.0.0 • Premium Edition",
                fontSize = 10.sp,
                color = Color.White.copy(0.5f),
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
        }
    }
}
