package ru.kpfu.itis.gureva.homeworks_android.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.gureva.homeworks_android.model.Cat

class CatDiffUtil(
    private val oldItems: List<Cat>,
    private val newItems: List<Cat>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return oldItem.type == newItem.type
                && oldItem.image == newItem.image
                    && oldItem.description == newItem.description
    }
}
