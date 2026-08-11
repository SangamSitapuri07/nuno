package com.nuno.app.screens.home
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.nuno.app.R
@Composable
fun PremiumGalaxyBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "bgRotationAnim")
    // Scaled to 2.2x so that a 1:1 image rotating 360 degrees (0, 90, 180, 270) spans past all screen edges on 20:9 displays
    val scalePulse by infiniteTransition.animateFloat(
        2.2f, 2.3f,
        infiniteRepeatable(tween(8000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "scale"
    )
    val rotationAngle by infiniteTransition.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(120_000, easing = LinearEasing)),
        label = "rotation"
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_galaxy_spiral),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .scale(scalePulse)
                .graphicsLayer {
                    rotationZ = rotationAngle
                }
        )
    }
}