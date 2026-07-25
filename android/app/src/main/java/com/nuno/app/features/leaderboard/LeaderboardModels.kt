package com.nuno.app.features.leaderboard

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardEntry(
    val rank: Int,
    val userId: String,
    val username: String,
    val avatarUrl: String? = null,
    val rating: Int,
    val tier: String,
    val division: String,
    val wins: Int,
    val country: String = "🌍",
    val rankChange: Int = 0
)

enum class LeaderboardTab(val label: String) {
    GLOBAL("Global"),
    REGIONAL("Regional"),
    FRIENDS("Friends"),
    WEEKLY("Weekly"),
    SEASON("Season")
}