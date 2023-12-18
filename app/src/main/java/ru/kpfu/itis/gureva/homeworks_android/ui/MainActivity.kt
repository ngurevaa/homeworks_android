package ru.kpfu.itis.gureva.homeworks_android.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.ui.fragments.LoginFragment

class MainActivity : AppCompatActivity() {
    private val fragmentContainerId: Int = R.id.main_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(fragmentContainerId, LoginFragment())
                .commit()
        }
    }
}
