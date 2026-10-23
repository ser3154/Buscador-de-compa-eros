package servando.contreras.buscadordecompaerosdeactividades.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideBackground
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideDark

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(CoincideBackground),
        contentAlignment = Alignment.Center
    ) {
        Text("Explorar (próximamente)", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
    }
}