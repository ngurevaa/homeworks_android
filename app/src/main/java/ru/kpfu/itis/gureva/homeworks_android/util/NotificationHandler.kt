package ru.kpfu.itis.gureva.homeworks_android.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ru.kpfu.itis.gureva.homeworks_android.MainActivity
import ru.kpfu.itis.gureva.homeworks_android.R

class NotificationHandler(
    private val context: Context
) {
    fun createNotification(title:String, description: String, importance: Int, visibility: Int, hideContent: Boolean, addAction: Boolean) {
        var flag = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                flag = false
            }
        }
        if (flag) {
            var intent = Intent(context, MainActivity::class.java)
            val pending = PendingIntent.getActivity(context, REQUEST_CODE_OPEN_HOME, intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(context, context.getString(R.string.default_notification_channel_id))
                    .setSmallIcon(R.drawable.app)
                    .setContentTitle(title)
                    .setPriority(importance)
                    .setVisibility(visibility)
                    .setAutoCancel(true)
                    .setContentIntent(pending)

            if (hideContent) {
                builder.setStyle(NotificationCompat.BigTextStyle().bigText(description))
            }
            else {
                builder.setContentText(description)
            }

            if (addAction) {
                intent.putExtra(MainActivity.FROM_NOTIFICATION_EXTRA_NAME, true)
                PendingIntent.getActivity(context, REQUEST_CODE_OPEN_FROM_NOTIFICATION, intent,
                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE).also {
                    builder.addAction(R.drawable.home, context.getString(R.string.btn_home), it)
                }

                Intent(context, MainActivity::class.java).also {
                    it.putExtra(MainActivity.SETTINGS_EXTRA_NAME, true)
                    PendingIntent.getActivity(context, REQUEST_CODE_OPEN_SETTINGS, it,
                        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE).also { pend ->
                        builder.addAction(R.drawable.home, context.getString(R.string.btn_settings), pend)
                    }
                }
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel(context.getString(R.string.default_notification_channel_id),
                    context.getString(R.string.default_notification_channel_name), importance)
                    .also {
                        notificationManager.createNotificationChannel(it)
                    }
            }

            notificationManager.notify(NOTIFICATION_IDENTIFIER, builder.build())
        }
    }

    companion object {
        const val REQUEST_CODE_OPEN_HOME = 100
        const val REQUEST_CODE_OPEN_FROM_NOTIFICATION = 200
        const val REQUEST_CODE_OPEN_SETTINGS = 300
        const val NOTIFICATION_IDENTIFIER = 1000
    }
}
