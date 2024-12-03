package net.wh64.chain.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DefaultColorScheme = darkColorScheme(
    primary = Palette1,
    secondary = Palette2,
    tertiary = RootPalette,
    background = background,
    surface = background,
    onBackground = foreground,
    onSurface = foreground,
    onPrimary = foreground,
    onSecondary = foreground,
    onTertiary = foreground,
)

@Composable
fun ChainTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DefaultColorScheme,
        typography = Typography,
        content = content
    )
}