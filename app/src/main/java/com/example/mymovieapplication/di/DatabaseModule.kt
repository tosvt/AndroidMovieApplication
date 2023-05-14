package com.example.mymovieapplication.di

import android.content.Context
import androidx.room.Room
import com.example.mymovieapplication.db.MoviesDB
import com.example.mymovieapplication.db.MoviesEntity
import com.example.mymovieapplication.utils.Constants.MOVIES_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, MoviesDB::class.java, MOVIES_DATABASE
    )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(db: MoviesDB) = db.movieDao()

    @Provides
    fun provideEntity() = MoviesEntity()

}