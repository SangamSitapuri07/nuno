package com.nuno.app.core.designsystem

import androidx.compose.ui.graphics.Color

object GameColors {
    // === DEEP SPACE PREMIUM BACKGROUND ===
    val Background = Color(0xFF06071A)
    val BackgroundDark = Color(0xFF030410)
    val BackgroundLight = Color(0xFF0F1230)
    val BackgroundGradientTop = Color(0xFF12143D)
    val BackgroundGradientBottom = Color(0xFF040514)

    // Surfaces - Glassmorphic, premium
    val Surface = Color(0xFF161A38)
    val SurfaceLight = Color(0xFF1E2249)
    val SurfaceCard = Color(0xFF131636)
    val SurfaceDialog = Color(0xFF181C40)
    val SurfaceGlass = Color(0x221A1F4A)
    val SurfaceGlassStrong = Color(0x66131A40)

    // Accent Colors - Premium vibrant
    val Gold = Color(0xFFFFC71F)
    val GoldDark = Color(0xFFD49F00)
    val GoldLight = Color(0xFFFFD86B)
    val Purple = Color(0xFF7B5CFF)
    val PurpleDark = Color(0xFF5A35CC)
    val PurpleLight = Color(0xFFA58FFF)
    val Blue = Color(0xFF3B6BFF)
    val BlueDark = Color(0xFF254EDB)
    val Cyan = Color(0xFF00D9FF)
    val CyanLight = Color(0xFF66EBFF)
    val Green = Color(0xFF00E676)
    val GreenDark = Color(0xFF00B359)
    val Red = Color(0xFFFF3B5C)
    val RedDark = Color(0xFFCC2E4A)
    val Orange = Color(0xFFFF8A00)
    val Pink = Color(0xFFFF3D8B)

    // UNO Card Colors - Premium saturated
    val CardRed = Color(0xFFE53935)
    val CardRedBright = Color(0xFFFF1744)
    val CardBlue = Color(0xFF1E88E5)
    val CardBlueBright = Color(0xFF2979FF)
    val CardGreen = Color(0xFF43A047)
    val CardGreenBright = Color(0xFF00E676)
    val CardYellow = Color(0xFFFFC400)
    val CardYellowBright = Color(0xFFFFEA00)
    val CardBlack = Color(0xFF1A1A29)
    val CardBlackLight = Color(0xFF2C2C40)

    // Text - Premium high contrast
    val TextWhite = Color(0xFFFFFFFF)
    val TextWhiteAlt = Color(0xFFF5F6FF)
    val TextGray = Color(0xFF8B92C0)
    val TextGrayLight = Color(0xFFB0B8E0)
    val TextDark = Color(0xFF4A5070)
    val TextDisabled = Color(0xFF2E3454)
    val TextGold = Gold

    // Borders - Glows
    val BorderGold = Color(0xFFFFC71F)
    val BorderPurple = Color(0xFF5C4DB1)
    val BorderBlue = Color(0xFF3A5BCC)
    val BorderGlow = Color(0xFF4CC9F0)
    val BorderCyan = Color(0xFF00D9FF)
    val BorderGlass = Color(0x33FFFFFF)
    val BorderGlassStrong = Color(0x55FFFFFF)

    // Status
    val Online = Color(0xFF00E676)
    val Offline = Color(0xFF556080)
    val InGame = Color(0xFFFFC71F)
    val InLobby = Color(0xFF00D9FF)
    val Away = Color(0xFFFF8A00)

    // Bottom Nav
    val NavActive = Color(0xFFFFFFFF)
    val NavInactive = Color(0xFF5A6488)
    val NavBackground = Color(0xFF0A0C22)
    val NavBackgroundGradientTop = Color(0xFF131735)
    val NavBackgroundGradientBottom = Color(0xFF070818)
    val NavIndicator = Color(0xFF3B6BFF)

    // Gradients
    val GradientPrimary = listOf(Blue, Purple)
    val GradientGold = listOf(Gold, Orange)
    val GradientCyan = listOf(Cyan, Blue)
    val GradientGreen = listOf(Green, Color(0xFF00BFA5))
    val GradientRed = listOf(Red, Color(0xFFD50000))
    val GradientPurple = listOf(Purple, Color(0xFF651FFF))
    val GradientNuno = listOf(Color(0xFFFF1744), Color(0xFF2979FF), Color(0xFF00E676), Color(0xFFFFEA00))

    // Premium glass
    val GlassWhite10 = Color(0x1AFFFFFF)
    val GlassWhite15 = Color(0x26FFFFFF)
    val GlassWhite05 = Color(0x0DFFFFFF)
    val GlassGold10 = Color(0x1AFFC71F)
    val GlassBlue10 = Color(0x1A3B6BFF)

    // Shadows
    val ShadowBlack = Color(0x66000000)
    val ShadowBlue = Color(0x443B6BFF)
    val ShadowPurple = Color(0x447B5CFF)
    val ShadowGold = Color(0x44FFC71F)
    val ShadowCyan = Color(0x4400D9FF)
}
