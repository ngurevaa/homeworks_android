package ru.kpfu.itis.gureva.homeworks_android.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gureva.homeworks_android.data.dao.RatingDao
import ru.kpfu.itis.gureva.homeworks_android.model.RatingModel

class RatingRepository(private val dao: RatingDao) {
    suspend fun getByFilmId(filmId: Int): Double {
        return withContext(Dispatchers.IO) {
            val list = dao.getById(filmId)
            println("List: $list")
            var sum = 0.0
            list.forEach {
                sum += it
            }
            if (list.isNotEmpty()) {
                sum /= list.size
            }
            return@withContext sum
        }
    }

    suspend fun save(ratingModel: RatingModel) {
        withContext(Dispatchers.IO) {
            if (exist(ratingModel.userId, ratingModel.filmId)) {
                dao.updateRating(ratingModel.userId, ratingModel.filmId, ratingModel.value)
            }
            else {
                dao.insertRating(ratingModel.userId, ratingModel.filmId, ratingModel.value)
            }
        }
    }

    suspend fun exist(userId: Int, filmId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            if (dao.exist(userId, filmId) != null) {
                return@withContext true
            }
            return@withContext false
        }
    }
}
