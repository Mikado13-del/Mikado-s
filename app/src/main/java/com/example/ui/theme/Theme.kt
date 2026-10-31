package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = EduBlueDarkPrimary,
  onPrimary = Color(0xFF003258),
  primaryContainer = Color(0xFF00497D),
  onPrimaryContainer = Color(0xFFD1E4FF),
  secondary = EduAmber,
  onSecondary = Color(0xFF452B00),
  secondaryContainer = Color(0xFF633F00),
  onSecondaryContainer = Color(0xFFFFDDB3),
  tertiary = EduEmerald,
  onTertiary = Color(0xFF003822),
  background = DarkBackground,
  onBackground = DarkTextPrimary,
  surface = DarkSurface,
  onSurface = DarkTextPrimary,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = DarkTextSecondary,
  outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
  primary = EduBluePrimary,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFD1E4FF),
  onPrimaryContainer = Color(0xFF001D36),
  secondary = EduAmber,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFFFDDB3),
  onSecondaryContainer = Color(0xFF2A1800),
  tertiary = EduEmerald,
  onTertiary = Color.White,
  background = LightBackground,
  onBackground = LightTextPrimary,
  surface = LightSurface,
  onSurface = LightTextPrimary,
  surfaceVariant = LightSurfaceVariant,
  onSurfaceVariant = LightTextSecondary,
  outline = LightBorder
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep consistent branding colors by default
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
