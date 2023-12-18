package ru.kpfu.itis.gureva.homeworks_android.ui.fragments

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import ru.kpfu.itis.gureva.homeworks_android.R
import ru.kpfu.itis.gureva.homeworks_android.data.db.AppDatabase
import ru.kpfu.itis.gureva.homeworks_android.databinding.FragmentFilmAddingBinding
import ru.kpfu.itis.gureva.homeworks_android.model.FilmModel
import ru.kpfu.itis.gureva.homeworks_android.utils.FilmRepository

class FilmAddingFragment : Fragment(R.layout.fragment_film_adding) {
    private var binding: FragmentFilmAddingBinding? = null
    private var repository: FilmRepository? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFilmAddingBinding.bind(view)
        repository = FilmRepository(AppDatabase.getDatabase(requireContext()).filmDao())

        binding?.run {
            btnSave.setOnClickListener {
                val fieldsValid = checkFieldValidation()

                if (fieldsValid) {
                    val name = etName.text.toString()
                    val year = etReleaseYear.text.toString().toInt()
                    val description = etDescription.text.toString()

                    lifecycleScope.launch {
                        try {
                            repository?.save(FilmModel(0, name, year, description, 0.0, false))

                            AlertDialog.Builder(requireContext())
                                .setTitle(getString(R.string.film_saving_title))
                                .setPositiveButton(getString(R.string.btn_positive)) {_, _ ->}
                                .show()

                        } catch (e: SQLiteConstraintException) {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(getString(R.string.film_saving_error_title))
                                .setMessage(getString(R.string.film_saving_error_message))
                                .setPositiveButton(getString(R.string.btn_positive)) {_, _ ->}
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun checkFieldValidation(): Boolean {
        // убрать строчки
        binding?.run {
            if (etName.text?.isEmpty() == true) {
                etName.error = "Name can not be empty"
                return false
            }
            else if (etReleaseYear.text?.isEmpty() == true) {
                etReleaseYear.error = "Release year can not be empty"
                return false
            }
            else if (etDescription.text?.isEmpty() == true) {
                etDescription.error = "Description can not be empty"
                return false
            }
            else {
                // убрать все ошибки с полей
            }
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        repository = null
    }
}
