package com.nuno.app.screens.game
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nuno.app.R

@Composable
fun UnoCardBack(size: CardSize = CardSize.MEDIUM) {
    Box(
        modifier = Modifier
            .size(width = size.width, height = size.height)
            .shadow(8.dp, RoundedCornerShape(10.dp), spotColor = Color.Black.copy(0.6f))
            .background(Color.Black, RoundedCornerShape(10.dp))
            .border(1.5.dp, Color.White, RoundedCornerShape(10.dp))
            .padding(2.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_card_back_nuno_premium),
            contentDescription = "NUNO Card Back",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
