package com.app.movieexplorer2.di

import com.app.movieexplorer2.data.remote.MovieApiService
import com.app.movieexplorer2.data.repository.MovieRepository
import com.app.movieexplorer2.utils.resource.ResponseHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideMovieRepository(
        api: MovieApiService,
        responseHandler: ResponseHandler
    ): MovieRepository {
        return MovieRepository(api, responseHandler)
    }
}