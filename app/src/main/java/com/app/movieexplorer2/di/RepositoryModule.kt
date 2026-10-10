package com.app.movieexplorer2.di

import com.app.movieexplorer2.data.local.MovieDao
import com.app.movieexplorer2.data.remote.AuthApiService
import com.app.movieexplorer2.data.remote.MockApiService
import com.app.movieexplorer2.data.remote.MovieApiService
import com.app.movieexplorer2.data.repo.AuthRepository
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
        mockApi: MockApiService,
        movieDao: MovieDao,
        responseHandler: ResponseHandler
    ): MovieRepository {
        return MovieRepository(api, mockApi, movieDao,responseHandler)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(
        api: AuthApiService,
        responseHandler: ResponseHandler
    ) : AuthRepository {
        return AuthRepository(api, responseHandler)
    }
}