package ru.kpfu.itis.gureva.homeworks_android.fragment

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.kpfu.itis.gureva.homeworks_android.MainActivity
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentHomeBinding
import ru.kpfu.itis.gureva.homeworks_android.listener.ReceiveListener
import ru.kpfu.itis.gureva.homeworks_android.util.AirplaneModeUtil
import ru.kpfu.itis.gureva.homeworks_android.util.NotificationHandler

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var binding: FragmentHomeBinding? = null
    private var notificationHandler: NotificationHandler? = null
    private var airplaneModeUtil: AirplaneModeUtil? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        airplaneModeUtil = AirplaneModeUtil(requireContext()).also {
            (activity as? ReceiveListener)?.setOnReceiveListener(it.getIntentAirplaneMode())
        }

        binding?.run {
            btnShow.setOnClickListener {
                val title = etTitle.text.toString()
                val description = etDescription.text.toString()

                notificationHandler = NotificationHandler(requireContext()).also {
                    it.createNotification(title, description,
                        NotificationsFragment.importance,
                        NotificationsFragment.visibility,
                        NotificationsFragment.hideContent,
                        NotificationsFragment.addAction)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        notificationHandler = null
        airplaneModeUtil = null
    }
}
