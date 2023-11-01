package ru.kpfu.itis.gureva.homeworks_android.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.adapter.CatAdapter
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentDialogBinding

class DialogFragment(
    private val onButtonCountClick: (Int) -> Unit
) : BottomSheetDialogFragment(R.layout.fragment_dialog) {
    private var binding: FragmentDialogBinding? = null
    private var validate = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDialogBinding.bind(view)

        binding?.run {
            initTextChangeListener()

            btnStart.setOnClickListener {
                val countNewItems = etInput.text.toString().toInt()
                onButtonCountClick(countNewItems)
                dismiss()
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
                        if (s.toString().toInt() <= 5) {
                            validate = true
                        }
                        else {
                            etInput.error = getString(R.string.new_cat_input_error)
                        }
                    }
                    else {
                        validate = false
                        etInput.error = getString(R.string.new_cat_input_error)
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
