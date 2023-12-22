package ru.kpfu.itis.gureva.homeworks_android.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gureva.homeworks_android.data.dao.FavouriteDao
import ru.kpfu.itis.gureva.homeworks_android.data.dao.FilmDao
import ru.kpfu.itis.gureva.homeworks_android.model.FilmModel

class FavouriteRepository(
    private val favouriteDao: FavouriteDao,
    private val filmDao: FilmDao) {
    suspend fun getAll(userId: Int): List<FilmModel> {
        return withContext(Dispatchers.IO) {
            val filmIdList = favouriteDao.getAll(userId)
            val films = ArrayList<FilmModel>()
            filmIdList?.forEach {
                filmDao.getById(it)?.let { film -> films.add(film.toFilmModel().apply { isFavourite = true }) }
            }
            return@withContext films
        }
    }

    suspend fun save(userId: Int, filmId: Int) {
        withContext(Dispatchers.IO) {
            favouriteDao.save(userId, filmId)
        }
    }

    suspend fun delete(userId: Int, filmId: Int) {
        withContext(Dispatchers.IO) {
            favouriteDao.delete(userId, filmId)
        }
    }

    suspend fun checkFilmStatus(userId: Int, filmId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val status = favouriteDao.checkFilmStatus(userId, filmId)
            return@withContext status != null
        }
    }
}
