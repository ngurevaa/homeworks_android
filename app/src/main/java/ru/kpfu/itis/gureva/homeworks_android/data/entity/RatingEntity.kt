package ru.kpfu.itis.gureva.homeworks_android.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "rating", foreignKeys = [ForeignKey(entity = FilmEntity::class, parentColumns = ["id"],
    childColumns = ["film_id"], onDelete = ForeignKey.CASCADE), ForeignKey(entity = UserEntity::class,
        parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.NO_ACTION)])
data class RatingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "film_id") val filmId: Int,
    val value: Double
)
