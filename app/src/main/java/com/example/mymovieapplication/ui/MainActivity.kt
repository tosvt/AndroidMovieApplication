package com.example.mymovieapplication.ui

import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapplication.R
import com.example.mymovieapplication.adapter.MoviesAdapter
import com.example.mymovieapplication.databinding.ActivityMainBinding
import com.example.mymovieapplication.ui.addmovie.AddMovieFragment
import com.example.mymovieapplication.utils.Constants.BUNDLE_ID
import com.example.mymovieapplication.utils.DataStatus
import com.example.mymovieapplication.viewmodel.DatabaseViewModel
import com.example.mymovieapplication.viewmodel.isVisible
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    private val viewModel: DatabaseViewModel by viewModels()

    private var selectedItem = 0

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

            mainToolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.add -> {
                        //btnShowFragment.visibility = View.GONE
                        // Создание экземпляра фрагмента
                        val myFragment = AddMovieFragment()
                        // Получение менеджера фрагментов
                        val fragmentManager = supportFragmentManager
                        // Создание транзакции фрагментов
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        // Замена текущего фрагмента на новый фрагмент
                        fragmentTransaction.replace(R.id.fragment_container, myFragment)
                        // Добавление транзакции в стек возврата
                        fragmentTransaction.addToBackStack(null)
                        // Применение транзакции
                        fragmentTransaction.commit()

                        return@setOnMenuItemClickListener true
                    }
                    R.id.baselineSort -> {
                        filter()
                        return@setOnMenuItemClickListener true
                    }

                    else -> {
                        return@setOnMenuItemClickListener false
                    }
                }
            }

            val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val movie = moviesAdapter.differ.currentList[position]
                    when(direction){
                        ItemTouchHelper.LEFT -> {
                            viewModel.deleteMovie(movie)
                            Snackbar.make(binding.root, "Фильм удален!", Snackbar.LENGTH_LONG).apply {
                                setAction("Отменить"){
                                    viewModel.saveMovie(false, movie)
                                }
                            }.show()
                        }
                        ItemTouchHelper.RIGHT -> {
                            val addMovieFragment = AddMovieFragment()
                            val bundle = Bundle()
                            bundle.putInt(BUNDLE_ID, movie.id)
                            addMovieFragment.arguments = bundle
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, addMovieFragment, "AddFragmentTag")
                                .addToBackStack(null)
                                .commit()

                        }
                    }
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftLabel("Удалить")
                        .addSwipeLeftBackgroundColor(Color.RED)
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                        .setSwipeLeftLabelColor(Color.WHITE)
                        .setSwipeLeftActionIconTint(Color.WHITE)
                        .addSwipeRightLabel("Изменить")
                        .addSwipeRightBackgroundColor(Color.GREEN)
                        .setSwipeRightLabelColor(Color.WHITE)
                        .setSwipeRightActionIconTint(Color.WHITE)
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                        .create()
                        .decorate()
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeCallBack)
            itemTouchHelper.attachToRecyclerView(rvMovies)
        }
    }

    private fun showEmpty(isShow: Boolean) {
        binding.apply {
            if (isShow) {
                empty.isVisible(true, list)
            } else {
                empty.isVisible(false, list)
            }
        }
    }

    private fun filter() {
        val builder = AlertDialog.Builder(this)
        val sortItem = arrayOf("Новые (по умолчанию)", "Названию : A-Z", "Названию : Z-A")
        builder.setSingleChoiceItems(sortItem, selectedItem) { dialog, item ->
            when (item) {
                0 -> viewModel.getAllMovies()
                1 -> viewModel.sortedASC()
                2 -> viewModel.sortedDESC()
            }
            selectedItem = item
            dialog.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
}