package ru.kpfu.itis.gureva.homeworks_android

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentFourthBinding

class FourthFragment : Fragment(R.layout.fragment_fourth) {
    private var binding: FragmentFourthBinding? = null
    private var counter = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFourthBinding.bind(view)
    }

    fun setText(text: String) {
        binding?.run {
            if (counter % 3 == 0) {
                tv1.text = text
            }
            else if (counter % 3 == 1) {
                tv2.text = text
            }
            else {
                tv3.text = text
            }
        }
        counter++
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding?.run {
            outState.putString(ARG_FIRST, tv1.text.toString())
            outState.putString(ARG_SECOND, tv2.text.toString())
            outState.putString(ARG_THIRD, tv3.text.toString())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        savedInstanceState?.getString(ARG_FIRST).let {
            binding?.tv1?.text = it
        }
        savedInstanceState?.getString(ARG_SECOND).let {
            binding?.tv2?.text = it
        }
        savedInstanceState?.getString(ARG_THIRD).let {
            binding?.tv3?.text = it
        }
    }
    companion object {
        private const val ARG_FIRST = "arg_first"
        private const val ARG_SECOND = "arg_second"
        private const val ARG_THIRD = "arg_third"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
