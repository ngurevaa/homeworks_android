package ru.kpfu.itis.gureva.homeworks_android.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemFilmBinding
import ru.kpfu.itis.gureva.homeworks_android.model.FilmModel
import ru.kpfu.itis.gureva.homeworks_android.ui.holder.FilmViewHolder

class FilmAdapter(
    private val onLikeClicked: (FilmModel) -> Unit,
    private val onFilmClicked: (Int) -> Unit
): ListAdapter<FilmModel, FilmViewHolder>(ItemCallbackImpl()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder(ItemFilmBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        onLikeClicked,
        onFilmClicked)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int, payloads: MutableList<Any>
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
