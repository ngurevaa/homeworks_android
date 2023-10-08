package ru.kpfu.itis.gureva.homeworks_android

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle

class MainActivity : AppCompatActivity(), OnSelectedButtonListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, FirstFragment())
            .commit()
        }
        else {
            supportFragmentManager.beginTransaction()
                .add(R.id.first_container, FirstFragment())
                .add(R.id.second_container, FourthFragment())
                .commit()
        }
    }

    override fun onButtonSelected(text: String) {
        (supportFragmentManager.findFragmentById(R.id.second_container) as FourthFragment)
            .setText(text)
    }
}
