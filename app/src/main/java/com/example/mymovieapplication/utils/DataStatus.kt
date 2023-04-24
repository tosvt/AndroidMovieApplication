package com.example.mymovieapplication.utils
//Отвечает за определение текущего состояния данных, когда мы их получаем
//также, этот класс можно использовать при работе, например, с API,
//когда нужно проверить состояние данных
data class DataStatus<out T> (
    val status : Status,
    val data : T?=null,
    val message : String?= null,
    val isEmpty : Boolean?=false
) {
    enum class Status {
        LOADING , SUCCESS , ERROR
    }
    companion object{
        fun <T> loading() : DataStatus<T>{
            return DataStatus(Status.LOADING)
        }
        fun <T> success(data : T? , isEmpty: Boolean?) : DataStatus<T>{
            return DataStatus(Status.SUCCESS, data, isEmpty=isEmpty)
        }
        fun <T> error(error : String): DataStatus<T> {
            return DataStatus(Status.ERROR, message = error)
        }
    }
}
