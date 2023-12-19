package ru.kpfu.itis.gureva.homeworks_android.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.data.db.AppDatabase
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentMainBinding
import ru.kpfu.itis.gureva.homeworks_android.model.FilmModel
import ru.kpfu.itis.gureva.homeworks_android.ui.adapter.FavouriteFilmAdapter
import ru.kpfu.itis.gureva.homeworks_android.ui.adapter.FilmAdapter
import ru.kpfu.itis.gureva.homeworks_android.utils.FavouriteRepository
import ru.kpfu.itis.gureva.homeworks_android.utils.FilmRepository

class MainFragment : Fragment(R.layout.fragment_main) {
    private val fragmentContainerId: Int = R.id.main_container
    private var binding: FragmentMainBinding? = null
    private var favouriteRepository: FavouriteRepository? = null
    private var allFilmsRepository: FilmRepository? = null
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

        filmAdapter = FilmAdapter(::onLikeClicked)
        favouriteFilmAdapter = FavouriteFilmAdapter(::onLikeClickedInFavourites)

        userId = arguments?.getInt(ARG_USER_ID) ?: 0
        binding?.run {
            btnAdd.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(fragmentContainerId, FilmAddingFragment())
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
        binding?.run {
            if (favouriteFilms?.size == 0) {
                tvFavouriteFilms.visibility = View.GONE
            }
            rvFavouriteFilms.adapter = favouriteFilmAdapter
            favouriteFilmAdapter?.submitList(favouriteFilms?.toList())
        }
    }

    private suspend fun outputOfAllFilms() {
        allFilms = allFilmsRepository?.getAll() as MutableList<FilmModel>?
        allFilms?.forEach {film ->
            val status = userId?.let { favouriteRepository?.checkFilmStatus(it, film.id) }
            if (status != null) {
                film.isFavourite = status
            }
        }
        binding?.run {
            if (allFilms?.size == 0) {
                tvFilms.text = getString(R.string.no_available_films)
            } else {
                rvFilms.adapter = filmAdapter
                filmAdapter?.submitList(allFilms?.toList())
            }
        }
    }

    private fun onLikeClicked(filmModel: FilmModel) {
//        Log.e("current", favouriteFilmAdapter?.currentList.toString())
//        Log.e("films", favouriteFilms.toString())
        val index = allFilms?.indexOfFirst { it.id == filmModel.id }
        if (index != null) {
            lifecycleScope.launch {
                if (allFilms?.get(index)?.isFavourite == true) {
                    userId?.let { favouriteRepository?.delete(it, filmModel.id) }
                }
                else {
                    userId?.let { favouriteRepository?.save(it, filmModel.id) }
                }
            }

            val value = allFilms?.get(index)?.copy()
            allFilms?.removeAt(index)
            value?.apply { isFavourite = !isFavourite }?.let { allFilms?.add(index, it) }

            if (value?.isFavourite == false) {
                favouriteFilms?.removeIf { it.id == filmModel.id }
            }
            else {
                value?.let { favouriteFilms?.add(it) }
            }
        }
        filmAdapter?.submitList(allFilms?.toList())
        favouriteFilmAdapter?.submitList(favouriteFilms?.toList())
    }


    private fun onLikeClickedInFavourites(filmModel: FilmModel) {
//        val index = favouriteFilms?.indexOfFirst { it.id == filmModel.id }
//        if (index != null) {
//            lifecycleScope.launch {
//                if (favouriteFilms?.get(index)?.isFavourite == true) {
//                    userId?.let { favouriteRepository?.delete(it, filmModel.id) }
//                }
//                else {
//                    userId?.let { favouriteRepository?.save(it, filmModel.id) }
//                }
//            }
//
//            val value = favouriteFilms?.get(index)?.copy()
//            favouriteFilms?.removeAt(index)
//            value?.apply { isFavourite = !isFavourite }?.let { favouriteFilms?.add(index, it) }
//        }
//        favouriteFilmAdapter?.submitList(favouriteFilms?.toList())
    }
    private fun click(films: MutableList<FilmModel>, filmModel: FilmModel, adapter: ListAdapter<FilmModel, RecyclerView.ViewHolder>) {
        val index = films.indexOfFirst { it.id == filmModel.id }
        lifecycleScope.launch {
            if (films.get(index).isFavourite) {
                userId?.let { favouriteRepository?.delete(it, filmModel.id) }
            }
            else {
                userId?.let { favouriteRepository?.save(it, filmModel.id) }
            }
        }

        val value = films.get(index).copy()
        films.removeAt(index)
        value.apply { isFavourite = !isFavourite }.let { films.add(index, it) }
        adapter.submitList(films.toList())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        favouriteRepository = null
        allFilmsRepository = null
    }

    companion object {
        const val ARG_USER_ID = "arg_user_id"

        fun newInstance(userId: Int) = MainFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_USER_ID, userId)
            }
        }
    }
}
