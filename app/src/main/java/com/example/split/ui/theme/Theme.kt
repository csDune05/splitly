package com.example.split.ui.theme

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
    primary = SplitlyColors.PrimaryLight,
    secondary = SplitlyColors.Accent,
    tertiary = SplitlyColors.Indigo,
    background = SplitlyColors.TextPrimary,
    surface = SplitlyColors.TextPrimary,
    onPrimary = SplitlyColors.White,
    onSecondary = SplitlyColors.White,
    onTertiary = SplitlyColors.White,
)

private val LightColorScheme = lightColorScheme(
    primary = SplitlyColors.Primary,
    secondary = SplitlyColors.PrimaryLight,
    tertiary = SplitlyColors.Accent,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = SplitlyColors.TextPrimary,
    onSurface = SplitlyColors.TextPrimary,
)

@Composable
fun SplitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
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
