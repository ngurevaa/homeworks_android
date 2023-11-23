package ru.kpfu.itis.gureva.homeworks_android.fragment

import android.app.Notification
import android.app.NotificationManager
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment(R.layout.fragment_notifications) {
    private var binding: FragmentNotificationsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationsBinding.bind(view)

        binding?.run {
            updateSettings()

            rgImportance.setOnCheckedChangeListener { _, checkId ->
                when(checkId) {
                    R.id.rb_urgent -> {
                        importance = NotificationManager.IMPORTANCE_HIGH
                    }

                    R.id.rb_high -> {
                        importance = NotificationManager.IMPORTANCE_DEFAULT
                    }

                    R.id.rb_medium -> {
                        importance = NotificationManager.IMPORTANCE_LOW
                    }
                }
            }

            rgVisibility.setOnCheckedChangeListener { _, checkId ->
                when(checkId) {
                    R.id.rb_public -> {
                        visibility = Notification.VISIBILITY_PUBLIC
                    }

                    R.id.rb_secret -> {
                        visibility = Notification.VISIBILITY_SECRET
                    }

                    R.id.rb_private -> {
                        visibility = Notification.VISIBILITY_PRIVATE
                    }
                }
            }

            cbHide.setOnCheckedChangeListener { _, isChecked ->
                hideContent = isChecked
            }

            cbAction.setOnCheckedChangeListener { _, isChecked ->
                addAction = isChecked
            }
        }
    }

    private fun updateSettings() {
        binding?.run {
            when(importance) {
                NotificationManager.IMPORTANCE_HIGH -> {
                    rgImportance.check(R.id.rb_urgent)
                }

                NotificationManager.IMPORTANCE_DEFAULT -> {
                    rgImportance.check(R.id.rb_high)
                }

                NotificationManager.IMPORTANCE_LOW -> {
                    rgImportance.check(R.id.rb_medium)
                }
            }

            when(visibility) {
                Notification.VISIBILITY_PUBLIC -> {
                    rgVisibility.check(R.id.rb_public)
                }

                Notification.VISIBILITY_SECRET -> {
                    rgVisibility.check(R.id.rb_secret)
                }

                Notification.VISIBILITY_PRIVATE -> {
                    rgVisibility.check(R.id.rb_private)
                }
            }

            cbHide.isChecked = hideContent
            cbAction.isChecked = addAction
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        var importance = NotificationManager.IMPORTANCE_HIGH
        var visibility = Notification.VISIBILITY_PUBLIC
        var hideContent = false
        var addAction = false
    }
}
