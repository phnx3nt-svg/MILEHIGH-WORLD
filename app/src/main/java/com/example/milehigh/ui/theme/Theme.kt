package com.example.milehigh.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = VoidBlack,
    primaryContainer = VoidSurfaceVariant,
    onPrimaryContainer = NeonCyan,
    secondary = ArcaneViolet,
    onSecondary = VoidBlack,
    secondaryContainer = ArcanePurple,
    onSecondaryContainer = TextPrimary,
    tertiary = RadiantGold,
    onTertiary = VoidBlack,
    tertiaryContainer = VoidSurfaceVariant,
    onTertiaryContainer = RadiantGold,
    background = VoidDark,
    onBackground = TextPrimary,
    surface = VoidDark,
    onSurface = TextPrimary,
    surfaceVariant = VoidSurface,
    onSurfaceVariant = TextSecondary,
    outline = VoidBorder,
    error = VoidfireCrimson,
    onError = TextPrimary
)

@Composable
fun MilehighTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
