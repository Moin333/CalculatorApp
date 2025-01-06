package com.example.calculator.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Dark Mode Colors
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark, // Primary color
    secondary = SecondaryDark, // Secondary color
    tertiary = TertiaryDark, // Tertiary color
    background = Color.Black, // Background color
    onBackground = Color.White, // Text or content on background
    surface = Color(0xFF191A19), // Elevated surfaces like cards
    onSurface = Color.White, // Text or content on surface
    surfaceVariant = Color.DarkGray
)

// Light Mode Colors
private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight, // Primary color
    secondary = SecondaryLight, // Secondary color
    tertiary = TertiaryLight, // Tertiary color
    background = Color(0xFFF5F5F5), // Slightly darker than white
    onBackground = Color.Black, // Text or content on background
    surface = Color.White, // Elevated surfaces like cards
    onSurface = Color.Black, // Text or content on surface
    surfaceVariant = Color(0xFFF5F5F5)
)

@Composable
fun CalculatorTheme(
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
