package servando.contreras.buscadordecompaerosdeactividades

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import servando.contreras.buscadordecompaerosdeactividades.navigation.AppNavigation
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.BuscadorDeCompañerosDeActividadesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // El tema sigue al sistema hasta que la persona lo cambia en Configuración.
            val systemDark = isSystemInDarkTheme()
            var darkOverride by rememberSaveable { mutableStateOf<Boolean?>(null) }
            val darkTheme = darkOverride ?: systemDark

            // Iconos de la barra de estado legibles en el tema elegido.
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(LightScrim, DarkScrim) { darkTheme }
                )
                onDispose {}
            }

            BuscadorDeCompañerosDeActividadesTheme(darkTheme = darkTheme) {
                AppNavigation(darkTheme = darkTheme, onDarkThemeChange = { darkOverride = it })
            }
        }
    }
}

private val LightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val DarkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
