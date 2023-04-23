package com.example.mymovieapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovieapplication.R
import com.example.mymovieapplication.adapter.MoviesAdapter
import com.example.mymovieapplication.databinding.ActivityMainBinding
import com.example.mymovieapplication.ui.addmovie.AddMovieFragment
import com.example.mymovieapplication.utils.DataStatus
import com.example.mymovieapplication.viewmodel.DatabaseViewModel
import com.example.mymovieapplication.viewmodel.isVisible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    private val viewModel: DatabaseViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    var selectedItem = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            //setSupportActionBar(mainToolbar)
            viewModel.getAllMovies()
            viewModel.movieList.observe(this@MainActivity) {
                when (it.status) {
                    DataStatus.Status.LOADING -> {
                        progressBar.isVisible(true, rvMovies)
                        empty.isVisible(false, rvMovies)
                    }
                    DataStatus.Status.SUCCESS -> {
                        it.isEmpty?.let {
                            showEmpty(it)
                        }
                        progressBar.isVisible(false, rvMovies)
                        moviesAdapter.differ.submitList(it.data)
                        rvMovies.apply {
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            adapter = moviesAdapter
                            //внедрение контента
                        }
                    }
                    DataStatus.Status.ERROR -> {
                        progressBar.isVisible(false, rvMovies)
                        Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    fun showEmpty(isShow: Boolean) {
        binding.apply {
            if (isShow) {
                empty.isVisible(true, list)
            } else {
                empty.isVisible(false, list)
            }
        }
    }

}