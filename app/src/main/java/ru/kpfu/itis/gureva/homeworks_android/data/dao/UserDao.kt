package ru.kpfu.itis.gureva.homeworks_android.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.gureva.homeworks_android.data.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    fun getByEmailAndPassword(email: String, password: String): UserEntity?

    @Insert
    fun save(user: UserEntity)
    //--------------------------------

    @Query("DELETE FROM user WHERE id = :id")
    fun deleteByUserId(id: Int)
    @Query("SELECT * FROM user WHERE id = :id")
    fun getById(id: Int): UserEntity
}
