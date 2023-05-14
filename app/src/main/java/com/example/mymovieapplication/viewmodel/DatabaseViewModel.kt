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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(private val repository: DatabaseRepository):ViewModel(){
    private val _movieList = MutableLiveData<DataStatus<List<MoviesEntity>>>() //изменяемый список фильмов
    val movieList : LiveData<DataStatus<List<MoviesEntity>>> // данные в режиме реального времени
        get() = _movieList //получаем список

    private val _movieDetails = MutableLiveData<DataStatus<MoviesEntity>>()
    val movieDetails : LiveData<DataStatus<MoviesEntity>>
        get() = _movieDetails

    init {
        getAllMovies()
    }

    fun saveMovie(isEdit: Boolean, entity: MoviesEntity) = viewModelScope.launch {
        if(isEdit){
            repository.updateMovie(entity)
        } else {
            repository.saveMovie(entity)
        }
    }

    fun deleteMovie(entity: MoviesEntity) = viewModelScope.launch {
        repository.deleteMovie(entity)
    }

    fun getAllMovies() = viewModelScope.launch {
        _movieList.postValue(DataStatus.loading())
        repository.getAllMovies()
            .catch { _movieList.postValue(DataStatus.error(it.message.toString())) }
            .collect{ _movieList.postValue(DataStatus.success(it, it.isEmpty()))}
    }

    fun sortedASC() = viewModelScope.launch {
        _movieList.postValue(DataStatus.loading())
        repository.sortedASC()
            .catch { _movieList.postValue(DataStatus.error(it.message.toString())) }
            .collect{ _movieList.postValue(DataStatus.success(it, it.isEmpty()))}
    }

    fun sortedDESC() = viewModelScope.launch {
        _movieList.postValue(DataStatus.loading())
        repository.sortedDESC()
            .catch { _movieList.postValue(DataStatus.error(it.message.toString())) }
            .collect{ _movieList.postValue(DataStatus.success(it, it.isEmpty()))}
    }

    fun getMovie(id: Int) = viewModelScope.launch {
        repository.getMovie(id).collect{
            _movieDetails.postValue(DataStatus.success(it, false))
        }
    }
}