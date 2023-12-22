package ru.kpfu.itis.gureva.homeworks_android.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.data.db.AppDatabase
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentPasswordChangeBinding
import ru.kpfu.itis.gureva.homeworks_android.utils.UserRepository

class PasswordChangeFragment : Fragment(R.layout.fragment_password_change) {
    private var binding: FragmentPasswordChangeBinding? = null
    private var userId: Int? = null
    private var userRepository: UserRepository? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPasswordChangeBinding.bind(view)

        userRepository = UserRepository(AppDatabase.getDatabase(requireContext()).userDao())

        userId = arguments?.getInt(ARG_USER_ID) ?: 0

        binding?.run {
            btnSave.setOnClickListener {
                val oldPasswordInput = etOldPassword.text.toString()
                lifecycleScope.launch {
                    val oldPassword = userRepository?.getById(userId!!)?.password

                    if (oldPasswordInput == oldPassword) {
                        val newPassword = etNewPassword.text.toString()
                        // проверка пароля
                        val passwordValid = true
                        if (passwordValid) {
                            userId?.let { userRepository?.changePassword(it, newPassword) }

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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        userRepository = null
    }

    companion object {
        private const val ARG_USER_ID = "arg_user_id"

        fun newInstance(userId: Int) = PasswordChangeFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_USER_ID, userId)
            }
        }
    }
}
