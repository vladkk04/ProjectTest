package com.klymiuk.scratchcard.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    onPrimary = Color.White,
    primaryContainer = Sky,
    onPrimaryContainer = Navy,
    error = Red,
)

private val DarkColorScheme = darkColorScheme(
    primary = LightBlue,
    onPrimary = Navy,
    primaryContainer = Navy,
    onPrimaryContainer = Sky,
    error = LightRed,
)

@Immutable
data class CardColors(
    val unscratched: Color,
    val scratched: Color,
    val activated: Color,
)

val LocalCardColors = staticCompositionLocalOf {
    CardColors(unscratched = Silver, scratched = Blue, activated = Green)
}

@Composable
fun ScratchCardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content,
    )
}

object ScratchCardTheme {
    val cardColors: CardColors
        @Composable get() = LocalCardColors.current
}
