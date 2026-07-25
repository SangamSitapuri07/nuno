package com.nuno.app.core.network

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String? = null,
    val level: Int,
    val xp: Int,
    val coins: Int,
    val rankPoints: Int,
    val accountStatus: String,
    val createdAt: String,
    val lastLogin: String? = null,
    val statistics: UserStatistics? = null,
    val leaderboard: UserLeaderboard? = null
)

@Serializable
data class UserStatistics(
    val gamesPlayed: Int,
    val gamesWon: Int,
    val gamesLost: Int,
    val winRate: Float,
    val longestWinStreak: Int,
    val currentWinStreak: Int,
    val cardsPlayed: Int,
    val cardsDrawn: Int
)

@Serializable
data class UserLeaderboard(
    val rating: Int,
    val tier: String,
    val division: String,
    val season: Int
)

@Serializable
data class UpdateProfileRequest(
    val username: String? = null,
    val avatarUrl: String? = null
)
@Serializable
data class MatchHistoryData(
    val matchId: String,
    val gameMode: String,
    val duration: Int,
    val startedAt: String,
    val endedAt: String,
    val isWinner: Boolean,
    val finalPosition: Int,
    val ratingChange: Int,
    val xpEarned: Int
)

@Serializable
data class UserSettings(
    val id: String,
    val playerId: String,
    val language: String,
    val musicVolume: Int,
    val soundVolume: Int,
    val voiceVolume: Int,
    val pushToTalk: Boolean,
    val notifications: Boolean,
    val darkMode: Boolean
)

@Serializable
data class UpdateSettingsRequest(
    val language: String? = null,
    val musicVolume: Int? = null,
    val soundVolume: Int? = null,
    val voiceVolume: Int? = null,
    val pushToTalk: Boolean? = null,
    val notifications: Boolean? = null,
    val darkMode: Boolean? = null
)