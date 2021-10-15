package com.tolgapirim.kdvhasaplama.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = GreenLight,
    primaryVariant = Green,
    secondary = PinkLight,
    secondaryVariant = Pink,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    surface = newBlack,
    onSurface = newWhite
)

private val LightColorPalette = lightColors(
    primary = Green,
    primaryVariant = GreenDark,
    secondary = Pink,
    secondaryVariant = PinkDark,
    surface = newWhite,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onSurface = newBlack

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun KDVHesaplamaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}