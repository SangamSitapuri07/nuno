package com.nuno.app.features.history.models

import kotlinx.serialization.Serializable

@Serializable
data class MatchRecord(
    val matchId: String,
    val gameMode: String,
    val opponentName: String,
    val opponentAvatar: String? = null,
    val duration: Int,
    val cardsPlayed: Int,
    val isWinner: Boolean,
    val ratingChange: Int,
    val coinsEarned: Int,
    val xpEarned: Int,
    val playedAt: String,
    val playerCount: Int = 2
)

enum class HistoryFilter(val label: String) {
    ALL("All"),
    WINS("Wins Only"),
    LOSSES("Losses Only"),
    CASUAL("Casual"),
    RANKED("Ranked"),
    PRIVATE("Private")
}