package com.nuno.app.core.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = NeutralWhite,
    primaryContainer = PrimaryPurple,
    onPrimaryContainer = NeutralWhite,
    secondary = AccentCyan,
    onSecondary = NeutralBlack,
    tertiary = AccentPink,
    onTertiary = NeutralWhite,
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceLight,
    onSurfaceVariant = TextSecondary,
    error = DangerRed,
    onError = NeutralWhite,
    outline = NeutralGray600,
    outlineVariant = NeutralGray700
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = NeutralWhite,
    primaryContainer = PrimaryPurple,
    onPrimaryContainer = NeutralWhite,
    secondary = AccentCyan,
    onSecondary = NeutralBlack,
    tertiary = AccentPink,
    onTertiary = NeutralWhite,
    background = NeutralGray50,
    onBackground = NeutralGray900,
    surface = NeutralWhite,
    onSurface = NeutralGray900,
    surfaceVariant = NeutralGray100,
    onSurfaceVariant = NeutralGray700,
    error = DangerRed,
    onError = NeutralWhite,
    outline = NeutralGray300,
    outlineVariant = NeutralGray200
)

@Composable
fun NUNOTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}