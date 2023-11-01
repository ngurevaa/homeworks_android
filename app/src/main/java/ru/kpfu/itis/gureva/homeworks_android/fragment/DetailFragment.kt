package ru.kpfu.itis.gureva.homeworks_android.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.fragment.app.Fragment
import ru.kpfu.itis.gureva.homeworks_android.CatRepository
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentDetailBinding
import ru.kpfu.itis.gureva.homeworks_android.model.Cat

class DetailFragment : Fragment(R.layout.fragment_detail) {
    private var binding: FragmentDetailBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)

        var position: Int = arguments?.getInt(ARG_CAT) ?: 0
        val cat = CatRepository.list[position]
        binding?.run {
            ivCat.setImageResource(cat.image)
            tvCat.text = cat.type
            tvDesc.text = cat.description
        }
    }

    companion object {
        const val ARG_CAT = "arg_cat"

        fun newInstance(position: Int) = DetailFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_CAT, position)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
