package com.example.mymovieapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovieapplication.db.MoviesEntity
import com.example.mymovieapplication.repository.DatabaseRepository
import com.example.mymovieapplication.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(private val repository: DatabaseRepository):ViewModel(){
    private val _movieList = MutableLiveData<DataStatus<List<MoviesEntity>>>()
    val movieList : LiveData<DataStatus<List<MoviesEntity>>>
    get() = _movieList

    init {
        getAllMovies()
    }

    fun saveMovie(entity: MoviesEntity) = viewModelScope.launch {
        repository.saveMovie(entity)
    }


    fun getAllMovies() = viewModelScope.launch {
        //_movieList.postValue(DataStatus.loading())
        repository.getAllMovies()
            .catch { _movieList.postValue(DataStatus.error(it.message.toString())) }
            .collect{ _movieList.postValue(DataStatus.success(it, it.isEmpty()))}
    }


}