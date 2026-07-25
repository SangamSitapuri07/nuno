package com.nuno.app.features.voice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nuno.app.core.theme.AccentCyan
import com.nuno.app.core.theme.DangerRed
import com.nuno.app.core.theme.SurfaceDark
import com.nuno.app.core.theme.TextPrimary

@Composable
fun MutePlayerButton(
    playerId: String,
    voiceViewModel: VoiceViewModel = hiltViewModel()
) {
    val uiState by voiceViewModel.uiState.collectAsState()
    val isMuted = uiState.mutedPlayers.contains(playerId)

    Box(
        modifier = Modifier
            .size(28.dp)
            .background(
                if (isMuted) DangerRed else SurfaceDark,
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = { voiceViewModel.togglePlayerMute(playerId) },
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                contentDescription = if (isMuted) "Unmute" else "Mute",
                tint = if (isMuted) TextPrimary else AccentCyan,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun MicButton(
    voiceViewModel: VoiceViewModel = hiltViewModel()
) {
    val uiState by voiceViewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .size(44.dp)
            .background(
                if (uiState.isMicMuted) DangerRed else SurfaceDark,
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = { voiceViewModel.toggleMic() }
        ) {
            Icon(
                imageVector = if (uiState.isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                contentDescription = if (uiState.isMicMuted) "Unmute Mic" else "Mute Mic",
                tint = if (uiState.isMicMuted) TextPrimary else AccentCyan
            )
        }
    }
}

@Composable
fun SpeakerButton(
    voiceViewModel: VoiceViewModel = hiltViewModel()
) {
    val uiState by voiceViewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .size(44.dp)
            .background(
                if (uiState.isSpeakerMuted) DangerRed else SurfaceDark,
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = { voiceViewModel.toggleSpeaker() }
        ) {
            Icon(
                imageVector = if (uiState.isSpeakerMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                contentDescription = if (uiState.isSpeakerMuted) "Unmute Speaker" else "Mute Speaker",
                tint = if (uiState.isSpeakerMuted) TextPrimary else AccentCyan
            )
        }
    }
}