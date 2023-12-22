package ru.kpfu.itis.gureva.homeworks_android.ui.holder

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.databinding.ItemFavouriteFilmBinding
import ru.kpfu.itis.gureva.homeworks_android.model.FilmModel

class FavouriteFilmViewHolder(
    private val binding: ItemFavouriteFilmBinding,
    private val onLikeClicked: (FilmModel) -> Unit,
    private val onFilmClicked: (Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {
    private var film: FilmModel? = null
    init {
        binding.run {
            ivLike.setOnClickListener {
                film?.let { it1 -> onLikeClicked(it1) }
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
