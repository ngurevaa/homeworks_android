package ru.kpfu.itis.gureva.homeworks_android.data.db

import android.app.Application

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getDatabase(this)
    }
}
