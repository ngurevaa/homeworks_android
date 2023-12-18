package ru.kpfu.itis.gureva.homeworks_android.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.data.db.AppDatabase
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentLoginBinding
import ru.kpfu.itis.gureva.homeworks_android.model.UserModel
import ru.kpfu.itis.gureva.homeworks_android.utils.UserRepository
import java.lang.Exception

class LoginFragment : Fragment(R.layout.fragment_login) {
    private var binding: FragmentLoginBinding? = null
    private var repository: UserRepository? = null
    private val fragmentContainerId: Int = R.id.main_container

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        repository =  UserRepository(AppDatabase.getDatabase(requireContext()).userDao())

        binding?.run {
            btnSignIn.setOnClickListener {
                val fieldsValid = checkFieldValidation()

                if (fieldsValid) {
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()

                    lifecycleScope.launch {
                        val user = repository?.getByEmailAndPassword(email, password)

                        if (user != null) {
                            parentFragmentManager.beginTransaction()
                                .replace(fragmentContainerId, MainFragment.newInstance(user.id))
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
            }

            btnSignUp.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(fragmentContainerId, RegistrationFragment())
                    .commit()
            }
        }
    }

    private fun checkFieldValidation(): Boolean {
        // убрать строчки
        binding?.run {
            if (etEmail.text?.isEmpty() == true) {
                etEmail.error = "Email can not be empty"
                return false
            }
            else if (etPassword.text?.isEmpty() == true) {
                etPassword.error = "Password can not be empty"
                return false
            }
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        repository = null
    }
}
