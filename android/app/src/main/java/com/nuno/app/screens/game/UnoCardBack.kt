package com.nuno.app.screens.game
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin
@Composable
fun UnoCardBack(size: CardSize = CardSize.MEDIUM) {
    Box(
        modifier = Modifier
            .size(width = size.width, height = size.height)
            .shadow(6.dp, RoundedCornerShape(8.dp))
            .background(Color.Black, RoundedCornerShape(8.dp))
            .border(1.5.dp, Color.White, RoundedCornerShape(8.dp))
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        // Red Sunburst Oval
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height
            rotate(degrees = -30f, pivot = Offset(w / 2, h / 2)) {
                drawOval(
                    color = Color(0xFFE50914),
                    topLeft = Offset(w * 0.05f, h * 0.08f),
                    size = Size(w * 0.9f, h * 0.84f)
                )
                for (i in 0..18) {
                    val angleRad = Math.toRadians((i * 20).toDouble())
                    val rx = w / 2 + (cos(angleRad) * w * 0.44f).toFloat()
                    val ry = h / 2 + (sin(angleRad) * h * 0.40f).toFloat()
                    drawLine(
                        color = Color.White.copy(alpha = 0.15f),
                        start = Offset(w / 2, h / 2),
                        end = Offset(rx, ry),
                        strokeWidth = 2f
                    )
                }
            }
        }
        // Tilted NUNO Logo with Yellow 3D Letters
        Box(
            modifier = Modifier.rotate(-28f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "NUNO®",
                color = Color(0xFFFBC02D),
                fontSize = (size.fontSize * 0.75f).sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                modifier = Modifier.shadow(4.dp)
            )
        }
    }
}