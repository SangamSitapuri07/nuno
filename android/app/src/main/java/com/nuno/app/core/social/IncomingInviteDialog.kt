package com.nuno.app.core.social

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun IncomingInviteDialog(
    invite: IncomingInvite,
    onAccept: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Game invite") },
        text = { Text("${invite.fromUsername} invited you to a room.") },
        confirmButton = {
            TextButton(onClick = onAccept) { Text("Join") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Decline") }
        }
    )
}