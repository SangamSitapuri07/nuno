package com.nuno.app.core.social

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun IncomingInviteDialog(
    invite: IncomingInvite,
    onAccept: () -> Unit,
    onDismiss: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "inviteGlow")
    val pulseGlow by infiniteTransition.animateFloat(
        0.4f, 1f,
        infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulse"
    )

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .fillMaxHeight(0.85f)
                .shadow(20.dp, RoundedCornerShape(18.dp), spotColor = Color(0xFFFFD700).copy(alpha = pulseGlow))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E2548),
                            Color(0xFF121630),
                            Color(0xFF0D1024)
                        )
                    ),
                    RoundedCornerShape(18.dp)
                )
                .border(
                    2.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFFD700).copy(alpha = pulseGlow),
                            Color(0xFF4CC9F0).copy(alpha = 0.8f),
                            Color(0xFFFFD700).copy(alpha = pulseGlow)
                        )
                    ),
                    RoundedCornerShape(18.dp)
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 48.dp, height = 24.dp)
                            .background(Color(0xFFE50914), RoundedCornerShape(12.dp))
                            .border(1.2.dp, Color(0xFFFBC02D), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("NUNO", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
                    }

                    Box(
                        modifier = Modifier
                            .background(Color(0xFF0F142A), CircleShape)
                            .border(1.dp, Color(0xFF4CC9F0).copy(alpha = 0.5f), CircleShape)
                            .padding(4.dp)
                    ) {
                        Icon(Icons.Default.Gamepad, null, tint = Color(0xFF4CC9F0), modifier = Modifier.size(14.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "GAME INVITATION",
                    color = Color(0xFFFFD700),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        com.nuno.app.core.designsystem.components.GameAvatar(
                            username = invite.fromUsername,
                            size = 36.dp,
                            borderColor = Color(0xFF4CC9F0)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = invite.fromUsername,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "invited you to join a NUNO match!",
                            color = Color(0xFFA0B0D0),
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .background(Color(0xFF0F142A), RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "ROOM CODE: ${invite.roomCode}",
                        color = Color(0xFFFFD700),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("DECLINE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
                        }
                    }

                    Button(
                        onClick = onAccept,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .shadow(6.dp, RoundedCornerShape(10.dp), spotColor = Color(0xFF4CAF50))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("JOIN", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}