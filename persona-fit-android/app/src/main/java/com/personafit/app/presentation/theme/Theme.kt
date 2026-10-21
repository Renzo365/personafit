package com.personafit.app.presentation.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ObsidianColorScheme = darkColorScheme(
    primary = ElectricMint,
    onPrimary = TextOnAccent,
    primaryContainer = TealContainer,
    onPrimaryContainer = ElectricMint,
    secondary = CoralAccent,
    onSecondary = TextPrimary,
    tertiary = SafeGreen,
    background = DarkSlateBg,
    onBackground = TextPrimary,
    surface = SurfaceCardDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevatedDark,
    onSurfaceVariant = TextSecondary,
    outline = BorderDark,
    error = WarningRed,
    onError = TextPrimary
)

@Composable
fun PersonaFitTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = ObsidianColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkSlateBg.toArgb()
            window.navigationBarColor = DarkSlateBg.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
