package ru.kpfu.itis.gureva.homeworks_android.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gureva.homeworks_android.data.dao.UserDao
import ru.kpfu.itis.gureva.homeworks_android.model.UserModel

class UserRepository(private val dao: UserDao) {
    suspend fun getByEmailAndPassword(email: String, password: String): UserModel? {
        return withContext(Dispatchers.IO) {
            return@withContext dao.getByEmailAndPassword(email, password)?.toUserModel()
        }
    }

    suspend fun save(userModel: UserModel) {
        withContext(Dispatchers.IO) {
            dao.save(userModel.toUserEntity())
        }
    }

    suspend fun changePhone(id: Int, phone: String) {
        withContext(Dispatchers.IO) {
            dao.changePhone(id, phone)
        }
    }

    suspend fun getById(id: Int): UserModel {
        return withContext(Dispatchers.IO) {
            return@withContext dao.getById(id).toUserModel()
        }
    }

    suspend fun changePassword(id: Int, password: String) {
        withContext(Dispatchers.IO) {
            dao.changePassword(id, password)
        }
    }
}
