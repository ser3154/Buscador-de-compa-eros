package servando.contreras.buscadordecompaerosdeactividades.data

import androidx.compose.runtime.saveable.listSaver

/** Perfil de la persona que usa la app. Se edita en Configuración y se muestra en Mi perfil. */
data class UserProfile(
    val fullName: String,
    val username: String,
    val email: String,
    val city: String,
    val bio: String,
    val photoUri: String? = null,
    val interests: List<String> = listOf("Deporte", "Estudio", "Hobby · Social"),
    val friends: Int = 24,
    val pendingFriendRequests: Int = 3
) {
    val initials: String
        get() = initialsOf(fullName).ifEmpty { "?" }

    companion object {
        val Sample = UserProfile(
            fullName = "Lucía Martín",
            username = "lucia.en.movimiento",
            email = "lucia.martin@correo.com",
            city = "Madrid, España",
            bio = "Corredora de domingo, fan de los juegos de mesa y siempre con un libro a mano. " +
                "Me apunto a planes tranquilos y buena conversación."
        )

        /** Permite guardar el perfil con rememberSaveable (rotación de pantalla, etc.). */
        val Saver = listSaver<UserProfile, String>(
            save = { listOf(it.fullName, it.username, it.email, it.city, it.bio, it.photoUri.orEmpty()) },
            restore = {
                Sample.copy(
                    fullName = it[0], username = it[1], email = it[2], city = it[3], bio = it[4],
                    photoUri = it[5].ifEmpty { null }
                )
            }
        )
    }
}
