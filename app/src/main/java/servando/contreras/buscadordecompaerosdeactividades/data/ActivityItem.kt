package servando.contreras.buscadordecompaerosdeactividades.data

import androidx.annotation.DrawableRes

enum class ParticipationState { VISITOR, PENDING, PARTICIPANT, CREATOR }

data class ActivityItem(
    val id: Int,
    val title: String,
    val category: String,
    val description: String,
    val date: String,
    val time: String,
    val place: String,
    val distance: String,
    val organizerName: String,
    val maxParticipants: Int,
    val confirmed: List<String> = emptyList(),
    val isPublic: Boolean = true,
    val requiresApproval: Boolean = false,
    val priceRange: String = "Gratis",
    val participation: ParticipationState = ParticipationState.VISITOR,
    @DrawableRes val imageRes: Int? = null
)