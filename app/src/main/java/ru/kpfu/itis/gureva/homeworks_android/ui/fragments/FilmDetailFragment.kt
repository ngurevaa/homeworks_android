package ru.kpfu.itis.gureva.homeworks_android.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.data.db.AppDatabase
import ru.kpfu.itis.gureva.homeworks_android.data.db.AppSharedPreferences
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentFilmDetailBinding
import ru.kpfu.itis.gureva.homeworks_android.model.RatingModel
import ru.kpfu.itis.gureva.homeworks_android.utils.FilmRepository
import ru.kpfu.itis.gureva.homeworks_android.utils.RatingRepository

class FilmDetailFragment : Fragment(R.layout.fragment_film_detail) {
    private var binding: FragmentFilmDetailBinding? = null
    private var filmRepository: FilmRepository? = null
    private var ratingRepository: RatingRepository? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFilmDetailBinding.bind(view)
        filmRepository = FilmRepository(AppDatabase.getDatabase(requireContext()).filmDao())
        ratingRepository = RatingRepository(AppDatabase.getDatabase(requireContext()).ratingDao())

        val filmId = arguments?.getInt(ARG_FILM_ID) ?: 0
        val userId = AppSharedPreferences.getSP(requireContext()).getInt(AppSharedPreferences.USER_ID, -1)

        binding?.run {
            lifecycleScope.launch {
                val film = filmRepository?.getById(filmId)

                tvName.text = "${getString(R.string.name_hint)}: ${film?.name}"
                tvReleaseYear.text = "${getString(R.string.release_year_hint)}: ${film?.releaseYear}"
                tvDescription.text = "${getString(R.string.description_hint)}: ${film?.description}"
                if (ratingRepository?.exist(userId, filmId)!!) {
                    rbRating.rating = ratingRepository?.getByFilmId(filmId)?.toFloat() ?: 0f
                }
            }

            rbRating.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                    lifecycleScope.launch {
                        ratingRepository?.save(RatingModel(userId, filmId, rating))
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        filmRepository = null
        ratingRepository = null
    }

    companion object {
        private const val ARG_FILM_ID = "arg_film_id"

        fun newInstance(filmId: Int) = FilmDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_FILM_ID, filmId)
            }
        }
    }
}
