package servando.contreras.buscadordecompaerosdeactividades.data

import androidx.annotation.DrawableRes

data class Plan(
    val id: Int,
    val category: String,
    val date: String,
    val title: String,
    val place: String,
    @DrawableRes val imageRes: Int? = null
)