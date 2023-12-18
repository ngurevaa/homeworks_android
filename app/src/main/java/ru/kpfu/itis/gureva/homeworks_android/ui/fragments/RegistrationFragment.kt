package ru.kpfu.itis.gureva.homeworks_android.ui.fragments

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.data.db.AppDatabase
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentRegistrationBinding
import ru.kpfu.itis.gureva.homeworks_android.model.UserModel
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
                val fieldValid = checkFieldValidation()

                if (fieldValid) {
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()
                    val phone = etPhone.text.toString()
                    val name = etName.text.toString()

                    lifecycleScope.launch {
                        try {
                            repository?.save(UserModel(0, name, phone, email, password))

                            MaterialAlertDialogBuilder(requireContext())
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
                            etEmail.error = getString(R.string.email_existing_error)
                        }
                    }
                }
            }
        }
    }

    private fun checkFieldValidation(): Boolean {
        binding?.run {
            if (etEmail.text?.isEmpty() == true) {
                etEmail.error = "Email can not be empty"
                return false
            }
            else if (etPassword.text?.isEmpty() == true) {
                etPassword.error = "Password can not be empty"
                return false
            }
            else if (etName.text?.isEmpty() == true) {
                etName.error = "Name can not be empty"
                return false
            }
            else if (etPhone.text?.isEmpty() == true) {
                etPhone.error = "Phone can not be empty"
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
