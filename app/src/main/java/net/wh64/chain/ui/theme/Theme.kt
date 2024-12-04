package net.wh64.chain.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DefaultColorScheme = darkColorScheme(
    primary = Palette1,
    secondary = Palette2,
    tertiary = RootPalette,
    background = Background,
    surface = Background,
    onBackground = Foreground,
    onSurface = Foreground,
    onPrimary = Foreground,
    onSecondary = Foreground,
    onTertiary = Foreground,
)

@Composable
fun ChainTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DefaultColorScheme,
        typography = Typography,
        content = content
    )
}