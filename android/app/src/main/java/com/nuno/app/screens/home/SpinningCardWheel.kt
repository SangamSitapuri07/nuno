package com.nuno.app.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nuno.app.R

@Composable
fun SpinningCardWheel(
    modifier: Modifier = Modifier,
    wheelSize: Dp = 220.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pedestalImageAnim")

    val scalePulse by infiniteTransition.animateFloat(
        1f, 1.03f,
        infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "scale"
    )

    val floatBounce by infiniteTransition.animateFloat(
        0f, -5f,
        infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "bounce"
    )

    Box(
        modifier = modifier
            .scale(scalePulse)
            .offset(y = floatBounce.dp)
            .size(width = 280.dp, height = 200.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_card_pedestal_3d),
            contentDescription = "NUNO Card Pedestal",
            modifier = Modifier.fillMaxSize()
        )
    }
}