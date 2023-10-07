package ru.kpfu.itis.gureva.homeworks_android

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentFirstBinding

class FirstFragment : Fragment(R.layout.fragment_first) {
    private var binding: FragmentFirstBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFirstBinding.bind(view)

        binding?.run {
            btnEnter.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_container, SecondFragment.newInstance(etInput.text.toString()))
                    .addToBackStack(null)
                    .commit()

                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_container, ThirdFragment.newInstance(etInput.text.toString()))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
