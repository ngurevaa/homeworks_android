package ru.kpfu.itis.gureva.homeworks_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import ru.kpfu.itis.gureva.homeworks_android.adapter.diffutil.CatDiffUtil
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemButtonBinding
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemCatBinding
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemDataBinding
import ru.kpfu.itis.gureva.homeworks_android.model.Cat
import ru.kpfu.itis.gureva.homeworks_android.holder.ButtonViewHolder
import ru.kpfu.itis.gureva.homeworks_android.holder.CatViewHolder
import ru.kpfu.itis.gureva.homeworks_android.holder.DataViewHolder

class CatAdapter(
    private var items: MutableList<Cat>,
    private val onLikeClicked: ((Int, Cat) -> Unit),
    private val onCatClicked: ((CatViewHolder, Int) -> Unit),
    private val onButtonClicked: (() -> Unit),
    private val onCatLongClicked: ((ItemCatBinding, Boolean) -> Unit),
    private val onDeleteClicked: ((Int) -> Unit),
    private val glide: RequestManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    fun getItems() = items

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            1
        } else if (position % 9 == 0) {
            2
        } else {
            3
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            1 -> return ButtonViewHolder(
                binding = ItemButtonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onButtonClicked = onButtonClicked
            )
            2 -> return DataViewHolder(
                binding = ItemDataBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> return CatViewHolder(
                binding = ItemCatBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                itemCount,
                onLikeClicked = onLikeClicked,
                onCatClicked = onCatClicked,
                onCatLongClicked = onCatLongClicked,
                onDeleteClicked = onDeleteClicked,
                glide
            )
        }
    }

    override fun getItemCount(): Int {
        return items.size + items.size / 9 + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            1 -> (holder as ButtonViewHolder).onBind()
            2 -> (holder as DataViewHolder).onBind()
            else -> {
                (holder as CatViewHolder).onBind(items[position - position / 9 - 1])
                ViewCompat.setTransitionName(holder.getImage(), position.toString() + "_image")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            (payloads.first() as Boolean).let {
                (holder as CatViewHolder).changeLikeStatus(it)
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    fun updateItem(position: Int, cat: Cat) {
        items[position - position / 9 - 1] = cat
        notifyItemChanged(position, cat.like)
    }

    fun updateItems(newItems: List<Cat>) {
        val diff = CatDiffUtil(items, newItems)
        val difference = DiffUtil.calculateDiff(diff)
        items.clear()
        items.addAll(newItems)
        difference.dispatchUpdatesTo(this)
    }
}
