package ru.kpfu.itis.gureva.homeworks_android.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentStartBinding

class StartFragment : Fragment(R.layout.fragment_start) {
    private var binding: FragmentStartBinding? = null
    private val fragmentContainerId: Int = R.id.main_container
    private var validate = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStartBinding.bind(view)

        binding?.run {
            initTextChangeListener()

            btnStart.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(fragmentContainerId, CatFragment.newInstance(etInput.text.toString().toInt()))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun initTextChangeListener() {
        with(binding) {
            this?.etInput?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (!s.isNullOrEmpty()) {
                        if (s.toString().toInt() <= 45) {
                            validate = true
                        }
                        else {
                            etInput.error = getString(R.string.cat_input_error)
                        }
                    }
                    else {
                        validate = false
                        etInput.error = getString(R.string.cat_input_error)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    btnStart.isEnabled = validate
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
