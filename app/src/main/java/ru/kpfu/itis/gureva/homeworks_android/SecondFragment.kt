package ru.kpfu.itis.gureva.homeworks_android

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentSecondBinding

class SecondFragment : Fragment(R.layout.fragment_second) {
    private var binding: FragmentSecondBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSecondBinding.bind(view)

        var arg: String? = arguments?.getString(ARG_INPUT_TEXT)
        if (!arg.equals("")) {
            binding?.tvInput?.text = arg
        }
        println(arg)


        binding?.run {
            btnThird.setOnClickListener {
                parentFragmentManager.popBackStack()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_container, ThirdFragment.newInstance(arg!!))
                    .addToBackStack(null)
                    .commit()
            }

            btnFirst.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    companion object {
        private const val ARG_INPUT_TEXT = "arg_input_text"

        fun newInstance(text: String) = SecondFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_INPUT_TEXT, text)
            }
        }
    }
}
