package com.nuno.app.features.friends

import kotlinx.serialization.Serializable

@Serializable
data class Friend(
    val friendshipId: String,
    val userId: String,
    val username: String,
    val avatarUrl: String? = null,
    val status: String,
    val lastOnline: String? = null,
    val currentRank: String = "BRONZE",
    val currentGame: String? = null,
    val isInVoice: Boolean = false
)

@Serializable
data class FriendRequest(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val status: String,
    val createdAt: String,
    val sender: FriendUser
)

@Serializable
data class FriendUser(
    val id: String,
    val username: String,
    val avatarUrl: String? = null
)

@Serializable
data class PlayerSearchResult(
    val id: String,
    val username: String,
    val avatarUrl: String? = null,
    val rankPoints: Int
)

@Serializable
data class SendFriendRequestBody(
    val playerId: String
)

@Serializable
data class RequestActionBody(
    val requestId: String
)