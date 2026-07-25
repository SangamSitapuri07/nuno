package com.nuno.app.features.notifications

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: String,
    val playerId: String,
    val title: String,
    val message: String,
    val read: Boolean,
    val createdAt: String,
    val type: String = "SYSTEM"
)

enum class NotificationTab(val label: String, val icon: String) {
    ALL("All", "📬"),
    GAME("Game", "🎮"),
    FRIENDS("Friends", "👥"),
    REWARDS("Rewards", "🎁"),
    SYSTEM("System", "⚙️")
}