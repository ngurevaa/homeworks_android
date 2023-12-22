package ru.kpfu.itis.gureva.homeworks_android.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.kpfu.itis.gureva.homeworks_android.data.dao.FavouriteDao
import ru.kpfu.itis.gureva.homeworks_android.data.dao.FilmDao
import ru.kpfu.itis.gureva.homeworks_android.data.dao.RatingDao
import ru.kpfu.itis.gureva.homeworks_android.data.dao.UserDao
import ru.kpfu.itis.gureva.homeworks_android.data.entity.FavouriteEntity
import ru.kpfu.itis.gureva.homeworks_android.data.entity.FilmEntity
import ru.kpfu.itis.gureva.homeworks_android.data.entity.RatingEntity
import ru.kpfu.itis.gureva.homeworks_android.data.entity.UserEntity

@Database(entities = [UserEntity::class, FilmEntity::class, FavouriteEntity::class, RatingEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun filmDao(): FilmDao
    abstract fun favouriteDao(): FavouriteDao
    abstract fun ratingDao(): RatingDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        const val DATABASE_NAME = "app_database"

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .build()
                    .also {
                        instance = it
                    }
            }
        }
    }
}
