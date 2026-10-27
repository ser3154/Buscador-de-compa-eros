package servando.contreras.buscadordecompaerosdeactividades.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import servando.contreras.buscadordecompaerosdeactividades.R
import servando.contreras.buscadordecompaerosdeactividades.componenets.PlanningColors
import servando.contreras.buscadordecompaerosdeactividades.data.ActivityHistory
import servando.contreras.buscadordecompaerosdeactividades.data.MonthStat
import servando.contreras.buscadordecompaerosdeactividades.data.PeriodStats
import servando.contreras.buscadordecompaerosdeactividades.data.initialsOf
import servando.contreras.buscadordecompaerosdeactividades.ui.theme.BuscadorDeCompañerosDeActividadesTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.math.min

private enum class StatsPeriod(val label: String) {
    WEEK("7 días"), MONTH("30 días"), QUARTER("3 meses"), YEAR("Este año"), CUSTOM("Personalizado")
}

private val monthShort = listOf("ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic")
private val monthLong = listOf(
    "enero", "febrero", "marzo", "abril", "mayo", "junio",
    "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
)

private fun LocalDate.short() = "$dayOfMonth ${monthShort[monthValue - 1]} $year"
private fun LocalDate.toUtcMillis() = atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
private fun Long.toLocalDate() = Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
private fun plans(n: Int) = if (n == 1) "1 plan" else "$n planes"

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StatisticsScreen(onBack: () -> Unit) {
    val today = ActivityHistory.today
    var period by rememberSaveable { mutableStateOf(StatsPeriod.QUARTER) }
    var customStart by rememberSaveable { mutableStateOf(today.minusDays(13).toString()) }
    var customEnd by rememberSaveable { mutableStateOf(today.toString()) }
    var showPicker by rememberSaveable { mutableStateOf(false) }

    val (start, end) = when (period) {
        StatsPeriod.WEEK -> today.minusDays(6) to today
        StatsPeriod.MONTH -> today.minusDays(29) to today
        StatsPeriod.QUARTER -> today.minusMonths(3).plusDays(1) to today
        StatsPeriod.YEAR -> today.withDayOfYear(1) to today
        StatsPeriod.CUSTOM -> LocalDate.parse(customStart) to LocalDate.parse(customEnd)
    }
    val stats = remember(start, end) { ActivityHistory.statsFor(start, end) }

    Column(
        Modifier
            .fillMaxSize()
            .background(PlanningColors.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        // Cabecera
        Row(
            Modifier.fillMaxWidth().padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                onClick = onBack,
                modifier = Modifier.size(40.dp).semantics { contentDescription = "Volver" },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, PlanningColors.border)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(painterResource(R.drawable.stats_arrow_left), null, Modifier.size(20.dp), tint = PlanningColors.teal)
                }
            }
            Text("coincide.", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = PlanningColors.teal)
        }
        Spacer(Modifier.height(22.dp))
        Text(
            "Mis estadísticas", fontSize = 30.sp, lineHeight = 34.sp, letterSpacing = (-1).sp,
            fontWeight = FontWeight.ExtraBold, color = PlanningColors.ink
        )
        Spacer(Modifier.height(8.dp))
        Text("Así se ve tu vida social en coincide.", fontSize = 14.sp, color = PlanningColors.muted)
        Spacer(Modifier.height(24.dp))

        // Filtros por fecha
        Text("¿Qué periodo quieres ver?", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = PlanningColors.muted)
        Spacer(Modifier.height(12.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            StatsPeriod.entries.forEach { option ->
                PeriodChip(option.label, selected = option == period) {
                    if (option == StatsPeriod.CUSTOM) showPicker = true else period = option
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Surface(
            onClick = { showPicker = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, PlanningColors.border)
        ) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(painterResource(R.drawable.stats_calendar_days), null, Modifier.size(18.dp), tint = PlanningColors.primary)
                Text(
                    "${start.short()} – ${end.short()}", Modifier.weight(1f),
                    fontSize = 14.sp, fontWeight = FontWeight.Medium, color = PlanningColors.ink
                )
                Icon(painterResource(R.drawable.stats_chevron_down), "Elegir fechas", Modifier.size(18.dp), tint = MaterialTheme.colorScheme.outline)
            }
        }
        Spacer(Modifier.height(24.dp))

        TotalCard(stats)
        Spacer(Modifier.height(24.dp))
        CategoryCard(stats)
        Spacer(Modifier.height(24.dp))
        MonthlyCard(stats.months)
        Spacer(Modifier.height(24.dp))
        ConnectionsCard(stats)
        Spacer(Modifier.height(28.dp))
        Spacer(Modifier.navigationBarsPadding())
    }

    if (showPicker) {
        PeriodPickerDialog(
            start = start, end = end, today = today,
            onDismiss = { showPicker = false },
            onConfirm = { from, to ->
                customStart = from.toString(); customEnd = to.toString()
                period = StatsPeriod.CUSTOM
                showPicker = false
            }
        )
    }
}

@Composable
private fun PeriodChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, if (selected) PlanningColors.primary else PlanningColors.border, CircleShape)
            .clickable(role = Role.RadioButton, onClick = onClick)
            .semantics { this.selected = selected }
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(
            label, fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (selected) PlanningColors.teal else PlanningColors.muted
        )
    }
}

/** Etiqueta «+4 vs. periodo anterior». Si baja, la flecha apunta hacia abajo. */
@Composable
private fun TrendBadge(difference: Int, suffix: String = "") {
    val sign = when {
        difference > 0 -> "+$difference"
        difference < 0 -> "−${-difference}"
        else -> "0"
    }
    Row(
        Modifier.clip(CircleShape).background(MaterialTheme.colorScheme.surface).padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            painterResource(R.drawable.stats_trending_up), null,
            Modifier.size(13.dp).graphicsLayer { if (difference < 0) scaleY = -1f },
            tint = PlanningColors.teal
        )
        Text("$sign$suffix", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink)
    }
}

@Composable
private fun Dot(color: Color, size: Int) {
    Box(Modifier.size(size.dp).clip(CircleShape).background(color))
}

@Composable
private fun StatsCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, PlanningColors.border, RoundedCornerShape(22.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = content
    )
}

@Composable
private fun CardTitle(text: String) {
    Text(text, fontSize = 18.sp, lineHeight = 22.sp, letterSpacing = (-0.4).sp, fontWeight = FontWeight.ExtraBold, color = PlanningColors.ink)
}

@Composable
private fun TotalCard(stats: PeriodStats) {
    val onHero = MaterialTheme.colorScheme.onPrimary
    val strong = onHero.copy(alpha = 0.78f)
    val muted = onHero.copy(alpha = 0.68f)
    val faint = onHero.copy(alpha = 0.58f)
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(PlanningColors.primary)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Actividades realizadas", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = muted)
            TrendBadge(stats.total - stats.previousTotal, " vs. periodo anterior")
        }
        Text(
            "${stats.total}", fontSize = 52.sp, lineHeight = 52.sp, letterSpacing = (-2).sp,
            fontWeight = FontWeight.ExtraBold, color = strong
        )
        Box(
            Modifier.fillMaxWidth().height(10.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.22f))
                .semantics { contentDescription = "${stats.percentOf(stats.organized)} % organizadas por ti" }
        ) {
            val fraction = if (stats.total == 0) 0f else stats.organized / stats.total.toFloat()
            Box(Modifier.fillMaxWidth(fraction).fillMaxHeight().background(Color.White))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf(
                Triple("Organizadas", stats.organized, Color.White),
                Triple("Me uní", stats.joined, Color.White.copy(alpha = 0.6f))
            ).forEach { (label, count, dot) ->
                Column(
                    Modifier.weight(1f).clip(RoundedCornerShape(14.dp)).background(Color.White.copy(alpha = 0.12f)).padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Dot(dot, 8)
                        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = muted)
                    }
                    Text("$count", fontSize = 26.sp, lineHeight = 30.sp, letterSpacing = (-0.5).sp, fontWeight = FontWeight.ExtraBold, color = strong)
                    Text("${stats.percentOf(count)} % del total", fontSize = 11.sp, color = faint)
                }
            }
        }
    }
}

@Composable
private fun categoryColor(category: String): Color = when (category) {
    "Hobby" -> MaterialTheme.colorScheme.tertiaryContainer
    "Social" -> PlanningColors.muted
    else -> PlanningColors.primary
}

@Composable
private fun CategoryCard(stats: PeriodStats) {
    val max = stats.categories.maxOf { it.count }.coerceAtLeast(1)
    StatsCard {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            CardTitle("Por categoría")
            Text("${stats.total} en total", fontSize = 12.sp, color = PlanningColors.muted)
        }
        stats.categories.forEach { item ->
            val color = categoryColor(item.category)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Dot(color, 10)
                        Text(item.category, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = PlanningColors.ink)
                    }
                    Text("${item.count} · ${item.percent} %", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = PlanningColors.muted)
                }
                Box(Modifier.fillMaxWidth().height(10.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface)) {
                    Box(Modifier.fillMaxWidth(item.count / max.toFloat()).fillMaxHeight().clip(CircleShape).background(color))
                }
            }
        }
    }
}

@Composable
private fun MonthlyCard(months: List<MonthStat>) {
    val joinedColor = PlanningColors.primary
    val organizedColor = MaterialTheme.colorScheme.tertiaryContainer
    val maxMonth = months.maxOfOrNull { it.total }?.coerceAtLeast(1) ?: 1
    val unit = min(16f, 112f / maxMonth) // 16 dp por plan, como en el diseño, sin salirse de la gráfica
    val best = months.maxByOrNull { it.total }

    StatsCard {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            CardTitle("Por mes")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf("Me uní" to joinedColor, "Organizadas" to organizedColor).forEach { (label, color) ->
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Dot(color, 8)
                        Text(label, fontSize = 11.sp, color = PlanningColors.muted)
                    }
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().height(176.dp),
            horizontalArrangement = Arrangement.spacedBy(if (months.size > 6) 4.dp else 12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            months.forEach { month ->
                Column(
                    Modifier.weight(1f).semantics(mergeDescendants = true) {
                        contentDescription = "${monthLong[month.month.monthValue - 1]}: ${month.joined} te uniste, ${month.organized} organizadas"
                    },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("${month.total}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PlanningColors.ink)
                    Column(Modifier.widthIn(max = 40.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        val both = month.joined > 0 && month.organized > 0
                        if (month.joined > 0) {
                            val shape = if (both) RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp) else RoundedCornerShape(8.dp)
                            Box(Modifier.fillMaxWidth().height((month.joined * unit).dp).clip(shape).background(joinedColor))
                        }
                        if (month.organized > 0) {
                            val shape = if (both) RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp) else RoundedCornerShape(8.dp)
                            Box(Modifier.fillMaxWidth().height((month.organized * unit).dp).clip(shape).background(organizedColor))
                        }
                        if (month.total == 0) {
                            Box(Modifier.fillMaxWidth().height(2.dp).clip(CircleShape).background(PlanningColors.border))
                        }
                    }
                    Text(
                        monthShort[month.month.monthValue - 1].replaceFirstChar { it.uppercase() },
                        fontSize = 12.sp, color = PlanningColors.muted, maxLines = 1
                    )
                }
            }
        }
        HorizontalDivider(color = PlanningColors.border)
        Text(
            if (best == null || best.total == 0) "Aún no hay actividades en este periodo."
            else "${monthLong[best.month.monthValue - 1].replaceFirstChar { it.uppercase() }} fue tu mes más activo, con ${plans(best.total)}.",
            fontSize = 12.sp, color = PlanningColors.muted
        )
    }
}

@Composable
private fun ConnectionsCard(stats: PeriodStats) {
    val people = stats.newPeople
    val shown = people.take(5)
    val rest = people.size - shown.size
    StatsCard {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            CardTitle("Nuevas conexiones")
            TrendBadge(people.size - stats.previousNewPeople)
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                "${people.size}", fontSize = 44.sp, lineHeight = 44.sp, letterSpacing = (-1.5).sp,
                fontWeight = FontWeight.ExtraBold, color = PlanningColors.ink
            )
            Text(
                if (people.size == 1) "persona nueva en tu círculo durante este periodo"
                else "personas nuevas en tu círculo durante este periodo",
                Modifier.weight(1f), fontSize = 13.sp, lineHeight = 18.sp, color = PlanningColors.muted
            )
        }
        if (people.isNotEmpty()) {
            val count = shown.size + if (rest > 0) 1 else 0
            Box(
                Modifier
                    .size(width = (26 * (count - 1) + 36).dp, height = 36.dp)
                    .semantics(mergeDescendants = true) { contentDescription = people.joinToString() }
            ) {
                (shown.map { initialsOf(it) to PlanningColors.ink } + if (rest > 0) listOf("+$rest" to PlanningColors.muted) else emptyList())
                    .forEachIndexed { index, (label, color) ->
                        Box(
                            Modifier
                                .offset(x = (26 * index).dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                                .border(2.dp, PlanningColors.border, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
                        }
                    }
            }
        }
        stats.topCompanion?.let { (name, together) ->
            HorizontalDivider(color = PlanningColors.border)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.size(40.dp).clip(CircleShape).background(PlanningColors.soft), contentAlignment = Alignment.Center) {
                    Text(initialsOf(name), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PlanningColors.teal)
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("Con quien más coincides", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = PlanningColors.muted)
                    Text("$name · ${plans(together)} juntos", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = PlanningColors.ink)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PeriodPickerDialog(
    start: LocalDate,
    end: LocalDate,
    today: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate, LocalDate) -> Unit
) {
    val lastDay = today.toUtcMillis()
    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = start.toUtcMillis(),
        initialSelectedEndDateMillis = end.toUtcMillis(),
        yearRange = 2025..today.year,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long) = utcTimeMillis <= lastDay
        }
    )
    val colors = DatePickerDefaults.colors(
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = PlanningColors.muted,
        headlineContentColor = PlanningColors.ink,
        selectedDayContainerColor = PlanningColors.primary,
        selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
        dayInSelectionRangeContainerColor = PlanningColors.soft,
        dayInSelectionRangeContentColor = PlanningColors.teal,
        todayDateBorderColor = PlanningColors.primary,
        todayContentColor = PlanningColors.teal
    )
    val buttonColors = ButtonDefaults.textButtonColors(contentColor = PlanningColors.teal)
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            val from = state.selectedStartDateMillis
            val to = state.selectedEndDateMillis
            TextButton(
                enabled = from != null && to != null, colors = buttonColors,
                onClick = { onConfirm(from!!.toLocalDate(), to!!.toLocalDate()) }
            ) { Text("Aplicar", fontWeight = FontWeight.SemiBold) }
        },
        dismissButton = { TextButton(onClick = onDismiss, colors = buttonColors) { Text("Cancelar") } },
        colors = colors
    ) {
        DateRangePicker(
            state = state,
            modifier = Modifier.weight(1f),
            colors = colors,
            title = { Text("Elige un periodo", Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp)) }
        )
    }
}

@Preview(showBackground = true, widthDp = 402, heightDp = 1600)
@Composable
private fun StatisticsScreenPreview() {
    BuscadorDeCompañerosDeActividadesTheme(darkTheme = false) {
        StatisticsScreen(onBack = {})
    }
}
