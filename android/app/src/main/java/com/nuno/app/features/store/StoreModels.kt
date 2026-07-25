package com.nuno.app.features.store

import kotlinx.serialization.Serializable

@Serializable
data class StoreItem(
    val itemId: String,
    val name: String,
    val description: String,
    val type: String,
    val rarity: String,
    val price: Int,
    val currency: String,
    val imageUrl: String? = null,
    val isAvailable: Boolean = true,
    val originalPrice: Int? = null,
    val discountPercent: Int = 0,
    val isLimited: Boolean = false,
    val timeRemaining: Long? = null
)

@Serializable
data class PurchaseRequest(
    val itemId: String,
    val cosmeticType: String,
    val currency: String,
    val price: Int
)

@Serializable
data class Balance(
    val coins: Int,
    val gems: Int = 0
)

enum class ShopCategory(val label: String, val icon: String) {
    FEATURED("Featured", "⭐"),
    OFFERS("Offers", "🔥"),
    CARDS("Cards", "🎴"),
    BUNDLES("Bundles", "🎁"),
    COSMETICS("Cosmetics", "👑"),
    EFFECTS("Effects", "✨"),
    EMOTES("Emotes", "😀")
}