package com.nuno.app.features.friends.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuno.app.core.theme.*
import com.nuno.app.features.friends.FriendRequest

@Composable
fun FriendRequestCard(
    request: FriendRequest,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, AccentGold.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.radialGradient(colors = listOf(PrimaryBlue, PrimaryPurple)),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = request.sender.username.firstOrNull()?.uppercase() ?: "?",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = request.sender.username, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(text = "wants to be your friend", color = TextSecondary, fontSize = 11.sp)
            }

            IconButton(onClick = onAccept, modifier = Modifier.size(40.dp).background(SuccessGreen, CircleShape)) {
                Icon(Icons.Default.Check, null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(6.dp))
            IconButton(onClick = onReject, modifier = Modifier.size(40.dp).background(DangerRed, CircleShape)) {
                Icon(Icons.Default.Close, null, tint = Color.White)
            }
        }
    }
}