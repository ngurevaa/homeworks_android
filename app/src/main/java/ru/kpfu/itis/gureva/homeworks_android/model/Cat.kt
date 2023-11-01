package ru.kpfu.itis.gureva.homeworks_android.model

data class Cat(
    val id: Int,
    val image: Int,
    val type: String,
    var like: Boolean,
    val description: String
)
