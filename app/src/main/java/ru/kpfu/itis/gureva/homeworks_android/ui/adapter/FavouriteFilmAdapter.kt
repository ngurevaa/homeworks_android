package ru.kpfu.itis.gureva.homeworks_android.ui.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemFavouriteFilmBinding
import ru.kpfu.itis.gureva.homeworks_android.model.FilmModel
import ru.kpfu.itis.gureva.homeworks_android.model.UserModel
import ru.kpfu.itis.gureva.homeworks_android.ui.holder.FavouriteFilmViewHolder

class FavouriteFilmAdapter(
    private val onLikeClicked: (FilmModel) -> Unit,
    private val onFilmClicked: (Int) -> Unit
): ListAdapter<FilmModel, FavouriteFilmViewHolder>(ItemCallbackImpl()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteFilmViewHolder {
        return FavouriteFilmViewHolder(ItemFavouriteFilmBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        onLikeClicked,
        onFilmClicked)
    }

    override fun onBindViewHolder(holder: FavouriteFilmViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onBindViewHolder(holder: FavouriteFilmViewHolder, position: Int, payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            (payloads.first() as Bundle).getBoolean(ItemCallbackImpl.ARG_FAVOURITE).let {
                holder.changeLikeStatus(it)
            }
        }
        else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
}
