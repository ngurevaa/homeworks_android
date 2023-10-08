package ru.kpfu.itis.gureva.homeworks_android

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentThirdBinding

class ThirdFragment : Fragment(R.layout.fragment_third) {
    private var binding: FragmentThirdBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentThirdBinding.bind(view)

        var arg: String? = arguments?.getString(ARG_INPUT_TEXT)
        if (!arg.equals("")) {
            binding?.tvInput?.text = arg
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val ARG_INPUT_TEXT = "arg_input_text"

        fun newInstance(text: String) = ThirdFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_INPUT_TEXT, text)
                }
            }
    }
}
