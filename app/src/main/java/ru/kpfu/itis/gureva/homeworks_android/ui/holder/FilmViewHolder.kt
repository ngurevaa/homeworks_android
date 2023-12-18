package ru.kpfu.itis.gureva.homeworks_android.ui.holder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemFilmBinding
import ru.kpfu.itis.gureva.homeworks_android.model.FilmModel

class FilmViewHolder(
    private val binding: ItemFilmBinding,
    private val onLikeClicked: (FilmModel) -> Unit
): RecyclerView.ViewHolder(binding.root) {
    private var film: FilmModel? = null

    init {
        binding.ivLike.setOnClickListener {
            film?.let { it1 -> onLikeClicked(it1) }
        }
    }

    fun onBind(film: FilmModel) {
        Log.e("fjvhjfbhf", film.isFavourite.toString())
        this.film = film
        binding.run {
            tvName.text = film.name
            tvRating.text = film.rating.toString()

            if (film.isFavourite) {
                changeLikeStatus(true)
            }
        }
    }

    fun changeLikeStatus(like: Boolean) {
        val likeDrawable = if (like) R.drawable.full_heart else R.drawable.empty_heart
        binding.ivLike.setImageResource(likeDrawable)
    }
}
