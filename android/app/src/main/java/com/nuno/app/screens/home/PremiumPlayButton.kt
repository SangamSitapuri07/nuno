package com.nuno.app.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nuno.app.R

@Composable
fun PremiumPlayButton(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "playImageAnim")

    val scalePulse by infiniteTransition.animateFloat(
        1f, 1.05f,
        infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "scale"
    )

    val floatBounce by infiniteTransition.animateFloat(
        0f, -5f,
        infiniteRepeatable(tween(1300, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "bounce"
    )

    Box(
        modifier = Modifier
            .scale(scalePulse)
            .offset(y = floatBounce.dp)
            .size(width = 150.dp, height = 110.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_play_tile_3d),
            contentDescription = "PLAY",
            modifier = Modifier.fillMaxSize()
        )
    }
}