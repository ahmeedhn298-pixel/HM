package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val TogetherDarkColorScheme =
  darkColorScheme(
    primary = TogetherBlue,
    onPrimary = TextPrimary,
    primaryContainer = TogetherBlueMuted,
    onPrimaryContainer = TextPrimary,
    secondary = NavySurfaceElevated,
    onSecondary = TextPrimary,
    secondaryContainer = NavySurfaceMuted,
    onSecondaryContainer = TextSecondary,
    tertiary = TogetherBlueBright,
    onTertiary = TextPrimary,
    background = NavyBackground,
    onBackground = TextPrimary,
    surface = NavySurface,
    onSurface = TextPrimary,
    surfaceVariant = NavySurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = NavyBorder,
    outlineVariant = NavySurfaceMuted,
  )

@Composable
fun TogetherTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = TogetherDarkColorScheme,
    typography = TogetherTypography,
    content = content,
  )
}
