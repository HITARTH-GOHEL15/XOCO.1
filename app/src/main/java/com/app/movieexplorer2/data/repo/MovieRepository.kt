package com.app.movieexplorer2.data.repository

import com.app.movieexplorer2.data.remote.MovieApiService
import com.app.movieexplorer2.data.remote.MovieDto
import com.app.movieexplorer2.data.remote.MovieResponseDto
import com.app.movieexplorer2.utils.resource.Resource
import com.app.movieexplorer2.utils.resource.ResponseHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class MovieRepository(
    val api: MovieApiService,
    private val responseHandler: ResponseHandler
) {

    fun popularMovies(): Flow<Resource<MovieResponseDto>> = flow {

        emit(Resource.loading())

        try {

            val response =
                responseHandler.handleResponse(
                    api.getPopularMovies()
                )

            emit(response)

        } catch (e: Exception) {

            e.printStackTrace()

            emit(
                responseHandler.handleException(e)
            )
        }

    }.catch {

        it.printStackTrace()

        emit(
            responseHandler.handleException(it)
        )
    }


    fun topRatedMovies(): Flow<Resource<MovieResponseDto>> = flow {

        emit(Resource.loading())

        try {

            val response =
                responseHandler.handleResponse(
                    api.getTopRatedMovie()
                )

            emit(response)

        } catch (e: Exception) {

            e.printStackTrace()

            emit(
                responseHandler.handleException(e)
            )
        }

    }.catch {

        it.printStackTrace()

        emit(
            responseHandler.handleException(it)
        )
    }


    fun upcomingMovies(): Flow<Resource<MovieResponseDto>> = flow {

        emit(Resource.loading())

        try {

            val response =
                responseHandler.handleResponse(
                    api.getUpcomingMovie()
                )

            emit(response)

        } catch (e: Exception) {

            e.printStackTrace()

            emit(
                responseHandler.handleException(e)
            )
        }

    }.catch {

        it.printStackTrace()

        emit(
            responseHandler.handleException(it)
        )
    }

    fun movieDetails(movieId: Int): Flow<Resource<MovieDto>> = flow {
        emit(Resource.loading())

        try {
            val response =
                responseHandler.handleResponse(
                    api.getMovieDetails(
                        movieId
                    )
                )

            emit(response)
        } catch (e: Exception) {
            e.printStackTrace()

            emit(
                responseHandler.handleException(e)
            )
        }
    }.catch {
        it.printStackTrace()

        emit(
            responseHandler.handleException(it)
        )
    }
}