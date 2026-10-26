package servando.contreras.buscadordecompaerosdeactividades.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF22CCE2), onPrimary = Color(0xFF0B3B4A),
    secondary = Color(0xFF0E6E80), onSecondary = Color.White,
    secondaryContainer = Color(0xFFD6F3F6), onSecondaryContainer = Color(0xFF0E6E80),
    tertiaryContainer = Color(0xFFD9F2E6), onTertiaryContainer = Color(0xFF1F6B4A),
    background = Color(0xFFF2FAFA), onBackground = Color(0xFF0B3B4A),
    surface = Color.White, onSurface = Color(0xFF0B3B4A),
    surfaceVariant = Color(0xFFD6F3F6), onSurfaceVariant = Color(0xFF516A73),
    outline = Color(0xFF6B8A92), outlineVariant = Color(0xFFCFE3E6)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF50D9E9), onPrimary = Color(0xFF003640),
    secondary = Color(0xFF88D5E2), onSecondary = Color(0xFF003640),
    secondaryContainer = Color(0xFF173F48), onSecondaryContainer = Color(0xFFB9EEF5),
    tertiaryContainer = Color(0xFF214D3E), onTertiaryContainer = Color(0xFFB7EED1),
    background = Color(0xFF0D1B20), onBackground = Color(0xFFE0F0F3),
    surface = Color(0xFF16292F), onSurface = Color(0xFFE0F0F3),
    surfaceVariant = Color(0xFF243C44), onSurfaceVariant = Color(0xFFB0C8CF),
    outline = Color(0xFF79959E), outlineVariant = Color(0xFF36525C)
)

@Composable
fun BuscadorDeCompañerosDeActividadesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
