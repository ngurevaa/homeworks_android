package ru.kpfu.itis.gureva.homeworks_android.ui.fragments

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.data.db.AppDatabase
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentRegistrationBinding
import ru.kpfu.itis.gureva.homeworks_android.model.UserModel
import ru.kpfu.itis.gureva.homeworks_android.utils.PasswordUtil
import ru.kpfu.itis.gureva.homeworks_android.utils.RegexUtil
import ru.kpfu.itis.gureva.homeworks_android.utils.UserRepository

class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private var binding: FragmentRegistrationBinding? = null
    private var repository: UserRepository? = null
    private val fragmentContainerId: Int = R.id.main_container

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegistrationBinding.bind(view)
        repository =  UserRepository(AppDatabase.getDatabase(requireContext()).userDao())

        binding?. run {
            btnSignUp.setOnClickListener {
                if (checkEmailValid() and checkPasswordValid() and checkNameValid() and checkPhoneValid()) {
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()
                    val phone = etPhone.text.toString()
                    val name = etName.text.toString()

                    lifecycleScope.launch {
                        try {
                            repository?.save(UserModel(0, name, phone, email, PasswordUtil.encrypt(password)))

                            AlertDialog.Builder(requireContext())
                                .setTitle(getString(R.string.registration_dialog_title))
                                .setMessage(getString(R.string.registration_dialog_message))
                                .setPositiveButton(getString(R.string.btn_positive)) {_, _ ->
                                    run {
                                        parentFragmentManager.beginTransaction()
                                            .replace(fragmentContainerId, LoginFragment())
                                            .commit()
                                    }
                                }
                                .show()
                        } catch (e: SQLiteConstraintException) {
                            layoutEmail.error = getString(R.string.email_existing_error)
                        }
                    }
                }
            }
        }
    }

    private fun checkEmailValid(): Boolean {
        binding?.run {
            if (etEmail.text?.isEmpty() == true) {
                layoutEmail.error = getString(R.string.empty_email_error)
                return false
            }
            else if (!RegexUtil.check(RegexUtil.EMAIL, etEmail.text.toString())) {
                layoutEmail.error = getString(R.string.invalid_email)
                return false
            }
            else {
                layoutEmail.error = null
            }
        }
        return true
    }

    private fun checkPasswordValid(): Boolean {
        binding?.run {
            if (etPassword.text?.isEmpty() == true) {
                layoutPassword.error = getString(R.string.empty_password_error)
                return false
            }
            else if (!RegexUtil.check(RegexUtil.PASSWORD, etPassword.text.toString())) {
                layoutPassword.error = getString(R.string.password_requirements)
                return false
            }
            else {
                layoutPassword.error = null
            }
        }
        return true
    }

    private fun checkNameValid(): Boolean {
        binding?.run {
            if (etName.text?.isEmpty() == true) {
                layoutName.error = getString(R.string.empty_name_error)
                return false
            }
            else if (!RegexUtil.check(RegexUtil.NAME, etName.text.toString())) {
                layoutName.error = getString(R.string.invalid_name)
                return false
            }
            else {
                layoutName.error = null
            }
        }
        return true
    }

    private fun checkPhoneValid(): Boolean {
        binding?.run {
            if (etPhone.text?.isEmpty() == true) {
                layoutPhone.error = getString(R.string.empty_phone_error)
                return false
            }
            else if (!RegexUtil.check(RegexUtil.PHONE, etPhone.text.toString())) {
                layoutPhone.error = getString(R.string.invalid_phone)
                return false
            }
            else {
                layoutPhone.error = null
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
