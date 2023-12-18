package ru.kpfu.itis.gureva.homeworks_android.ui.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemFilmBinding
import ru.kpfu.itis.gureva.homeworks_android.model.FilmModel
import ru.kpfu.itis.gureva.homeworks_android.ui.holder.FavouriteFilmViewHolder
import ru.kpfu.itis.gureva.homeworks_android.ui.holder.FilmViewHolder

class FilmAdapter(
    private val onLikeClicked: (FilmModel) -> Unit
): ListAdapter<FilmModel, FilmViewHolder>(object : DiffUtil.ItemCallback<FilmModel>() {
    // потом создать отдельный класс тк копия кода
    override fun areItemsTheSame(oldItem: FilmModel, newItem: FilmModel): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: FilmModel, newItem: FilmModel): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: FilmModel, newItem: FilmModel): Any? {
        val bundle = Bundle()
        if (oldItem.isFavourite != newItem.isFavourite) {
            bundle.putBoolean("", newItem.isFavourite)
        }
        return if (bundle.isEmpty) super.getChangePayload(oldItem, newItem) else bundle
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(ItemFilmBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ), onLikeClicked)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int, payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            (payloads.first() as Bundle).getBoolean("").let {
                holder.changeLikeStatus(it)
            }
        }
        else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
}
