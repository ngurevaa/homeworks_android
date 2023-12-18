package ru.kpfu.itis.gureva.homeworks_android.model

import ru.kpfu.itis.gureva.homeworks_android.data.entity.FilmEntity

data class FilmModel(
    val id: Int,
    val name: String,
    val releaseYear: Int,
    val description: String,
    val rating: Double,
    var isFavourite: Boolean
) {
    fun toFilmEntity(): FilmEntity = FilmEntity(id, name, releaseYear, description, rating)
}
