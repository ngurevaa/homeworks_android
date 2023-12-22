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
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentProfileBinding
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

        userId = arguments?.getInt(ARG_USER_ID) ?: 0

        binding?.run {
            // получить данные о профиле и высветить их
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
                // проверить телефон
                val phone = etPhone.text.toString()
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

            btnChangePassword.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(fragmentContainerId, PasswordChangeFragment.newInstance(userId!!))
                    .addToBackStack(null)
                    .commit()
            }

            btnExit.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(fragmentContainerId, LoginFragment())
                    .commit()
            }

            btnDeleteProfile.setOnClickListener {
                //удалить профиль из базы данных


                parentFragmentManager.beginTransaction()
                    .replace(fragmentContainerId, LoginFragment())
                    .commit()
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

        fun newInstance(userId: Int) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_USER_ID, userId)
            }
        }
    }
}
