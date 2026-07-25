package com.nuno.app.features.settings.models

enum class SettingsCategory(val label: String, val icon: String) {
    GENERAL("General", "⚙️"),
    GRAPHICS("Graphics", "🎨"),
    AUDIO("Audio", "🔊"),
    GAMEPLAY("Gameplay", "🎮"),
    CONTROLS("Controls", "🎯"),
    PRIVACY("Privacy", "🔒"),
    NOTIFICATIONS("Notifications", "🔔"),
    HELP("Help", "❓"),
    ABOUT("About", "ℹ️")
}

data class SettingItem(
    val id: String,
    val label: String,
    val type: SettingType,
    val value: Any = false
)

enum class SettingType {
    SWITCH,
    SLIDER,
    DROPDOWN,
    BUTTON
}