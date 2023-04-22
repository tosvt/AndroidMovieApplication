package com.example.mymovieapplication.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MoviesEntity::class], version = 1, exportSchema = false)
abstract class MoviesDB : RoomDatabase() {
    abstract fun movieDao() : MoviesDao
}