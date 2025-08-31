package com.tibolatte.milbadge.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Dore,
    onPrimary = Color.White,
    secondary = RosePastel,
    onSecondary = TexteMarron,
    tertiary = LavandePastel,
    onTertiary = TexteMarron,
    background = Creme,
    onBackground = TexteMarron,
    surface = Creme,
    onSurface = TexteMarron,
    error = Color(0xFFB00020),
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = Dore,
    onPrimary = Color.Black,
    secondary = RosePastel,
    onSecondary = Color.Black,
    tertiary = LavandePastel,
    onTertiary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

@Composable
fun MilBadgeTheme(
    useDarkTheme: Boolean = false, // <-- on force le clair pour coller au mockup
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (useDarkTheme) DarkColors else LightColors,
        typography = RomanticTypography,
        content = content
    )
}
