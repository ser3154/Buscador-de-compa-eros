package servando.contreras.buscadordecompaerosdeactividades.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale
import servando.contreras.buscadordecompaerosdeactividades.componenets.*

private data class CalendarPlan(val day: Int, val time: String, val title: String, val place: String, val owner: Boolean = false)
private val calendarPlans = listOf(
    CalendarPlan(17, "16:30", "Café, apuntes y foco", "Café La Bicicleta, Malasaña · 0.8 km"),
    CalendarPlan(17, "09:00", "Kilómetros y buena compañía", "Parque del Retiro · 1.2 km"),
    CalendarPlan(18, "11:00", "Dibuja, charla, repite", "Jardines de Sabatini · 2.1 km"),
    CalendarPlan(24, "10:00", "Una vuelta al Retiro, sin prisa", "Puerta de Alcalá, Parque del Retiro"),
    CalendarPlan(25, "17:00", "Tarde de picnic y música", "Parque del Oeste", true)
)
@Composable
fun CalendarScreen(onBack: () -> Unit, onRequests: () -> Unit) {
    var monthText by rememberSaveable { mutableStateOf("2026-10") }
    var selectedText by rememberSaveable { mutableStateOf("2026-10-17") }
    var dialogTitle by rememberSaveable { mutableStateOf<String?>(null) }
    var dialogBody by rememberSaveable { mutableStateOf("") }
    val month = YearMonth.parse(monthText)
    val selected = LocalDate.parse(selectedText)
    val locale = Locale.forLanguageTag("es-MX")
    val plans = if (month == YearMonth.of(2026, 10)) calendarPlans.filter { it.day == selected.dayOfMonth } else emptyList()

    Scaffold(containerColor = PlanningColors.background,
        topBar = { Column(Modifier.statusBarsPadding()) { PlanningHeader("Mi calendario", onBack) } },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Text("Tus próximos encuentros", color = PlanningColors.ink, fontSize = 25.sp, lineHeight = 36.sp, fontWeight = FontWeight.Bold)
            Text("Organiza tu tiempo y encuentra tu siguiente plan.", color = PlanningColors.muted, fontSize = 13.sp, lineHeight = 19.sp)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                PlanningArrow("‹", "Mes anterior") { val next = month.minusMonths(1); monthText = next.toString(); selectedText = next.atDay(1).toString() }
                Text(month.format(DateTimeFormatter.ofPattern("MMMM yyyy", locale)).replaceFirstChar { it.titlecase(locale) },
                    Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink)
                PlanningArrow("›", "Mes siguiente") { val next = month.plusMonths(1); monthText = next.toString(); selectedText = next.atDay(1).toString() }
            }
            Surface(shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp), color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, PlanningColors.border)) {
                Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row { listOf("L", "M", "M", "J", "V", "S", "D").forEach {
                        Text(it, Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 12.sp, color = PlanningColors.muted)
                    } }
                    val offset = month.atDay(1).dayOfWeek.value - 1
                    val weeks = (offset + month.lengthOfMonth() + 6) / 7
                    repeat(weeks) { week ->
                        Row(Modifier.fillMaxWidth()) {
                            repeat(7) { weekday ->
                                val day = week * 7 + weekday - offset + 1
                                Box(Modifier.weight(1f).height(42.dp), contentAlignment = Alignment.Center) {
                                    if (day in 1..month.lengthOfMonth()) {
                                        val date = month.atDay(day)
                                        val hasPlan = month == YearMonth.of(2026, 10) && calendarPlans.any { it.day == day }
                                        Box(Modifier.size(42.dp).clip(CircleShape)
                                            .background(if (date == selected) PlanningColors.selected else if (hasPlan) PlanningColors.soft else Color.Transparent)
                                            .clickable { selectedText = date.toString() }
                                            .semantics { this.selected = date == selected; contentDescription = date.format(DateTimeFormatter.ofPattern("d 'de' MMMM yyyy", locale)) + if (hasPlan) ", con actividades" else "" },
                                            contentAlignment = Alignment.Center) {
                                            Text("$day${if (hasPlan) "·" else ""}", fontSize = 13.sp, color = PlanningColors.teal)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Text(selected.format(DateTimeFormatter.ofPattern("d 'de' MMMM", locale)), color = PlanningColors.ink, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            if (plans.isEmpty()) PlanningCard {
                Text("No tienes encuentros este día", color = PlanningColors.ink, fontWeight = FontWeight.Bold)
                Text("Selecciona otra fecha o explora nuevos planes.", color = PlanningColors.muted, fontSize = 13.sp)
                PlanningAction("Explorar actividades", onBack)
            }
            plans.forEach { plan ->
                PlanningCard {
                    val time = selected.format(DateTimeFormatter.ofPattern("EEE d MMM", locale)).replaceFirstChar { it.titlecase(locale) } + " · ${plan.time}"
                    Text(time, color = PlanningColors.teal, fontSize = 12.sp)
                    Text(plan.title, color = PlanningColors.ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(plan.place, color = PlanningColors.muted, fontSize = 12.sp)
                    PlanningBadge(if (plan.owner) "Organizas" else "Te uniste")
                    PlanningAction("Ver actividad", {
                        dialogTitle = plan.title; dialogBody = "$time\n${plan.place}\n${if (plan.owner) "Organizas este encuentro." else "Tu participación está confirmada."}"
                    })
                }
            }
            PlanningAction("Solicitudes para participar", onRequests)
            Spacer(Modifier.height(24.dp))
        }
    }
    dialogTitle?.let { title ->
        AlertDialog(onDismissRequest = { dialogTitle = null }, title = { Text(title) }, text = { Text(dialogBody) },
            confirmButton = { TextButton(onClick = { dialogTitle = null }) { Text("Cerrar") } })
    }
}

@Preview(showBackground = true, widthDp = 402, heightDp = 874)
@Composable
private fun CalendarScreenPreview() {
    servando.contreras.buscadordecompaerosdeactividades.ui.theme.BuscadorDeCompañerosDeActividadesTheme {
        CalendarScreen({}, {})
    }
}
