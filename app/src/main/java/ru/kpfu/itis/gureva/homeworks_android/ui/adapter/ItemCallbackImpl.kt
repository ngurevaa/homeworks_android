package ru.kpfu.itis.gureva.homeworks_android.ui.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.gureva.homeworks_android.model.FilmModel

class ItemCallbackImpl: DiffUtil.ItemCallback<FilmModel>() {
    override fun areItemsTheSame(oldItem: FilmModel, newItem: FilmModel): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: FilmModel, newItem: FilmModel): Boolean = oldItem == newItem

    override fun getChangePayload(oldItem: FilmModel, newItem: FilmModel): Any? {
        val bundle = Bundle()
        if (oldItem.isFavourite != newItem.isFavourite) {
            bundle.putBoolean(ARG_FAVOURITE, newItem.isFavourite)
        }
        return if (bundle.isEmpty) super.getChangePayload(oldItem, newItem) else bundle
    }

    companion object {
        const val ARG_FAVOURITE = "arg_favourite"
    }
}
