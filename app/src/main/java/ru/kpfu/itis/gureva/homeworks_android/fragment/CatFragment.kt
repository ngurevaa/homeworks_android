package ru.kpfu.itis.gureva.homeworks_android.fragment

import android.os.Bundle
import android.transition.Fade
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import ru.kpfu.itis.gureva.homeworks_android.CatRepository
import ru.kpfu.itis.gureva.homeworks_android.DetailsTransition
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.adapter.CatAdapter
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentCatBinding
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemCatBinding
import ru.kpfu.itis.gureva.homeworks_android.holder.CatViewHolder
import ru.kpfu.itis.gureva.homeworks_android.model.Cat


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
            :: onButtonClicked,
            :: onCatLongClicked,
            :: onDeleteClicked,
            Glide.with(this)
        )

        binding?.run {
            if (catNumber <= 12) {
                rvCat.layoutManager = LinearLayoutManager(context)

                val itemTouch = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                        val position = viewHolder.adapterPosition
                        onDeleteClicked(position)
                    }
                }
                val itemTouchHelper = ItemTouchHelper(itemTouch)
                itemTouchHelper.attachToRecyclerView(rvCat)
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
        details.sharedElementEnterTransition = DetailsTransition()
        details.enterTransition = Fade()
        details.sharedElementReturnTransition = DetailsTransition()
        parentFragmentManager
            .beginTransaction()
            .addSharedElement(holder.getImage(), TRANSITION_NAME)
            .replace(fragmentContainerId, details)
            .addToBackStack(null)
            .commit()
    }

    private fun onButtonClicked() {
        DialogFragment(::onButtonCountClick).show(parentFragmentManager, TAG)
    }

    private fun onCatLongClicked(binding: ItemCatBinding, repeat: Boolean) {
        binding.run {
            if (repeat) {
                ivCat.visibility = View.INVISIBLE
                tvCat.visibility = View.INVISIBLE
                ivLike.visibility = View.INVISIBLE
                ivDelete.visibility = View.VISIBLE
            }
            else {
                ivCat.visibility = View.VISIBLE
                tvCat.visibility = View.VISIBLE
                ivLike.visibility = View.VISIBLE
                ivDelete.visibility = View.INVISIBLE
            }
        }
    }

    private fun onDeleteClicked(position: Int) {
        println(position)
        val cat = adapter?.getItems()?.removeAt(position - position / 9 - 1)
        println(adapter?.getItems()?.size)
        adapter?.notifyItemRemoved(position)

        binding?.root?.let { Snackbar.make(it, "", Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.snackbar_delete)) {
                if (cat != null) {
                    val newItems = ArrayList(adapter?.getItems())
                    newItems.add(position - position / 9 - 1, cat)
                    adapter?.notifyItemChanged(position - 1)
                    adapter?.updateItems(newItems)
                }
            }.show() }
    }

    private fun onButtonCountClick(count: Int) {
        val newItems = ArrayList(adapter?.getItems())
        var randomIndexNewItems: Int
        var randomIndexCatRepository: Int

        for (i in 1..count) {
            if (newItems.size == 0) {
                randomIndexNewItems = 0
            }
            else {
                randomIndexNewItems = (0 until (newItems.size)).random()
            }

            randomIndexCatRepository = (0 until CatRepository.list.size).random()
            var cat = CatRepository.list[randomIndexCatRepository]
            cat.like = false
            newItems.add(randomIndexNewItems, cat)
        }
        adapter?.updateItems(newItems)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val TRANSITION_NAME = "kittenImage"
        private const val TAG = "cat_fragment_tag"
        private const val ARG_CAT_NUMBER = "arg_cat_number"

        fun newInstance(catNumber: Int) = CatFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_CAT_NUMBER, catNumber)
            }
        }
    }
}
