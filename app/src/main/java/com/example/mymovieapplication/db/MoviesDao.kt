package com.example.mymovieapplication.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mymovieapplication.utils.Constants.MOVIES_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovie(moviesEntity: MoviesEntity)

    @Query("SELECT * FROM $MOVIES_TABLE")
    fun getAllMovies() : Flow<MutableList<MoviesEntity>>

    @Query("SELECT * FROM $MOVIES_TABLE ORDER BY title ASC ")
    fun sortedASC() : Flow<MutableList<MoviesEntity>>

    @Query("SELECT * FROM $MOVIES_TABLE ORDER BY title DESC ")
    fun sortedDESC() : Flow<MutableList<MoviesEntity>>

    @Query("SELECT * FROM $MOVIES_TABLE WHERE title LIKE '%' || :title || '%' ")
    fun searchMovie(title:String) : Flow<MutableList<MoviesEntity>>

    @Update
    suspend fun updateMovie(entity: MoviesEntity)

    @Delete
    suspend fun deleteMovie(entity: MoviesEntity)

    @Query("SELECT * FROM $MOVIES_TABLE WHERE id == :id")
    fun getMovie(id: Int): Flow<MoviesEntity>
}