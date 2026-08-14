package com.nuno.app.core.designsystem

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object GameTypography {
    // Display - Hero
    val displayHuge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 48.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 3.sp,
        lineHeight = 52.sp
    )

    val displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 36.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 2.sp,
        lineHeight = 40.sp
    )

    val displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 28.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.5.sp,
        lineHeight = 32.sp
    )

    val displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 24.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.sp,
        lineHeight = 28.sp
    )

    // Headline
    val headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp
    )

    val headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.3.sp
    )

    val headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
    )

    // Title
    val titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.2.sp
    )

    val titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold
    )

    val titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.5.sp
    )

    // Body
    val bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 22.sp
    )

    val bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp
    )

    val bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 11.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 14.sp
    )

    // Label - Premium tight
    val labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
    )

    val labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp
    )

    val labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 9.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.5.sp
    )

    val caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 9.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.5.sp
    )

    // Button - Premium
    val buttonLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 2.sp
    )

    val button = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 14.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 1.5.sp
    )

    val buttonSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
    )

    // Gaming specific
    val gameTitle = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 32.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 4.sp
    )

    val cardValue = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 24.sp,
        fontWeight = FontWeight.Black
    )

    val rankNumber = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 18.sp,
        fontWeight = FontWeight.Black
    )
}
