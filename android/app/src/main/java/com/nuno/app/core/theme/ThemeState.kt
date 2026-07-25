package com.nuno.app.core.theme

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ThemeManager {
    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
    }
}

@Composable
fun rememberThemeColors(): ThemeColors {
    val isDark by ThemeManager.isDarkMode.collectAsState()
    return remember(isDark) {
        if (isDark) darkThemeColors() else lightThemeColors()
    }
}

data class ThemeColors(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val textPrimary: Color,
    val textSecondary: Color
)

private fun darkThemeColors() = ThemeColors(
    background = Color(0xFF0F1123),
    surface = Color(0xFF1A1D33),
    primary = Color(0xFF4361EE),
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFF8B92B8)
)

private fun lightThemeColors() = ThemeColors(
    background = Color(0xFFF5F5FA),
    surface = Color(0xFFFFFFFF),
    primary = Color(0xFF4361EE),
    textPrimary = Color(0xFF212529),
    textSecondary = Color(0xFF6C757D)
)