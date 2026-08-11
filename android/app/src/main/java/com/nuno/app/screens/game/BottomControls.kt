package com.nuno.app.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomControls(
    remainingTime: Int,
    onEmotes: () -> Unit,
    onUnoCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Bottom Left Emote Button
        IconButton(
            onClick = onEmotes,
            modifier = Modifier
                .size(44.dp)
                .background(Color(0xFF0F142A).copy(alpha = 0.85f), CircleShape)
        ) {
            Text("😀", fontSize = 24.sp)
        }

        // Bottom Right Timer & NUNO! Button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Timer(remainingTime = remainingTime)

            Button(
                onClick = onUnoCall,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3D00)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .height(48.dp)
                    .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = Color(0xFFFF3D00))
            ) {
                Text("UNO!", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
            }
        }
    }
}