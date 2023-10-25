package ru.kpfu.itis.gureva.homeworks_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val fragmentContainerId: Int = R.id.main_container
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(fragmentContainerId, StartFragment())
                .commit()
        }
    }
}
