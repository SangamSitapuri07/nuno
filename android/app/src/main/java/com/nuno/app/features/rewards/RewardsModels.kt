package com.nuno.app.features.rewards.models

data class RewardChest(
    val id: String,
    val name: String,
    val rarity: String,
    val isOpen: Boolean = false,
    val timeUntilNext: Long = 0
)

data class DailyReward(
    val day: Int,
    val icon: String,
    val amount: Int,
    val type: String,
    val isClaimed: Boolean,
    val isToday: Boolean
)

enum class RewardTab(val label: String, val icon: String) {
    DAILY("Daily", "📅"),
    SEASON("Season", "🏆"),
    ACHIEVEMENTS("Achievements", "🎯"),
    CHESTS("Chests", "📦")
}