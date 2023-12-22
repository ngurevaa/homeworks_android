package ru.kpfu.itis.gureva.homeworks_android.ui.holder

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemFilmBinding
import ru.kpfu.itis.gureva.homeworks_android.model.FilmModel

class FilmViewHolder(
    private val binding: ItemFilmBinding,
    private val onLikeClicked: (FilmModel) -> Unit,
    private val onFilmClicked: (Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {
    private var film: FilmModel? = null

    init {
        binding.run {
            ivLike.setOnClickListener {
                film?.let { film -> onLikeClicked(film) }
            }

            root.setOnClickListener {
                film?.let { film -> onFilmClicked(film.id) }
            }
        }
    }

    fun onBind(film: FilmModel) {
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
