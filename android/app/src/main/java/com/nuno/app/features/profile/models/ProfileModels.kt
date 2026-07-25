package com.nuno.app.features.profile.models

import kotlinx.serialization.Serializable

@Serializable
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val currentProgress: Int,
    val maxProgress: Int,
    val isUnlocked: Boolean,
    val rewardCoins: Int = 0,
    val rewardXp: Int = 0,
    val rarity: String = "COMMON"
)

@Serializable
data class MatchHistoryItem(
    val matchId: String,
    val gameMode: String,
    val duration: Int,
    val isWinner: Boolean,
    val opponentName: String = "Unknown",
    val cardsPlayed: Int = 0,
    val ratingChange: Int,
    val xpEarned: Int,
    val playedAt: String
)

@Serializable
data class FavoriteCard(
    val cardId: String,
    val color: String,
    val value: String,
    val timesPlayed: Int
)

data class ProfileStats(
    val gamesPlayed: Int = 0,
    val gamesWon: Int = 0,
    val gamesLost: Int = 0,
    val winRate: Float = 0f,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val cardsPlayed: Int = 0,
    val cardsDrawn: Int = 0,
    val avgMatchTime: Int = 0,
    val mvpCount: Int = 0,
    val favoriteColor: String = "BLUE"
)