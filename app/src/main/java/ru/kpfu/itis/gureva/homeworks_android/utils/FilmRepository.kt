package ru.kpfu.itis.gureva.homeworks_android.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gureva.homeworks_android.data.dao.FilmDao
import ru.kpfu.itis.gureva.homeworks_android.model.FilmModel

class FilmRepository(private val dao: FilmDao) {
    suspend fun save(filmModel: FilmModel) {
        withContext(Dispatchers.IO) {
            dao.save(filmModel.toFilmEntity())
        }
    }

    suspend fun getAll(): List<FilmModel> {
        return withContext(Dispatchers.IO) {
            val filmEntities = dao.getAll()
            val filmModels = ArrayList<FilmModel>()
            filmEntities?.forEach {
                filmModels.add(it.toFilmModel())
            }
            return@withContext filmModels
        }
    }
}
