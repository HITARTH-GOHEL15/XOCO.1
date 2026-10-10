package com.app.movieexplorer2.di

import MIGRATION_2_3
import android.content.Context
import androidx.room.Room
import com.app.movieexplorer2.data.local.MovieDao
import com.app.movieexplorer2.data.local.MovieDatabase
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
    fun provideMovieDatabase(
        @ApplicationContext context: Context
    ): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie_database"
        )
            .addMigrations(MIGRATION_2_3)
            .build()
    }

    @Provides
    fun provideMovieDao(
        database: MovieDatabase
    ): MovieDao {
        return database.movieDao()
    }
}