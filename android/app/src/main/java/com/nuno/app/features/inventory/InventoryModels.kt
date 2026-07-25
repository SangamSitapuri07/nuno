package com.nuno.app.features.inventory.models

import kotlinx.serialization.Serializable

@Serializable
data class InventoryItem(
    val id: String,
    val cosmeticId: String,
    val cosmeticType: String,
    val name: String = "Item",
    val rarity: String = "COMMON",
    val quantity: Int = 1,
    val isFavorite: Boolean = false,
    val isEquipped: Boolean = false,
    val unlockedAt: String
)

enum class InventoryTab(val label: String, val icon: String) {
    ALL("All", "📦"),
    CARDS("Cards", "🎴"),
    AVATARS("Avatars", "👤"),
    FRAMES("Frames", "🖼️"),
    EMOTES("Emotes", "😀"),
    TITLES("Titles", "🏆"),
    EFFECTS("Effects", "✨"),
    THEMES("Themes", "🎨")
}

enum class InventoryFilter(val label: String) {
    ALL("All"),
    COMMON("Common"),
    RARE("Rare"),
    EPIC("Epic"),
    LEGENDARY("Legendary"),
    MYTHIC("Mythic")
}

enum class InventorySort(val label: String) {
    NEWEST("Newest"),
    OLDEST("Oldest"),
    RARITY("Rarity"),
    NAME("Name")
}