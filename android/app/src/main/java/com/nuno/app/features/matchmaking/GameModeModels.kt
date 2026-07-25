package com.nuno.app.features.matchmaking.models

import androidx.compose.ui.graphics.Color
import com.nuno.app.core.theme.*

data class GameMode(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val difficulty: String,
    val estimatedDuration: String,
    val playerCount: String,
    val coinReward: Int,
    val xpReward: Int,
    val queueTime: String,
    val minRank: String,
    val isLimitedTime: Boolean = false,
    val isLocked: Boolean = false,
    val gradient: List<Color>
)

object GameModes {
    val all = listOf(
        GameMode(
            id = "casual",
            name = "CASUAL",
            description = "Play for fun with random players. No rating impact.",
            icon = "🎮",
            difficulty = "Easy",
            estimatedDuration = "5-10 min",
            playerCount = "2-4",
            coinReward = 20,
            xpReward = 75,
            queueTime = "~30s",
            minRank = "Any",
            gradient = listOf(PrimaryBlue, AccentCyan)
        ),
        GameMode(
            id = "ranked",
            name = "RANKED",
            description = "Competitive matches that affect your rating. Climb the leaderboard!",
            icon = "🏆",
            difficulty = "Hard",
            estimatedDuration = "8-15 min",
            playerCount = "2",
            coinReward = 50,
            xpReward = 150,
            queueTime = "~1 min",
            minRank = "Bronze+",
            gradient = listOf(PrimaryPurple, AccentPink)
        ),
        GameMode(
            id = "quick",
            name = "QUICK MATCH",
            description = "Fast matches with reduced timers. Perfect for short breaks.",
            icon = "⚡",
            difficulty = "Medium",
            estimatedDuration = "3-5 min",
            playerCount = "2-4",
            coinReward = 15,
            xpReward = 50,
            queueTime = "~20s",
            minRank = "Any",
            gradient = listOf(WarningOrange, AccentGold)
        ),
        GameMode(
            id = "custom",
            name = "CUSTOM ROOM",
            description = "Create a private room with custom rules and invite friends.",
            icon = "🎯",
            difficulty = "Custom",
            estimatedDuration = "Variable",
            playerCount = "2-10",
            coinReward = 0,
            xpReward = 25,
            queueTime = "Instant",
            minRank = "Any",
            gradient = listOf(SuccessGreen, AccentCyan)
        ),
        GameMode(
            id = "tournament",
            name = "TOURNAMENT",
            description = "Compete in scheduled tournaments for exclusive rewards.",
            icon = "👑",
            difficulty = "Expert",
            estimatedDuration = "1 hour",
            playerCount = "8-16",
            coinReward = 500,
            xpReward = 1000,
            queueTime = "Scheduled",
            minRank = "Silver+",
            isLimitedTime = true,
            gradient = listOf(AccentGold, WarningOrange)
        ),
        GameMode(
            id = "practice",
            name = "PRACTICE",
            description = "Play against AI to sharpen your skills. No rewards.",
            icon = "🎓",
            difficulty = "Adjustable",
            estimatedDuration = "5 min",
            playerCount = "1v1 AI",
            coinReward = 0,
            xpReward = 10,
            queueTime = "Instant",
            minRank = "Any",
            gradient = listOf(NeutralGray500, NeutralGray600)
        )
    )
}