package servando.contreras.buscadordecompaerosdeactividades.componenets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.*

/** Shared home navigation: same icons, labels, indicator and dimensions on every main screen. */
@Composable
fun CoincideNavigationBar(selectedTab: Int, onSelect: (Int) -> Unit) {
    val tabs = listOf(
        "Explorar" to Icons.Default.Place,
        "Mis planes" to Icons.Default.DateRange,
        "Mensajes" to Icons.Default.Email,
        "Perfil" to Icons.Default.Person
    )
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        tabs.forEachIndexed { index, (label, icon) ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onSelect(index) },
                icon = { Icon(icon, contentDescription = null) },
                label = { Text(label, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CoincideTeal,
                    selectedTextColor = CoincideTeal,
                    indicatorColor = CoincideSoft,
                    unselectedIconColor = CoincideHint,
                    unselectedTextColor = CoincideHint
                )
            )
        }
    }
}
