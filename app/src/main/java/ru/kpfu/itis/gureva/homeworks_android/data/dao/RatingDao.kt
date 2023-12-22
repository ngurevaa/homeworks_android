package ru.kpfu.itis.gureva.homeworks_android.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.kpfu.itis.gureva.homeworks_android.data.entity.RatingEntity

@Dao
interface RatingDao {
    @Query("SELECT value FROM rating WHERE film_id = :filmId")
    fun getById(filmId: Int): List<Double>

    @Query("SELECT * from rating WHERE user_id = :userId AND film_id = :filmId")
    fun exist(userId: Int, filmId: Int): RatingEntity?

    @Query("UPDATE rating SET value = :value WHERE user_id = :userId AND film_id = :filmId")
    fun updateRating(userId: Int, filmId: Int, value: Float)

    @Query("INSERT INTO rating(user_id, film_id, value) VALUES (:userId, :filmId, :value)")
    fun insertRating(userId: Int, filmId: Int, value: Float)
}
