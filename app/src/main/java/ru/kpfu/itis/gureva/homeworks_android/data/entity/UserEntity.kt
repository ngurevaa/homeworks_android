package ru.kpfu.itis.gureva.homeworks_android.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.kpfu.itis.gureva.homeworks_android.model.UserModel

@Entity(tableName = "user",
    indices = [Index(value = ["phone"], unique = true), Index(value = ["email"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 1,
    val name: String,
    val phone: String,
    val email: String,
    val password: String
) {
    fun toUserModel(): UserModel = UserModel(id, name, phone, email, password)
}
