package ru.kpfu.itis.gureva.homeworks_android

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentStartBinding

class StartFragment : Fragment(R.layout.fragment_start) {
    private var binding: FragmentStartBinding? = null
    private val fragmentContainerId: Int = R.id.main_container
    private var phoneIsValidated = false
    private var questionIsValidated = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStartBinding.bind(view)

        binding?.run {
            btnStart.isEnabled = false
            initTextChangedListeners()
            btnStart.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(fragmentContainerId, ViewPagerFragment(etQuestions.text.toString().toInt()))
                    .commit()
            }
        }
    }

    private fun initTextChangedListeners() {
        with(binding) {
            this?.etPhone?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s != null) {
                        if (s.length >= 2 && s.substring(0, 2) == "89") {
                            var numberCounter = 0
                            s.forEach {
                                if (it.isDigit()) {
                                    numberCounter++
                                }
                            }

                            if (numberCounter != 11) {
                                etPhone.error = getString(R.string.et_phone_error_length_11)
                                phoneIsValidated = false
                            }
                            else {
                                phoneIsValidated = true
                            }
                        }
                        else if (s.length >= 3 && s.substring(0, 3) == "+79") {
                            var numberCounter = 0
                            s.forEach {
                                if (it.isDigit()) {
                                    numberCounter++
                                }
                            }

                            if (numberCounter != 11) {
                                etPhone.error = getString(R.string.et_phone_error_length_12)
                                phoneIsValidated = false
                            }
                            else {
                                phoneIsValidated = true
                            }
                        }
                        else {
                            etPhone.error = getString(R.string.et_phone_error_start)
                            phoneIsValidated = false
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    btnStart.isEnabled = phoneIsValidated && questionIsValidated
                }
            })

            this?.etQuestions?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s != null) {
                        if (s.isNotEmpty()) {
                            if (s.substring(0, 1) == "0") {
                                etQuestions.error = getString(R.string.et_questions_error_positive_number)
                                questionIsValidated = false
                            } else {
                                questionIsValidated = true
                            }
                        }
                        else {
                            questionIsValidated = false
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    btnStart.isEnabled = phoneIsValidated && questionIsValidated
                }
            })
        }
    }
}
