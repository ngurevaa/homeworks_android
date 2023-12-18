package ru.kpfu.itis.gureva.homeworks_android.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gureva.homeworks_android.data.dao.UserDao
import ru.kpfu.itis.gureva.homeworks_android.model.UserModel

class UserRepository(private val dao: UserDao) {
    suspend fun getByEmailAndPassword(email: String, password: String): UserModel? {
//        var userModel: UserModel?
//        withContext(Dispatchers.IO) {
//            userModel = dao.getByEmailAndPassword(email, password)?.toUserModel()
//        }
//        return userModel
        return withContext(Dispatchers.IO) {
            return@withContext dao.getByEmailAndPassword(email, password)?.toUserModel()
        }
    }

    suspend fun save(userModel: UserModel) {
        withContext(Dispatchers.IO) {
            dao.save(userModel.toUserEntity())
        }
    }
}
