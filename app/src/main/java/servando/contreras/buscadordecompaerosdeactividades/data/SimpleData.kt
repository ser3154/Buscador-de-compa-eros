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

    val activities = listOf(
        ActivityItem(
            id = 1, title = "Una vuelta al Retiro, sin prisa", category = "Deporte",
            description = "Caminamos por el Retiro a ritmo tranquilo y terminamos con un café. ¡Todos los niveles son bienvenidos!",
            date = "Sáb. 24 oct. 2026", time = "10:00",
            place = "Puerta de Alcalá, Parque del Retiro", distance = "1.4 km",
            organizerName = "Andrea López", maxParticipants = 8,
            confirmed = listOf("Andrea López", "Marcos Ruiz", "Sofía García"),
            participation = ParticipationState.VISITOR
        ),
        ActivityItem(
            id = 2, title = "Café, apuntes y foco", category = "Estudio",
            description = "Sesión de estudio tranquila con café.",
            date = "Sáb. 17 oct. 2026", time = "16:30",
            place = "Café La Bicicleta, Malasaña", distance = "0.8 km",
            organizerName = "Andrea López", maxParticipants = 6,
            requiresApproval = true, participation = ParticipationState.PENDING
        ),
        ActivityItem(
            id = 3, title = "Kilómetros y buena compañía", category = "Deporte",
            description = "Salida de running a ritmo suave.",
            date = "Dom. 18 oct. 2026", time = "09:00",
            place = "Parque Juan Carlos I", distance = "3.2 km",
            organizerName = "Marcos Ruiz", maxParticipants = 10,
            confirmed = listOf("Marcos Ruiz", "Lucía Martín", "Elena Torres"),
            participation = ParticipationState.PARTICIPANT
        ),
        ActivityItem(
            id = 4, title = "Dibuja, charla, repite", category = "Hobby",
            description = "Tarde de dibujo libre y conversación.",
            date = "Dom. 25 oct. 2026", time = "17:00",
            place = "Centro Cultural, Lavapiés", distance = "2.1 km",
            organizerName = "Lucía Martín", maxParticipants = 12,
            confirmed = listOf("Lucía Martín", "Diego Pérez"),
            participation = ParticipationState.CREATOR
        )
    )
}