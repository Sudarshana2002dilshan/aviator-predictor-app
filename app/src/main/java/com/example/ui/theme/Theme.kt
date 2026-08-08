package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AviatorDarkColorScheme = darkColorScheme(
  primary = AviatorRedPrimary,
  onPrimary = TextPrimary,
  primaryContainer = AviatorSurface,
  onPrimaryContainer = TextPrimary,
  secondary = AviatorGold,
  onSecondary = AviatorDarkBg,
  tertiary = AviatorCyan,
  background = AviatorDarkBg,
  onBackground = TextPrimary,
  surface = AviatorSurface,
  onSurface = TextPrimary,
  surfaceVariant = AviatorCardBg,
  onSurfaceVariant = TextSecondary,
  outline = AviatorCardBorder
)

@Composable
fun AviatorTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = AviatorDarkColorScheme,
    typography = Typography,
    content = content
  )
}
