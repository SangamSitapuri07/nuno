package com.nuno.app.features.lobby

import kotlinx.serialization.Serializable

@Serializable
data class LobbyRoom(
    val roomId: String,
    val roomCode: String,
    val hostId: String,
    val players: List<LobbyPlayer>,
    val maxPlayers: Int,
    val currentPlayers: Int,
    val voiceEnabled: Boolean,
    val chatEnabled: Boolean,
    val gameMode: String,
    val status: String,
    val createdAt: Long
)

@Serializable
data class LobbyPlayer(
    val userId: String,
    val username: String,
    val avatarUrl: String? = null,
    val isReady: Boolean = false,
    val isHost: Boolean = false,
    val isVoiceConnected: Boolean = false,
    val ping: Int = 0,
    val joinedAt: Long = 0
)

@Serializable
data class ChatMessage(
    val userId: String,
    val username: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)