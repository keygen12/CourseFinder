package com.example.coursefinder.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    tertiary = AccentYellow,
    background = SurfaceLight,
    surface = SurfaceLight,
    onPrimary = Color.White,
    onBackground = OnSurfaceDark,
    onSurface = OnSurfaceDark
)

@Composable
fun CourseFinderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}
