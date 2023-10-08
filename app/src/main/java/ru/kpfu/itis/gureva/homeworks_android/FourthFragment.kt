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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
