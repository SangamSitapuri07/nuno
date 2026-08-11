package com.nuno.app.screens.game
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.designsystem.GameColors
import kotlin.math.cos
import kotlin.math.sin
@Composable
fun UnoCard(
    card: GameCardData,
    size: CardSize = CardSize.MEDIUM,
    isPlayable: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val baseColor = when (card.color) {
        "RED" -> Color(0xFFE50914)
        "BLUE" -> Color(0xFF1E88E5)
        "GREEN" -> Color(0xFF2E7D32)
        "YELLOW" -> Color(0xFFFBC02D)
        else -> Color(0xFF212121)
    }
    val cornerText = when (card.value) {
        "SKIP" -> "⊘"
        "REVERSE" -> "⇄"
        "DRAW_TWO" -> "+2"
        "WILD" -> "W"
        "WILD_DRAW_FOUR" -> "+4"
        else -> card.value
    }
    Box(
        modifier = Modifier
            .size(width = size.width, height = size.height)
            .shadow(
                elevation = if (isPlayable) 12.dp else 4.dp,
                shape = RoundedCornerShape(8.dp),
                spotColor = if (isPlayable) GameColors.Cyan else Color.Black
            )
            .background(Color.Black, RoundedCornerShape(8.dp))
            .border(
                width = if (isPlayable) 3.dp else 1.5.dp,
                color = if (isPlayable) GameColors.Cyan else Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(2.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        // Inner Sunburst Oval Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height
            rotate(degrees = -28f, pivot = Offset(w / 2, h / 2)) {
                drawOval(
                    color = baseColor,
                    topLeft = Offset(w * 0.05f, h * 0.1f),
                    size = Size(w * 0.9f, h * 0.8f)
                )
                for (i in 0..18) {
                    val angleRad = Math.toRadians((i * 20).toDouble())
                    val rx = w / 2 + (cos(angleRad) * w * 0.42f).toFloat()
                    val ry = h / 2 + (sin(angleRad) * h * 0.38f).toFloat()
                    drawLine(
                        color = Color.White.copy(alpha = 0.15f),
                        start = Offset(w / 2, h / 2),
                        end = Offset(rx, ry),
                        strokeWidth = 2f
                    )
                }
                drawOval(
                    color = Color.White,
                    topLeft = Offset(w * 0.22f, h * 0.28f),
                    size = Size(w * 0.56f, h * 0.44f),
                    style = Stroke(width = 3f)
                )
                drawOval(
                    color = Color.White,
                    topLeft = Offset(w * 0.25f, h * 0.30f),
                    size = Size(w * 0.50f, h * 0.40f),
                    style = Stroke(width = 1.5f)
                )
            }
        }
        // Center Content Symbol/Text
        when (card.value) {
            "DRAW_TWO" -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy((-8).dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = (size.width.value * 0.28).dp, height = (size.height.value * 0.32).dp)
                            .shadow(2.dp, RoundedCornerShape(3.dp))
                            .background(Color(0xFFFBC02D), RoundedCornerShape(3.dp))
                            .border(1.dp, Color.White, RoundedCornerShape(3.dp))
                    )
                    Box(
                        modifier = Modifier
                            .size(width = (size.width.value * 0.28).dp, height = (size.height.value * 0.32).dp)
                            .shadow(4.dp, RoundedCornerShape(3.dp))
                            .background(Color(0xFFFBC02D), RoundedCornerShape(3.dp))
                            .border(1.dp, Color.Black, RoundedCornerShape(3.dp))
                    )
                }
            }
            "WILD_DRAW_FOUR" -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy((-10).dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val cols = listOf(GameColors.CardRed, GameColors.CardBlue, GameColors.CardGreen, GameColors.CardYellow)
                    cols.forEach { c ->
                        Box(
                            modifier = Modifier
                                .size(width = (size.width.value * 0.24).dp, height = (size.height.value * 0.28).dp)
                                .background(c, RoundedCornerShape(2.dp))
                                .border(1.dp, Color.White, RoundedCornerShape(2.dp))
                        )
                    }
                }
            }
            else -> {
                Text(
                    text = cornerText,
                    color = Color.White,
                    fontSize = (size.fontSize * 1.1f).sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
        // Top-Left Corner
        Text(
            text = cornerText,
            color = Color.White,
            fontSize = (size.fontSize / 2.6f).sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 3.dp, start = 4.dp)
        )
        // Bottom-Right Corner (Inverted)
        Text(
            text = cornerText,
            color = Color.White,
            fontSize = (size.fontSize / 2.6f).sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .rotate(180f)
                .padding(top = 3.dp, start = 4.dp)
        )
    }
}