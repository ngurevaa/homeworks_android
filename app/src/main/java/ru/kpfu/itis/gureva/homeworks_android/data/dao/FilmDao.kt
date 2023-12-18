package ru.kpfu.itis.gureva.homeworks_android.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.gureva.homeworks_android.data.entity.FilmEntity

@Dao
interface FilmDao {
    @Insert
    fun save(film: FilmEntity)

    @Query("SELECT * FROM film WHERE id = :id")
    fun get(id: Int): FilmEntity?

    @Query("SELECT * FROM film")
    fun getAll(): List<FilmEntity>?
}
