import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import servando.contreras.buscadordecompaerosdeactividades.componenets.BackButton
import servando.contreras.buscadordecompaerosdeactividades.data.SampleData
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideBackground
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.CoincideDark

@Composable
fun ActivityDetailScreen(activityId: Int, onBack: () -> Unit) {
    val activity = SampleData.activities.firstOrNull { it.id == activityId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CoincideBackground)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        BackButton(onClick = onBack)
        Spacer(Modifier.height(16.dp))
        Text(
            activity?.title ?: "Actividad no encontrada",
            fontSize = 26.sp, fontWeight = FontWeight.Bold, color = CoincideDark
        )
    }
}