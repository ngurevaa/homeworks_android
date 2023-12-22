package ru.kpfu.itis.gureva.homeworks_android.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.data.db.AppDatabase
import ru.kpfu.itis.gureva.homeworks_android.data.db.AppSharedPreferences
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentLoginBinding
import ru.kpfu.itis.gureva.homeworks_android.utils.PasswordUtil
import ru.kpfu.itis.gureva.homeworks_android.utils.UserRepository

class LoginFragment : Fragment(R.layout.fragment_login) {
    private var binding: FragmentLoginBinding? = null
    private var repository: UserRepository? = null
    private val fragmentContainerId: Int = R.id.main_container

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        repository =  UserRepository(AppDatabase.getDatabase(requireContext()).userDao())

        if (AppSharedPreferences.getSP(requireContext()).getBoolean(AppSharedPreferences.IS_LOGIN, false)) {
            parentFragmentManager.beginTransaction()
                .replace(fragmentContainerId, MainFragment())
                .commit()
        }
        else {
            binding?.run {
                btnSignIn.setOnClickListener {
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()

                    lifecycleScope.launch {
                        val user = repository?.getByEmailAndPassword(email, PasswordUtil.encrypt(password))

                        if (user != null) {
                            AppSharedPreferences.getSP(requireContext()).edit {
                                putBoolean(AppSharedPreferences.IS_LOGIN, true)
                                putInt(AppSharedPreferences.USER_ID, user.id)
                            }

                            parentFragmentManager.beginTransaction()
                                .replace(fragmentContainerId, MainFragment())
                                .commit()
                        }
                        else {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(getString(R.string.login_error_title))
                                .setMessage(getString(R.string.login_error_message))
                                .setPositiveButton(getString(R.string.btn_positive)) {_, _ ->}
                                .show()
                        }
                    }
                }

                btnSignUp.setOnClickListener {
                    parentFragmentManager.beginTransaction()
                        .replace(fragmentContainerId, RegistrationFragment())
                        .commit()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        repository = null
    }
}
