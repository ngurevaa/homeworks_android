package ru.kpfu.itis.gureva.homeworks_android.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.data.db.AppDatabase
import ru.kpfu.itis.gureva.homeworks_android.data.db.AppSharedPreferences
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentMainBinding
import ru.kpfu.itis.gureva.homeworks_android.model.FilmModel
import ru.kpfu.itis.gureva.homeworks_android.ui.adapter.FavouriteFilmAdapter
import ru.kpfu.itis.gureva.homeworks_android.ui.adapter.FilmAdapter
import ru.kpfu.itis.gureva.homeworks_android.utils.FavouriteRepository
import ru.kpfu.itis.gureva.homeworks_android.utils.FilmRepository
import ru.kpfu.itis.gureva.homeworks_android.utils.RatingRepository

class MainFragment : Fragment(R.layout.fragment_main) {
    private val fragmentContainerId: Int = R.id.main_container
    private var binding: FragmentMainBinding? = null
    private var favouriteRepository: FavouriteRepository? = null
    private var allFilmsRepository: FilmRepository? = null
    private var ratingRepository: RatingRepository? = null
    private var favouriteFilmAdapter: FavouriteFilmAdapter? = null
    private var filmAdapter: FilmAdapter? = null
    private var favouriteFilms: MutableList<FilmModel>? = null
    private var allFilms: MutableList<FilmModel>? = null
    private var userId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        favouriteRepository = FavouriteRepository(AppDatabase.getDatabase(requireContext()).favouriteDao(),
            AppDatabase.getDatabase(requireContext()).filmDao())
        allFilmsRepository = FilmRepository(AppDatabase.getDatabase(requireContext()).filmDao())
        ratingRepository = RatingRepository(AppDatabase.getDatabase(requireContext()).ratingDao())

        filmAdapter = FilmAdapter(::onLikeClicked, ::onFilmClicked)
        initItemTouch()
        favouriteFilmAdapter = FavouriteFilmAdapter(::onLikeClicked, ::onFilmClicked)

        val i = AppSharedPreferences.getSP(requireContext()).getInt(AppSharedPreferences.USER_ID, -1)
        if (i != -1) userId = i

        binding?.run {
            btnAdd.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(fragmentContainerId, FilmAddingFragment())
                    .addToBackStack(null)
                    .commit()
            }

            btnProfile.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(fragmentContainerId, ProfileFragment())
                    .addToBackStack(null)
                    .commit()
            }

            lifecycleScope.launch {
                outputOfFavouriteFilms()
                outputOfAllFilms()
            }
        }
    }

    private suspend fun outputOfFavouriteFilms() {
        favouriteFilms = userId?.let { favouriteRepository?.getAll(it) as MutableList<FilmModel>? }
        checkFavouriteFilmCount()
        binding?.rvFavouriteFilms?.adapter = favouriteFilmAdapter
        favouriteFilmAdapter?.submitList(favouriteFilms?.toList())
    }

    private suspend fun outputOfAllFilms() {
        allFilms = allFilmsRepository?.getAll() as MutableList<FilmModel>?
        allFilms?.forEach {film ->
            val status = userId?.let { favouriteRepository?.checkFilmStatus(it, film.id) }
            if (status != null) {
                film.isFavourite = status
            }
            film.rating = ratingRepository?.getByFilmId(film.id) ?: 0.0
        }
        allFilms?.sortByDescending { it.releaseYear }
        checkAllFilmCount()

        binding?.rvFilms?.adapter = filmAdapter
        filmAdapter?.submitList(allFilms?.toList())
    }

    private fun onLikeClicked(filmModel: FilmModel) {
        val index = allFilms?.indexOfFirst { it.id == filmModel.id }
        if (index != null) {
            if (allFilms?.get(index)?.isFavourite == true) {
                lifecycleScope.launch { userId?.let { favouriteRepository?.delete(it, filmModel.id) } }
                favouriteFilms?.removeIf { it.id == filmModel.id }
            }
            else {
                lifecycleScope.launch { userId?.let { favouriteRepository?.save(it, filmModel.id) } }
                favouriteFilms?.add(filmModel.copy().apply { isFavourite = true })
            }
            checkFavouriteFilmCount()

            val value = allFilms?.get(index)?.copy()
            allFilms?.removeAt(index)
            value?.apply { isFavourite = !isFavourite }?.let { allFilms?.add(index, it) }
        }
        filmAdapter?.submitList(allFilms?.toList())
        favouriteFilmAdapter?.submitList(favouriteFilms?.toList())
    }

    private fun onFilmClicked(filmId: Int) {
        parentFragmentManager.beginTransaction()
            .replace(fragmentContainerId, FilmDetailFragment.newInstance(filmId))
            .addToBackStack(null)
            .commit()
    }

    private fun initItemTouch() {
        val itemTouch = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteFromAllFilms(viewHolder.adapterPosition)
            }
        }
        ItemTouchHelper(itemTouch).attachToRecyclerView(binding?.rvFilms)
    }

    private fun deleteFromAllFilms(position: Int) {
        val filmId = allFilms?.get(position)?.id
        lifecycleScope.launch {
            filmId?.let { allFilmsRepository?.delete(it) }
        }
        allFilms?.removeAt(position)
        favouriteFilms?.removeIf { it.id == filmId }

        checkAllFilmCount()
        checkFavouriteFilmCount()

        filmAdapter?.submitList(allFilms?.toList())
        favouriteFilmAdapter?.submitList(favouriteFilms?.toList())
    }

    private fun checkFavouriteFilmCount() {
        binding?.run {
            if (favouriteFilms?.size == 0) {
                tvFavouriteFilms.visibility = View.GONE
            }
            else {
                tvFavouriteFilms.visibility = View.VISIBLE
            }
        }
    }

    private fun checkAllFilmCount() {
        binding?.run {
            if (allFilms?.size == 0) {
                tvFilms.text = getString(R.string.no_available_films)
            }
            else {
                tvFilms.text = getString(R.string.all_films)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        favouriteRepository = null
        allFilmsRepository = null
        ratingRepository = null
    }
}
