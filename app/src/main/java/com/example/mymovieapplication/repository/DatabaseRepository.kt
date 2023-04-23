package com.example.mymovieapplication.repository

import com.example.mymovieapplication.db.MoviesDao
import com.example.mymovieapplication.db.MoviesEntity
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val dao: MoviesDao) {
    suspend fun saveMovie(entity: MoviesEntity) = dao.saveMovie(entity)
    fun getAllMovies() = dao.getAllMovies()
    fun sortedASC() = dao.sortedASC()
    fun sortedDESC() = dao.sortedDESC()

}