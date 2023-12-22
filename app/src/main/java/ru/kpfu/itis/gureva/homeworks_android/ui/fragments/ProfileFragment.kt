package ru.kpfu.itis.gureva.homeworks_android.ui.fragments

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.data.db.AppDatabase
import ru.kpfu.itis.gureva.homeworks_android.data.db.AppSharedPreferences
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentProfileBinding
import ru.kpfu.itis.gureva.homeworks_android.utils.RegexUtil
import ru.kpfu.itis.gureva.homeworks_android.utils.UserRepository

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val fragmentContainerId: Int = R.id.main_container
    private var binding: FragmentProfileBinding? = null
    private var userRepository: UserRepository? = null
    private var userId: Int? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        userRepository = UserRepository(AppDatabase.getDatabase(requireContext()).userDao())

        val i = AppSharedPreferences.getSP(requireContext()).getInt(AppSharedPreferences.USER_ID, -1)
        if (i != -1) userId = i

        binding?.run {
            lifecycleScope.launch {
                val user = userRepository?.getById(userId!!)
                tvName.text = "${getString(R.string.name_hint)}: ${user?.name}"
                tvEmail.text = "${getString(R.string.email_hint)}: ${user?.email}"
                tvPhone.text = "${getString(R.string.phone_hint)}: ${user?.phone}"
            }

            btnEditPhone.setOnClickListener {
                layoutPhone.visibility = View.VISIBLE
                btnSavePhone.visibility = View.VISIBLE
                btnEditPhone.visibility = View.GONE
                tvPhone.visibility = View.GONE
            }

            btnSavePhone.setOnClickListener {
                val phone = etPhone.text.toString()
                if (checkPhoneValid()) {
                    lifecycleScope.launch {
                        userId?.let {
                            try {
                                userRepository?.changePhone(it, phone)

                                layoutPhone.visibility = View.GONE
                                btnSavePhone.visibility = View.GONE
                                btnEditPhone.visibility = View.VISIBLE
                                tvPhone.visibility = View.VISIBLE
                                tvPhone.text = "${getString(R.string.phone_hint)}: $phone"
                            } catch (e: SQLiteConstraintException) {
                                AlertDialog.Builder(requireContext())
                                    .setTitle(getString(R.string.phone_existing_error))
                                    .setPositiveButton(getString(R.string.btn_positive)) {_, _ ->}
                                    .show()
                            }
                        }
                    }
                }
            }

            btnChangePassword.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(fragmentContainerId, PasswordChangeFragment())
                    .addToBackStack(null)
                    .commit()
            }

            btnExit.setOnClickListener {
                AppSharedPreferences.getSP(requireContext()).edit {
                    putBoolean(AppSharedPreferences.IS_LOGIN, false)
                }

                parentFragmentManager.beginTransaction()
                    .replace(fragmentContainerId, LoginFragment())
                    .commit()
            }

            btnDeleteProfile.setOnClickListener {
                AppSharedPreferences.getSP(requireContext()).edit {
                    putBoolean(AppSharedPreferences.IS_LOGIN, false)
                }

                lifecycleScope.launch { userId?.let { it1 -> userRepository?.deleteById(it1) } }

                parentFragmentManager.beginTransaction()
                    .replace(fragmentContainerId, LoginFragment())
                    .commit()
            }
        }
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
        userRepository = null
    }
}
