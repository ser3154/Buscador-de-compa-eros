package servando.contreras.buscadordecompaerosdeactividades.data

import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

/** Actividad a la que la persona ya asistió (organizada por ella o a la que se unió). */
data class PastActivity(
    val date: LocalDate,
    val title: String,
    val category: String,
    val organized: Boolean,
    val people: List<String>
)

data class MonthStat(val month: YearMonth, val joined: Int, val organized: Int) {
    val total get() = joined + organized
}

data class CategoryStat(val category: String, val count: Int, val percent: Int)

data class PeriodStats(
    val total: Int,
    val organized: Int,
    val joined: Int,
    val previousTotal: Int,
    val categories: List<CategoryStat>,
    val months: List<MonthStat>,
    val newPeople: List<String>,
    val previousNewPeople: Int,
    val topCompanion: Pair<String, Int>?
) {
    fun percentOf(count: Int) = if (total == 0) 0 else (count * 100f / total).roundToInt()
}

/**
 * Historial de demostración. La fecha de referencia es el 15 de octubre de 2026,
 * así que «3 meses» (16 jul – 15 oct) da los mismos números que el diseño.
 */
object ActivityHistory {
    val today: LocalDate = LocalDate.of(2026, 10, 15)

    val categories = listOf("Deporte", "Estudio", "Hobby", "Social")

    private fun a(date: String, title: String, category: String, organized: Boolean, vararg people: String) =
        PastActivity(LocalDate.parse(date), title, category, organized, people.toList())

    val items: List<PastActivity> = listOf(
        // Antes del 15 de abril
        a("2026-01-11", "Carrera de año nuevo", "Deporte", false, "Marcos Ruiz"),
        a("2026-01-24", "Brunch de los sábados", "Social", false, "Elena Torres"),
        a("2026-02-07", "Club de lectura en la biblioteca", "Estudio", false, "Elena Torres"),
        a("2026-02-21", "Pádel para principiantes", "Deporte", true, "Marcos Ruiz"),
        a("2026-03-08", "Taller de cerámica", "Hobby", false, "Elena Torres"),
        a("2026-03-22", "Ruta por la Casa de Campo", "Deporte", false, "Marcos Ruiz"),
        a("2026-04-04", "Noche de juegos de mesa", "Social", true, "Marcos Ruiz", "Elena Torres"),
        a("2026-04-10", "Intercambio de idiomas", "Estudio", false, "Elena Torres"),
        // 15 abr – 15 jul (periodo anterior a «3 meses»)
        a("2026-04-18", "Yoga en el parque", "Deporte", false, "Nora Vidal"),
        a("2026-04-26", "Estudio de oposiciones", "Estudio", false, "Marcos Ruiz"),
        a("2026-05-03", "Fotografía urbana", "Hobby", false, "Hugo Díaz"),
        a("2026-05-10", "Bici por Madrid Río", "Deporte", false, "Marcos Ruiz"),
        a("2026-05-16", "Cena de tapas", "Social", true, "Elena Torres", "Nora Vidal"),
        a("2026-05-24", "Repaso de inglés", "Estudio", false, "Raúl Peña"),
        a("2026-05-31", "Partido de vóley", "Deporte", false, "Hugo Díaz"),
        a("2026-06-07", "Taller de acuarela", "Hobby", false, "Teresa Soto"),
        a("2026-06-14", "Carrera popular", "Deporte", true, "Marcos Ruiz"),
        a("2026-06-21", "Fiesta de San Juan", "Social", false, "Elena Torres"),
        a("2026-06-28", "Biblioteca y café", "Estudio", false, "Raúl Peña"),
        a("2026-07-04", "Natación al aire libre", "Deporte", false, "Nora Vidal"),
        a("2026-07-08", "Cine de verano", "Hobby", true, "Teresa Soto", "Hugo Díaz"),
        a("2026-07-12", "Terraceo con amigos", "Social", false, "Marcos Ruiz"),
        // 16 jul – 15 oct («3 meses»)
        a("2026-07-19", "Kilómetros al amanecer", "Deporte", false, "Andrea López"),
        a("2026-07-26", "Apuntes compartidos", "Estudio", false, "Diego Ruiz"),
        a("2026-08-02", "Senderismo en La Pedriza", "Deporte", false, "Andrea López", "Carla Núñez"),
        a("2026-08-09", "Tarde de ganchillo", "Hobby", true, "Sofía García"),
        a("2026-08-16", "Maratón de estudio", "Estudio", false, "Diego Ruiz"),
        a("2026-08-23", "Pádel en Chamberí", "Deporte", false, "Marcos Ruiz"),
        a("2026-09-03", "Una vuelta al Retiro", "Deporte", false, "Andrea López"),
        a("2026-09-06", "Club de lectura", "Estudio", false, "Pablo Torres"),
        a("2026-09-10", "Dibuja, charla, repite", "Hobby", true, "Carla Núñez", "Sofía García"),
        a("2026-09-13", "Brunch en Lavapiés", "Social", false, "Elena Torres"),
        a("2026-09-17", "Running por Madrid Río", "Deporte", false, "Marcos Ruiz"),
        a("2026-09-20", "Estudio en silencio", "Estudio", true, "Pablo Torres", "Laura Gil"),
        a("2026-09-27", "Taller de cerámica", "Hobby", false, "Andrea López"),
        a("2026-10-02", "Kilómetros y buena compañía", "Deporte", false, "Marcos Ruiz"),
        a("2026-10-04", "Noche de juegos de mesa", "Social", true, "Elena Torres", "Iván Mora"),
        a("2026-10-07", "Intercambio de idiomas", "Estudio", false, "Laura Gil"),
        a("2026-10-10", "Fotografía al atardecer", "Hobby", true, "Diego Ruiz"),
        a("2026-10-13", "Bici por la Casa de Campo", "Deporte", false, "Sofía García")
    )

    /** Primera vez que coincidiste con cada persona. */
    private val firstMet: Map<String, LocalDate> = items.sortedBy { it.date }
        .flatMap { activity -> activity.people.map { it to activity.date } }
        .groupBy({ it.first }, { it.second })
        .mapValues { (_, dates) -> dates.first() }

    fun between(start: LocalDate, end: LocalDate) = items.filter { it.date in start..end }

    fun statsFor(start: LocalDate, end: LocalDate): PeriodStats {
        val inRange = between(start, end)
        val days = ChronoUnit.DAYS.between(start, end) + 1
        val previousEnd = start.minusDays(1)
        val previousStart = previousEnd.minusDays(days - 1)

        val total = inRange.size
        val organized = inRange.count { it.organized }
        val categories = categories.map { category ->
            val count = inRange.count { it.category == category }
            CategoryStat(category, count, if (total == 0) 0 else (count * 100f / total).roundToInt())
        }

        val months = generateSequence(YearMonth.from(start)) { it.plusMonths(1) }
            .takeWhile { it <= YearMonth.from(end) }
            .map { month ->
                val ofMonth = inRange.filter { YearMonth.from(it.date) == month }
                MonthStat(month, ofMonth.count { !it.organized }, ofMonth.count { it.organized })
            }.toList().takeLast(12)

        val newPeople = firstMet.filterValues { it in start..end }.entries
            .sortedBy { it.value }.map { it.key }
        val previousNewPeople = firstMet.values.count { it in previousStart..previousEnd }

        val topCompanion = inRange.flatMap { it.people }
            .groupingBy { it }.eachCount()
            .maxWithOrNull(compareBy<Map.Entry<String, Int>> { it.value }
                .thenByDescending { firstMet[it.key] })
            ?.toPair()

        return PeriodStats(
            total = total,
            organized = organized,
            joined = total - organized,
            previousTotal = between(previousStart, previousEnd).size,
            categories = categories,
            months = months,
            newPeople = newPeople,
            previousNewPeople = previousNewPeople,
            topCompanion = topCompanion
        )
    }
}

/** Iniciales de un nombre: «Andrea López» → «AL». */
fun initialsOf(name: String) = name.split(" ").filter { it.isNotBlank() }.take(2)
    .joinToString("") { it.first().uppercase() }
