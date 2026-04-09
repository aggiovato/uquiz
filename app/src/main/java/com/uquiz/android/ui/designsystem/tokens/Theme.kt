package com.uquiz.android.ui.designsystem.tokens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.uquiz.android.domain.user.enums.AppThemeMode

private val ULightColorScheme = lightColorScheme(
    primary = Navy500,
    onPrimary = Color.White,
    primaryContainer = Navy100,
    onPrimaryContainer = Navy900,
    secondary = Teal500,
    onSecondary = Color.White,
    secondaryContainer = Teal100,
    onSecondaryContainer = Teal900,
    tertiary = Gold500,
    onTertiary = Ink950,
    tertiaryContainer = Gold100,
    onTertiaryContainer = Gold900,
    error = Red500,
    onError = Color.White,
    errorContainer = Red100,
    onErrorContainer = Red900,
    background = Neutral50,
    onBackground = Ink950,
    surface = Color.White,
    onSurface = Ink950,
    surfaceVariant = Neutral100,
    onSurfaceVariant = Neutral700,
    outline = Neutral200,
    outlineVariant = Neutral100,
)

private val UDarkColorScheme = darkColorScheme(
    primary = Navy300,
    onPrimary = Navy900,
    primaryContainer = Navy700,
    onPrimaryContainer = Navy100,
    secondary = Teal400,
    onSecondary = Teal900,
    secondaryContainer = Teal900,
    onSecondaryContainer = Teal100,
    tertiary = Gold400,
    onTertiary = Gold900,
    tertiaryContainer = Gold900,
    onTertiaryContainer = Gold100,
    error = Red400,
    onError = Red900,
    errorContainer = Red900,
    onErrorContainer = Red100,
    background = Neutral950,
    onBackground = Neutral50,
    surface = Color(0xFF1A2536),
    onSurface = Neutral50,
    surfaceVariant = Neutral900,
    onSurfaceVariant = Neutral300,
    outline = Neutral700,
    outlineVariant = Neutral900,
)

/**
 * Aplica el tema Material de uQuiz adaptando el esquema de color según [themeMode].
 *
 * Usar en la raíz de la app para que todas las pantallas y componentes del design
 * system compartan los mismos tokens y escala tipográfica.
 *
 * @param themeMode Modo de tema activo. Por defecto [AppThemeMode.LIGHT].
 * @param content Contenido UI renderizado dentro del tema.
 */
@Composable
fun UTheme(
    themeMode: AppThemeMode = AppThemeMode.LIGHT,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    MaterialTheme(
        colorScheme = if (darkTheme) UDarkColorScheme else ULightColorScheme,
        typography = UTypography,
        content = content
    )
}
