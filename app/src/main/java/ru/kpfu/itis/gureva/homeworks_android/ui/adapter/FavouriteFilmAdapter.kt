package ru.kpfu.itis.gureva.homeworks_android.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemFavouriteFilmBinding
import ru.kpfu.itis.gureva.homeworks_android.model.FilmModel
import ru.kpfu.itis.gureva.homeworks_android.model.UserModel
import ru.kpfu.itis.gureva.homeworks_android.ui.holder.FavouriteFilmViewHolder

class FavouriteFilmAdapter(
    private val onLikeClicked: (FilmModel) -> Unit
): ListAdapter<FilmModel, FavouriteFilmViewHolder>(object :
    DiffUtil.ItemCallback<FilmModel>() {
    override fun areItemsTheSame(oldItem: FilmModel, newItem: FilmModel): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: FilmModel, newItem: FilmModel): Boolean = oldItem == newItem

    override fun getChangePayload(oldItem: FilmModel, newItem: FilmModel): Any? {
        val bundle = Bundle()
        if (oldItem.isFavourite != newItem.isFavourite) {
            bundle.putBoolean("", newItem.isFavourite)
        }
        return if (bundle.isEmpty) super.getChangePayload(oldItem, newItem) else bundle
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteFilmViewHolder {
        return FavouriteFilmViewHolder(ItemFavouriteFilmBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ), onLikeClicked = onLikeClicked)
    }

    override fun onBindViewHolder(holder: FavouriteFilmViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onBindViewHolder(holder: FavouriteFilmViewHolder, position: Int, payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            (payloads.first() as Boolean).let {
                holder.changeLikeStatus(it)
            }
        }
        else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
}
