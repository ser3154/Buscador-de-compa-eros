package servando.contreras.buscadordecompaerosdeactividades.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import servando.contreras.buscadordecompaerosdeactividades.componenets.CategoryChip
import servando.contreras.buscadordecompaerosdeactividades.data.Plan
import servando.contreras.buscadordecompaerosdeactividades.data.SampleData
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExploreScreen(onNewActivity: () -> Unit){
    var search by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todo") }

    val categories = SampleData.filterCategories
    val plans = SampleData.plans
    Scaffold(
        containerColor = CoincideBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewActivity,
                containerColor = CoincidePrimary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva actividad")
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Explorar", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
                Text("Coincide.", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CoincideTeal)
            }
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Hola, Lucía", fontSize = 11.sp, color = CoincideHint)
                    Text("Madrid, España", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
                }
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(CoincideSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Text("LM", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CoincideTeal)
                }
            }

            Spacer(Modifier.height(14.dp))
            Text("Hay un plan contigo.", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Busca una actividad por nombre", fontSize = 12.sp, color = CoincideHint) },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = CoincidePrimary,
                    unfocusedBorderColor = CoincideBorder,
                    focusedTextColor = CoincideDark,
                    unfocusedTextColor = CoincideDark
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(14.dp))
            Text("¿Qué te apetece?", fontSize = 11.sp, color = CoincideHint)
            Spacer(Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    CategoryChip(
                        text = cat,
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat }
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CategoryChip("A 5 km · GPS")
                CategoryChip("17–18 oct.")
            }

            Spacer(Modifier.height(20.dp))
            Text("Cerca de ti", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
            Spacer(Modifier.height(2.dp))
            Text("${plans.size} planes · por cercanía", fontSize = 11.sp, color = CoincideHint)
            Spacer(Modifier.height(12.dp))

            plans.forEach { plan ->
                PlanCard(plan)
                Spacer(Modifier.height(14.dp))
            }

            Spacer(Modifier.height(60.dp))
        }
    }
}

@Composable
private fun PlanCard(plan: Plan) {
    val shape = RoundedCornerShape(14.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, CoincideBorder, shape)
    ) {
        if (plan.imageRes != null) {
            Image(
                painter = painterResource(plan.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Brush.linearGradient(listOf(Color(0xFF8D6E63), Color(0xFFD7B98E))))
            )
        }

        Column(modifier = Modifier.padding(12.dp)) {
            CategoryChip(plan.category)
            Spacer(Modifier.height(10.dp))
            Text(plan.date, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = CoincideTeal)
            Spacer(Modifier.height(4.dp))
            Text(plan.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CoincideDark)
            Spacer(Modifier.height(2.dp))
            Text(plan.place, fontSize = 11.sp, color = CoincideHint)
        }
    }
}
