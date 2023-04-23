package com.example.mymovieapplication.ui.addmovie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.mymovieapplication.R
import com.example.mymovieapplication.databinding.FragmentAddMovieBinding
import com.example.mymovieapplication.db.MoviesEntity
import com.example.mymovieapplication.viewmodel.DatabaseViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddMovieFragment : Fragment() {

    @Inject
    lateinit var entity: MoviesEntity

    private val viewModel : DatabaseViewModel by viewModels()

    private var movieId = 0
    private var title = ""
    private var year = ""
    private var country = ""
    private var rate = ""
    private var duration = ""


    private lateinit var binding: FragmentAddMovieBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            imgClose.setOnClickListener {
                requireActivity().onBackPressed()
            }

            btnSave.setOnClickListener {
                title = edtTitle.text.toString()
                year = edtYearOfIssue.text.toString()
                country = edtCountry.text.toString()
                rate = edtRaiting.text.toString()
                duration = edtDuration.text.toString()

                if(title.isEmpty() || year.isEmpty() || country.isEmpty() || rate.isEmpty() || duration.isEmpty()){
                    Snackbar.make(it, "Поля обязательны для заполнения", Snackbar.LENGTH_SHORT).show()
                } else {
                    entity.id = movieId
                    entity.title = title
                    entity.country = country
                    entity.raiting = rate
                    entity.yearofissue = year
                    entity.duration = duration

                    viewModel.saveMovie(entity)

                    edtTitle.setText("")
                    edtDuration.setText("")
                    edtCountry.setText("")
                    edtRaiting.setText("")
                    edtYearOfIssue.setText("")

                    requireActivity().onBackPressed()

                }

            }
        }
    }

}