package servando.contreras.buscadordecompaerosdeactividades.data

object SampleData {

    val filterCategories = listOf("Todo", "Deporte", "Estudio", "Hobby", "Social", "Comida")

    val activityCategories = listOf("Deporte", "Estudio", "Hobby", "Social", "Comida")

    val plans = listOf(
        Plan(
            id = 1,
            category = "Estudio",
            date = "Sáb. 17 oct. · 16:30",
            title = "Café, apuntes y foco",
            place = "Café La Bicicleta, Malasaña · 0.8 km"
            // imageRes = R.drawable.plan_cafe
        ),
        Plan(
            id = 2,
            category = "Deporte",
            date = "Dom. 18 oct. · 10:00",
            title = "Una vuelta al Retiro, sin prisa",
            place = "Parque del Retiro · 1.4 km"
        ),
        Plan(
            id = 3,
            category = "Social",
            date = "Dom. 18 oct. · 19:00",
            title = "Tarde de juegos de mesa",
            place = "Bar Ludo, Lavapiés · 2.1 km"
        )
    )
}