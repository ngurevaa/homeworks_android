package ru.kpfu.itis.gureva.homeworks_android.util

import android.content.Context
import android.content.Intent
import android.provider.Settings
import ru.kpfu.itis.gureva.homeworks_android.MainActivity

class AirplaneModeUtil(
    private val context: Context
) {
    fun getIntentAirplaneMode(): Intent {
        val intent = Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        if (Settings.System.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) == 0) {
            intent.putExtra(MainActivity.AIRPLANE_MODE_EXTRA_NAME, false)
        }
        else {
            intent.putExtra(MainActivity.AIRPLANE_MODE_EXTRA_NAME, true)
        }
        return intent
    }
}
