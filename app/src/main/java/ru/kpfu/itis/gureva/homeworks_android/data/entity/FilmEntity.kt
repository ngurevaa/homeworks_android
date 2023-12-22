package ru.kpfu.itis.gureva.homeworks_android.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.kpfu.itis.gureva.homeworks_android.model.FilmModel

@Entity(tableName = "film", indices = [Index(value = ["name", "release_year"], unique = true)])
data class FilmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    @ColumnInfo(name = "release_year") val releaseYear: Int,
    val description: String
) {
    fun toFilmModel(): FilmModel = FilmModel(id, name, releaseYear, description, 0.0, false)
}
