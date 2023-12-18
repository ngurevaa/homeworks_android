package ru.kpfu.itis.gureva.homeworks_android.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.kpfu.itis.gureva.homeworks_android.data.entity.FilmEntity

@Dao
interface FavouriteDao {
    @Query("SELECT film_id FROM favourite WHERE user_id = :userId")
    fun getAll(userId: Int): List<Int>?

    @Query("INSERT into favourite(user_id, film_id) VALUES(:userId, :filmId)")
    fun save(userId: Int, filmId: Int)

    @Query("DELETE FROM favourite WHERE user_id = :userId AND film_id = :filmId")
    fun delete(userId: Int, filmId: Int)
}
