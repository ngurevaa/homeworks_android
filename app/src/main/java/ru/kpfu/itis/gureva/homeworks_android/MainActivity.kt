package ru.kpfu.itis.gureva.homeworks_android

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.homeworks_android.databinding.ActivityMainBinding
import ru.kpfu.itis.gureva.homeworks_android.fragment.CoroutinesFragment
import ru.kpfu.itis.gureva.homeworks_android.fragment.HomeFragment
import ru.kpfu.itis.gureva.homeworks_android.fragment.NotificationsFragment
import ru.kpfu.itis.gureva.homeworks_android.listener.CoroutineListener
import ru.kpfu.itis.gureva.homeworks_android.listener.ReceiveListener
import ru.kpfu.itis.gureva.homeworks_android.util.NotificationHandler
import kotlin.random.Random

class MainActivity : AppCompatActivity(), ReceiveListener, CoroutineListener {
    private val fragmentContainerId: Int = R.id.main_container
    private lateinit var binding: ActivityMainBinding
    private var coroutinesList: MutableList<Deferred<Int>>? = null
    private var notificationHandler: NotificationHandler? = null
    private lateinit var receiver: BroadcastReceiver
    private val randomFrom = 1
    private val randomTo = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val isAirplaneModeEnabled = intent?.getBooleanExtra(AIRPLANE_MODE_EXTRA_NAME, false) ?: return

                (supportFragmentManager.findFragmentById(fragmentContainerId) as? HomeFragment)
                    ?.view?.findViewById<Button>(R.id.btn_show)?.isEnabled = !isAirplaneModeEnabled

                (supportFragmentManager.findFragmentById(fragmentContainerId) as? CoroutinesFragment)
                    ?.view?.findViewById<Button>(R.id.btn_start)?.isEnabled = !isAirplaneModeEnabled

                binding.run {
                    if (isAirplaneModeEnabled) {
                        cvAirplaneMode.root.visibility = View.VISIBLE
                    }
                    else {
                        cvAirplaneMode.root.visibility = View.INVISIBLE
                    }
                }
            }
        }
        ContextCompat.registerReceiver(this, receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED), ContextCompat.RECEIVER_EXPORTED)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(fragmentContainerId, HomeFragment())
                .commit()
        }
        setOnBottomNavigationListener()

        checkPermissionForSDK33More()

        checkExtras()
    }

    private fun checkExtras() {
        if (intent.extras?.containsKey(FROM_NOTIFICATION_EXTRA_NAME) == true) {
            binding.root.let { Snackbar.make(it,getString(R.string.open_from_notification), Snackbar.LENGTH_LONG).show() }
        }
        else if (intent.extras?.containsKey(SETTINGS_EXTRA_NAME) == true) {
            binding.bottomNavigation.selectedItemId = R.id.notifications

            supportFragmentManager.beginTransaction()
                .replace(fragmentContainerId, NotificationsFragment())
                .commit()
        }
    }

    private fun setOnBottomNavigationListener() {
        binding.bottomNavigation.setOnItemSelectedListener {item ->
            when(item.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(fragmentContainerId, HomeFragment())
                        .commit()
                    true
                }

                R.id.notifications -> {
                    supportFragmentManager.beginTransaction()
                        .replace(fragmentContainerId, NotificationsFragment())
                        .commit()
                    true
                }

                R.id.coroutines -> {
                    supportFragmentManager.beginTransaction()
                        .replace(fragmentContainerId, CoroutinesFragment())
                        .commit()
                    true
                }

                else -> false
            }
        }
    }

    override fun onCreateCoroutineListener() {
        createCoroutine()
    }

    private fun createCoroutine() {
        lifecycleScope.launch {
            coroutinesList = mutableListOf()

            launch(Dispatchers.IO) {
                repeat(CoroutinesFragment.count) {
                    coroutinesList?.add(async { getInt() })
                }
                if (CoroutinesFragment.async) {
                    coroutinesList?.awaitAll()
                }
                else {
                    coroutinesList?.joinAll()
                }

                notificationHandler = NotificationHandler(this@MainActivity).also {
                    it.createNotification(getString(R.string.coroutines_end_title),
                        getString(R.string.coroutines_end_description), NotificationsFragment.importance,
                            NotificationsFragment.visibility, hideContent = false, addAction = false)
                }
            }
        }
    }

    private suspend fun getInt(): Int {
        val number = Random.nextInt(randomFrom, randomTo)
        delay(timeMillis = number * 1000L)
        return number
    }

    override fun onPause() {
        super.onPause()

        if (CoroutinesFragment.stopOnBackground) {
            var count = 0
            coroutinesList?.forEach {
                if (it.isCompleted) {
                    count++
                }
                it.cancel()
            }
            Log.i(getString(R.string.completed_coroutine_number), "$count")
        }
    }

    private fun checkPermissionForSDK33More() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    val dialog = AlertDialog.Builder(this)
                        .setMessage(getString(R.string.repeated_permission_message))
                        .setPositiveButton(getString(R.string.give_permission)) { _, _ ->
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(permission),
                                POST_NOTIFICATIONS_REQUEST_CODE
                            )
                        }
                        .setNegativeButton(getString(R.string.do_not_give_permission)) { _, _ -> }
                        .show()

                    val color = ContextCompat.getColor(this, R.color.brown)
                    dialog.run {
                        getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(color)
                        getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(color)
                    }
                }
                else {
                    ActivityCompat.requestPermissions(this, arrayOf(permission), POST_NOTIFICATIONS_REQUEST_CODE)
                }
            }
        }
    }

    override fun setOnReceiveListener(intent: Intent) {
        receiver.onReceive(this, intent)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    companion object {
        const val POST_NOTIFICATIONS_REQUEST_CODE = 1001
        const val AIRPLANE_MODE_EXTRA_NAME = "state"
        const val FROM_NOTIFICATION_EXTRA_NAME = "from_notification"
        const val SETTINGS_EXTRA_NAME = "settings"
    }
}
