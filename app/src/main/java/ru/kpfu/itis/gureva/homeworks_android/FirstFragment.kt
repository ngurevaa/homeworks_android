package ru.kpfu.itis.gureva.homeworks_android

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentFirstBinding

interface OnSelectedButtonListener {
    fun onButtonSelected(text: String)
}

class FirstFragment : Fragment(R.layout.fragment_first), View.OnClickListener {
    private var binding: FragmentFirstBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFirstBinding.bind(view)

        binding?.run {
            btnEnter.setOnClickListener {
                val text = etInput.text.toString()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_container, SecondFragment.newInstance(text))
                    .addToBackStack(null)
                    .commit()

                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_container, ThirdFragment.newInstance(text))
                    .addToBackStack(null)
                    .commit()
            }

            btnSave.setOnClickListener(this@FirstFragment)
        }
    }

    override fun onClick(v: View?) {
        val listener = activity as OnSelectedButtonListener?
        val text = binding?.etInput?.text
        listener?.onButtonSelected(text.toString())
        text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
