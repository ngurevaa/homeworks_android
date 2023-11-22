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

                notificationHandler = NotificationHandler(requireContext())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val permission = Manifest.permission.POST_NOTIFICATIONS

                    if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                        notificationHandler?.createNotification(title, description,
                            NotificationsFragment.importance,
                            NotificationsFragment.visibility,
                            NotificationsFragment.hideContent,
                            NotificationsFragment.addAction
                        )
                    }
                    else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
                            val dialog = AlertDialog.Builder(requireContext())
                                .setMessage(getString(R.string.repeated_permission_message))
                                .setPositiveButton(getString(R.string.give_permission)) { _, _ ->
                                    ActivityCompat.requestPermissions(
                                        requireActivity(),
                                        arrayOf(permission),
                                        MainActivity.POST_NOTIFICATIONS_REQUEST_CODE
                                    )
                                }
                                .setNegativeButton(getString(R.string.do_not_give_permission)) { _, _ -> }
                                .show()

                            val color = ContextCompat.getColor(requireContext(), R.color.brown)
                            dialog.run {
                                getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(color)
                                getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(color)
                            }
                        }
                        else {
                            // third optional task
                            AlertDialog.Builder(requireContext())
                                .setMessage(getString(R.string.message_go_to_setting))
                                .setNeutralButton(getString(R.string.btn_settings)) { _, _ ->
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.fromParts(SCHEME, requireContext().packageName, null)
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }.also {
                                        startActivity(it)
                                    }
                                }
                                .show()
                        }
                    }
                }
                else {
                    notificationHandler?.createNotification(title, description,
                        NotificationsFragment.importance,
                        NotificationsFragment.visibility,
                        NotificationsFragment.hideContent,
                        NotificationsFragment.addAction
                    )
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

    companion object {
        private const val SCHEME = "package"
    }
}
