package ru.kpfu.itis.gureva.homeworks_android.fragment

import android.os.Bundle
import android.transition.Fade
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import ru.kpfu.itis.gureva.homeworks_android.CatRepository
import ru.kpfu.itis.gureva.homeworks_android.DetailsTransition
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.adapter.CatAdapter
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentCatBinding
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemCatBinding
import ru.kpfu.itis.gureva.homeworks_android.holder.CatViewHolder
import ru.kpfu.itis.gureva.homeworks_android.model.Cat
import java.util.ArrayList

class CatFragment : Fragment(R.layout.fragment_cat) {
    private var binding: FragmentCatBinding? = null
    private var catNumber: Int = 0
    private var adapter: CatAdapter? = null
    private val fragmentContainerId: Int = R.id.main_container

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCatBinding.bind(view)

        catNumber = arguments?.getInt(ARG_CAT_NUMBER) ?: 0

        adapter = CatAdapter(
            CatRepository.list.subList(0, catNumber),
            :: onLikeClicked,
            :: onCatClicked,
            :: onButtonClicked
        )

        binding?.run {
            if (catNumber <= 12) {
                rvCat.layoutManager = LinearLayoutManager(context)
            } else {
                val manager = GridLayoutManager(context, 2)
                manager.spanSizeLookup = object : SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position % 9 == 0) {
                            2
                        } else {
                            1
                        }
                    }
                }
                rvCat.layoutManager = manager
            }
            rvCat.adapter = adapter
        }
    }

    private fun onLikeClicked(position: Int, cat: Cat) {
        adapter?.updateItem(position, cat)
    }

    private fun onCatClicked(holder: CatViewHolder, position: Int) {
        val details = DetailFragment.newInstance(position)
//        details.sharedElementEnterTransition = DetailsTransition()
//        details.enterTransition = Fade()
//        exitTransition = Fade()
//        details.sharedElementReturnTransition = DetailsTransition()
//
//        parentFragmentManager.beginTransaction()
////            .setReorderingAllowed(true)
//            .addSharedElement(binding.ivCat, "second_cat")
//            .replace(fragmentContainerId, details)
//            .addToBackStack(null)
//            .commit()
        details.sharedElementEnterTransition = DetailsTransition()
        details.enterTransition = Fade()
        exitTransition = Fade()
        details.sharedElementReturnTransition = DetailsTransition()
        parentFragmentManager
            .beginTransaction()
            .addSharedElement(holder.image, "kittenImage")
            .replace(R.id.main_container, details)
            .addToBackStack(null)
            .commit()
    }

    private fun onButtonClicked(position: Int) {
        DialogFragment(::onButtonCountClick).show(parentFragmentManager, "tag")
    }

    private fun onButtonCountClick(count: Int) {
        val newItems = ArrayList(adapter?.getItems())
        println(newItems)
        var randomIndexNewItems: Int
        var randomIndexCatRepository: Int

        for (i in 1..count) {
            randomIndexNewItems = (0 until (newItems.size)).random()
            randomIndexCatRepository = (0 until CatRepository.list.size).random()
            newItems.add(randomIndexNewItems, CatRepository.list[randomIndexCatRepository])
        }
        adapter?.updateItems(newItems)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val ARG_CAT_NUMBER = "arg_cat_number"

        fun newInstance(catNumber: Int) = CatFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_CAT_NUMBER, catNumber)
            }
        }
    }
}
