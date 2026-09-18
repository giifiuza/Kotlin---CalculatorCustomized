package br.com.unisal.fiuza.calculadoracustomized.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyanPrimaryDark,
    onPrimary = NeonCyanOnPrimaryDark,
    primaryContainer = NeonCyanPrimaryContainerDark,
    onPrimaryContainer = NeonCyanOnPrimaryContainerDark,
    secondary = NeonVioletSecondaryDark,
    onSecondary = NeonVioletOnSecondaryDark,
    secondaryContainer = NeonVioletSecondaryContainerDark,
    onSecondaryContainer = NeonVioletOnSecondaryContainerDark,
    tertiary = NeonPinkTertiaryDark,
    onTertiary = NeonPinkOnTertiaryDark,
    error = NeonAlertErrorDark,
    onError = NeonAlertOnErrorDark,
    errorContainer = NeonAlertErrorContainerDark,
    onErrorContainer = NeonAlertOnErrorContainerDark,
    background = NeonBackgroundDark,
    onBackground = NeonOnBackgroundDark,
    surface = NeonSurfaceDark,
    onSurface = NeonOnSurfaceDark,
    surfaceVariant = NeonSurfaceVariantDark,
    onSurfaceVariant = NeonOnSurfaceVariantDark,
    outline = NeonOutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = NeonCyanPrimaryLight,
    onPrimary = NeonCyanOnPrimaryLight,
    primaryContainer = NeonCyanPrimaryContainerLight,
    onPrimaryContainer = NeonCyanOnPrimaryContainerLight,
    secondary = NeonVioletSecondaryLight,
    onSecondary = NeonVioletOnSecondaryLight,
    secondaryContainer = NeonVioletSecondaryContainerLight,
    onSecondaryContainer = NeonVioletOnSecondaryContainerLight,
    tertiary = NeonPinkTertiaryLight,
    onTertiary = NeonPinkOnTertiaryLight,
    error = NeonAlertErrorLight,
    onError = NeonAlertOnErrorLight,
    errorContainer = NeonAlertErrorContainerLight,
    onErrorContainer = NeonAlertOnErrorContainerLight,
    background = NeonBackgroundLight,
    onBackground = NeonOnBackgroundLight,
    surface = NeonSurfaceLight,
    onSurface = NeonOnSurfaceLight,
    surfaceVariant = NeonSurfaceVariantLight,
    onSurfaceVariant = NeonOnSurfaceVariantLight,
    outline = NeonOutlineLight
)

// Formas do tema: cantos bem arredondados para os botões "padrão"
// (números, operadores, funções científicas) e uma forma em "pílula"
// (extraLarge) reservada para dar destaque especial ao botão de igualdade.
val NeonShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(percent = 50)
)

@Composable
fun CalculadorafiuzaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
        shapes = NeonShapes,
        content = content
    )
}