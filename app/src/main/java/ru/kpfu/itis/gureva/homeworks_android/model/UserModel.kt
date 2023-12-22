package ru.kpfu.itis.gureva.homeworks_android.model

import androidx.room.PrimaryKey
import ru.kpfu.itis.gureva.homeworks_android.data.entity.UserEntity

data class UserModel (
    val id: Int,
    val name: String,
    val phone: String,
    val email: String,
    val password: String
)  {
    fun toUserEntity(): UserEntity = UserEntity(id, name, phone, email, password)
}
