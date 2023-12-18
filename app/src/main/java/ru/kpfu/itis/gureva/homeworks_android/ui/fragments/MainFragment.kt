package ru.kpfu.itis.gureva.homeworks_android.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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
    private var favouriteFilms: List<FilmModel>? = null
    private var allFilms: MutableList<FilmModel>? = null
    private var userId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        favouriteRepository = FavouriteRepository(AppDatabase.getDatabase(requireContext()).favouriteDao(),
            AppDatabase.getDatabase(requireContext()).filmDao())
        allFilmsRepository = FilmRepository(AppDatabase.getDatabase(requireContext()).filmDao())

        filmAdapter = FilmAdapter(::onLikeClicked)

        userId = arguments?.getInt(ARG_USER_ID) ?: 0
        binding?.run {
            btnAdd.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(fragmentContainerId, FilmAddingFragment())
                    .addToBackStack(null)
                    .commit()
            }

            lifecycleScope.launch {
//                outputOfFavouriteFilms(userId)
                outputOfAllFilms()
            }
        }
    }

//    private suspend fun outputOfFavouriteFilms(userId: Int) {
//        favouriteFilms = favouriteRepository?.getAll(userId)
//        binding?.run {
//            if (favouriteFilms?.size == 0) {
//                tvFavouriteFilms.visibility = View.GONE
//            } else {
//                favouriteFilmAdapter = FavouriteFilmAdapter(::onLikeClicked).also {
//                    it.submitList(favouriteFilms)
//                }
//            }
//        }
//    }

    private suspend fun outputOfAllFilms() {
        allFilms = allFilmsRepository?.getAll() as MutableList<FilmModel>?
        binding?.run {
            if (allFilms?.size == 0) {
                tvFilms.text = getString(R.string.no_available_films)
            } else {
                val films = ArrayList<FilmModel>()
                films.addAll(allFilms!!)
                rvFilms.adapter = filmAdapter
                println(films)
                filmAdapter?.submitList(films)
            }
        }
    }

    private fun onLikeClicked(filmModel: FilmModel) {
        lifecycleScope.launch {
            if (filmModel.isFavourite) {
                userId?.let { favouriteRepository?.delete(it, filmModel.id) }
            }
            else {
                userId?.let { favouriteRepository?.save(it, filmModel.id) }
            }
        }
        val index = allFilms?.indexOf(filmModel)

        val list = ArrayList<FilmModel>()
        allFilms?.let { list.addAll(it) }
        println(filmAdapter?.currentList)
        if (index != null) {
            allFilms?.removeAt(index)
            allFilms?.add(index, filmModel.copy())
            allFilms?.get(index)?.isFavourite = !filmModel.isFavourite
        }
        filmAdapter?.submitList(allFilms?.toList())
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
