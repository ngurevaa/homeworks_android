package ru.kpfu.itis.gureva.homeworks_android.data.db

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class AppSharedPreferences {
    companion object {
        const val NAME = ""
        const val IS_LOGIN = "is_login"
        const val USER_ID = "user_id"

        fun getSP(context: Context): SharedPreferences {
            return context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        }
    }
}
