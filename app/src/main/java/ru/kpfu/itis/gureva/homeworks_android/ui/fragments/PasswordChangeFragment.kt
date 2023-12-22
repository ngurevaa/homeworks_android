package ru.kpfu.itis.gureva.homeworks_android.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.data.db.AppDatabase
import ru.kpfu.itis.gureva.homeworks_android.data.db.AppSharedPreferences
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentPasswordChangeBinding
import ru.kpfu.itis.gureva.homeworks_android.utils.PasswordUtil
import ru.kpfu.itis.gureva.homeworks_android.utils.RegexUtil
import ru.kpfu.itis.gureva.homeworks_android.utils.UserRepository

class PasswordChangeFragment : Fragment(R.layout.fragment_password_change) {
    private var binding: FragmentPasswordChangeBinding? = null
    private var userId: Int? = null
    private var userRepository: UserRepository? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPasswordChangeBinding.bind(view)

        userRepository = UserRepository(AppDatabase.getDatabase(requireContext()).userDao())

        val i = AppSharedPreferences.getSP(requireContext()).getInt(AppSharedPreferences.USER_ID, -1)
        if (i != -1) userId = i

        binding?.run {
            btnSave.setOnClickListener {
                val oldPasswordInput = etOldPassword.text.toString()
                lifecycleScope.launch {
                    val oldPassword = userRepository?.getById(userId!!)?.password

                    if (PasswordUtil.encrypt(oldPasswordInput) == oldPassword) {
                        val newPassword = etNewPassword.text.toString()

                        if (checkPasswordValid()) {
                            userId?.let { userRepository?.changePassword(it, PasswordUtil.encrypt(newPassword)) }

                            AlertDialog.Builder(requireContext())
                                .setTitle(getString(R.string.correct_password))
                                .setPositiveButton(getString(R.string.btn_positive)) {_, _ ->}
                                .show()
                        }

                        else {
                            layoutNewPassword.error = getString(R.string.password_requirements)
                        }
                    }
                    else {
                        AlertDialog.Builder(requireContext())
                            .setTitle(getString(R.string.password_mismatch_error))
                            .setPositiveButton(getString(R.string.btn_positive)) {_, _ ->}
                            .show()
                    }
                }
            }
        }
    }

    private fun checkPasswordValid(): Boolean {
        binding?.run {
            if (etNewPassword.text?.isEmpty() == true) {
                layoutNewPassword.error = getString(R.string.empty_password_error)
                return false
            }
            else if (!RegexUtil.check(RegexUtil.PASSWORD, etNewPassword.text.toString())) {
                layoutNewPassword.error = getString(R.string.password_requirements)
                return false
            }
            else {
                layoutNewPassword.error = null
            }
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        userRepository = null
    }
}
