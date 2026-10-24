package servando.contreras.buscadordecompaerosdeactividades.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import servando.contreras.buscadordecompaerosdeactividades.componenets.*
import servando.contreras.buscadordecompaerosdeactividades.data.SampleData
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewActivityScreen(
    onBack: () -> Unit,
    onPublish: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dateTime by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }

    val categories = SampleData.activityCategories

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CoincideBackground)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onClick = onBack)
            Spacer(Modifier.width(12.dp))
            Text(
                "Nueva actividad",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CoincideDark,
                modifier = Modifier.weight(1f)
            )
            Text("coincide.", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CoincideTeal)
        }

        Spacer(Modifier.height(20.dp))
        Text(
            "Un buen plan empieza\ncontigo.",
            fontSize = 26.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Bold,
            color = CoincideDark
        )
        Spacer(Modifier.height(8.dp))
        Text("Comparte tu idea y encuentra a tu gente.", fontSize = 12.sp, color = CoincideHint)
        Spacer(Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD9F2E6), RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Text(
                "No hace falta un gran plan. Solo ganas de compartirlo.",
                fontSize = 12.sp,
                color = Color(0xFF1F6B4A)
            )
        }

        Spacer(Modifier.height(20.dp))
        Text("El plan", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
        Spacer(Modifier.height(12.dp))

        CoincideTextField(
            label = "Nombre de la actividad",
            value = name,
            onValueChange = { name = it },
            placeholder = "Una vuelta al Retiro, sin prisa"
        )

        Spacer(Modifier.height(14.dp))
        Text("Categoría", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = CoincideDark)
        Spacer(Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                CategoryChip(
                    text = cat,
                    selected = category == cat,
                    onClick = { category = cat }
                )
            }
        }

        Spacer(Modifier.height(14.dp))
        CoincideTextArea(
            label = "Descripción de la actividad",
            value = description,
            onValueChange = { description = it },
            placeholder = "Caminamos por el Retiro a ritmo tranquilo y terminamos con un café...",
            helper = "Cuenta qué harás y qué necesitan saber las personas para participar."
        )

        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(CoincideBorder)
        )
        Spacer(Modifier.height(20.dp))

        Text("Cuándo nos vemos", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
        Spacer(Modifier.height(12.dp))

        CoincideTextField(
            label = "Fecha y hora",
            value = dateTime,
            onValueChange = { dateTime = it },
            placeholder = "24 / 10 / 2026 · 10:00"
        )
        Spacer(Modifier.height(12.dp))
        CoincideTextField(
            label = "Lugar",
            value = place,
            onValueChange = { place = it },
            placeholder = "Parque del Retiro, entrada Alfonso XII"
        )

        Spacer(Modifier.height(24.dp))
        PrimaryButton("Publicar actividad", onClick = onPublish)
        Spacer(Modifier.height(16.dp))
    }
}