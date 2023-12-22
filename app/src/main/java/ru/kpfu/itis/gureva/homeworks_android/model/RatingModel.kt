package ru.kpfu.itis.gureva.homeworks_android.model

import ru.kpfu.itis.gureva.homeworks_android.data.entity.RatingEntity

data class RatingModel(
    val userId: Int,
    val filmId: Int,
    val value: Float
)
