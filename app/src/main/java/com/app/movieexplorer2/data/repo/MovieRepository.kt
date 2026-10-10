package com.app.movieexplorer2.data.repository

import android.util.Log
import com.app.movieexplorer2.app.MovieCategory
import com.app.movieexplorer2.data.local.MovieCategoryEntity
import com.app.movieexplorer2.data.local.MovieDao
import com.app.movieexplorer2.data.local.mapper.toDomain
import com.app.movieexplorer2.data.local.mapper.toEntity
import com.app.movieexplorer2.data.local.mapper.toMovieDto
import com.app.movieexplorer2.data.mapper.toMovieList
import com.app.movieexplorer2.data.models.Movie
import com.app.movieexplorer2.data.remote.models.GuestSessionResponse
import com.app.movieexplorer2.data.remote.MockApiService
import com.app.movieexplorer2.data.remote.models.MockPostResponse
import com.app.movieexplorer2.data.remote.MovieApiService
import com.app.movieexplorer2.data.remote.models.MovieDto
import com.app.movieexplorer2.data.remote.models.MovieResponseDto
import com.app.movieexplorer2.data.remote.models.RatingRequest
import com.app.movieexplorer2.data.remote.models.RatingResponse
import com.app.movieexplorer2.data.remote.models.UpdatePostRequest
import com.app.movieexplorer2.data.remote.models.UpdateReviewPatchRequest
import com.app.movieexplorer2.utils.ErrorBean
import com.app.movieexplorer2.utils.resource.Resource
import com.app.movieexplorer2.utils.resource.ResponseHandler
import com.app.movieexplorer2.utils.resource.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MovieRepository(
    private val api: MovieApiService,
    private val mockApi: MockApiService,
    private val movieDao: MovieDao,
    private val responseHandler: ResponseHandler
) {

    fun popularMovies(): Flow<Resource<MovieResponseDto>> = flow {
        emit(Resource.loading())

        try {
            val response = responseHandler.handleResponse(
                api.getPopularMovies()
            )

            Log.d("MovieRepository", "API status = ${response.status}")
            Log.d("MovieRepository", "API data = ${response.data}")

            if (response.status == Status.SUCCESS) {

                val movies = response.data?.results
                    .orEmpty()
                    .toMovieList()

                Log.d("MovieRepository", "Movies count = ${movies.size}")

                val movieEntities = movies.map { it.toEntity() }

                Log.d(
                    "MovieRepository",
                    "Movie entities count = ${movieEntities.size}"
                )

                val categoryEntities = movieEntities.mapIndexed { index, movie ->
                    MovieCategoryEntity(
                        movieId = movie.id,
                        category = MovieCategory.POPULAR,
                        position = index
                    )
                }

                Log.d(
                    "MovieRepository",
                    "Category entities count = ${categoryEntities.size}"
                )

                movieDao.replaceMovieCategory(
                    category = MovieCategory.POPULAR,
                    movies = movieEntities,
                    categories = categoryEntities
                )
            }

            emit(response)

        } catch (e: Exception) {

            Log.e(
                "MovieRepository",
                "popularMovies failed",
                e
            )

            emit(responseHandler.handleException(e))
        }
    }.catch {
        Log.e(
            "MovieRepository",
            "Flow failed",
            it
        )

        emit(responseHandler.handleException(it))
    }


    fun topRatedMovies(): Flow<Resource<MovieResponseDto>> = flow {
        emit(Resource.loading())

        try {
            val response = responseHandler.handleResponse(
                api.getTopRatedMovie()
            )

            if (response.status == Status.SUCCESS) {

                val movies = response.data?.results
                    .orEmpty()
                    .toMovieList()

                val movieEntities = movies.map { it.toEntity() }

                val categoryEntities = movieEntities.mapIndexed { index, movie ->
                    MovieCategoryEntity(
                        movieId = movie.id,
                        category = MovieCategory.TOP_RATED,
                        position = index
                    )
                }

                movieDao.replaceMovieCategory(
                    category = MovieCategory.TOP_RATED,
                    movies = movieEntities,
                    categories = categoryEntities
                )
            }

            emit(response)

        } catch (e: Exception) {
            emit(responseHandler.handleException(e))
        }
    }.catch {
        emit(responseHandler.handleException(it))
    }


    fun upcomingMovies(): Flow<Resource<MovieResponseDto>> = flow {
        emit(Resource.loading())

        try {
            val response = responseHandler.handleResponse(
                api.getUpcomingMovie()
            )

            if (response.status == Status.SUCCESS) {

                val movies = response.data?.results
                    .orEmpty()
                    .toMovieList()

                val movieEntities = movies.map { it.toEntity() }

                val categoryEntities = movieEntities.mapIndexed { index, movie ->
                    MovieCategoryEntity(
                        movieId = movie.id,
                        category = MovieCategory.UPCOMING,
                        position = index
                    )
                }

                movieDao.replaceMovieCategory(
                    category = MovieCategory.UPCOMING,
                    movies = movieEntities,
                    categories = categoryEntities
                )
            }

            emit(response)

        } catch (e: Exception) {
            emit(responseHandler.handleException(e))
        }
    }.catch {
        emit(responseHandler.handleException(it))
    }


    fun movieDetails(
        movieId: Int
    ): Flow<Resource<MovieDto>> = flow {

        emit(Resource.loading())

        try {

            val response =
                responseHandler.handleResponse(
                    api.getMovieDetails(movieId)
                )

            if(response.status == Status.SUCCESS) {

                response.data?.let { movieDto ->

                    movieDao.insertMovies(
                        listOf(
                            movieDto.toEntity()
                        )
                    )
                }
            }

            emit(response)

        } catch (e: Exception) {

            emit(
                responseHandler.handleException(e)
            )
        }

    }.catch {

        emit(
            responseHandler.handleException(it)
        )
    }


    fun createGuestSession(): Flow<Resource<GuestSessionResponse>> = flow {

        emit(Resource.loading())

        try {

            val response =
                responseHandler.handleResponse(
                    api.createGuestSession()
                )

            emit(response)

        } catch (e: Exception) {

            emit(
                responseHandler.handleException(e)
            )
        }

    }.catch {

        emit(
            responseHandler.handleException(it)
        )
    }


    fun rateMovie(
        movieId: Int,
        guestSession: String,
        rating: Double
    ): Flow<Resource<RatingResponse>> = flow {

        emit(Resource.loading())

        try {

            val response =
                responseHandler.handleResponse(
                    api.rateMovie(
                        movieId,
                        guestSession,
                        request = RatingRequest(
                            value = rating
                        )
                    )
                )

            emit(response)

        } catch (e: Exception) {

            emit(
                responseHandler.handleException(e)
            )
        }

    }.catch {

        emit(
            responseHandler.handleException(it)
        )
    }


    fun putMovie(
        id: Int,
        request: UpdatePostRequest
    ): Flow<Resource<MockPostResponse>> = flow {

        emit(Resource.loading())

        try {

            val response =
                responseHandler.handleResponse(
                    mockApi.putMovie(
                        id,
                        request = request
                    )
                )

            emit(response)

        } catch (e: Exception) {

            emit(
                responseHandler.handleException(e)
            )
        }

    }.catch {

        emit(
            responseHandler.handleException(it)
        )
    }


    fun patchMovie(
        id: Int,
        request: UpdateReviewPatchRequest
    ): Flow<Resource<MockPostResponse>> = flow {

        emit(Resource.loading())

        try {

            val response =
                responseHandler.handleResponse(
                    mockApi.patchMovie(
                        id = id,
                        request = request
                    )
                )

            emit(response)

        } catch (e: Exception) {

            emit(
                responseHandler.handleException(e)
            )
        }

    }.catch {

        emit(
            responseHandler.handleException(it)
        )
    }

    fun observeMoviesFromDb(
        category: String
    ): Flow<List<Movie>> {
        return movieDao.getMoviesByCategory(category)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    fun observeTopRatedMovies(): Flow<List<Movie>> {
        return movieDao.getMoviesByCategory(MovieCategory.TOP_RATED)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    fun observeUpcomingMovies(): Flow<List<Movie>> {
        return movieDao.getMoviesByCategory(MovieCategory.UPCOMING)
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    fun movieDetailsFromDb(
        movieId: Int
    ): Flow<Resource<MovieDto>> = flow {

        emit(Resource.loading())

        try {

            val movieEntity =
                movieDao.getMovieById(movieId.toLong())

            Log.d(
                "DETAIL_FLOW",
                "Room entity = $movieEntity"
            )

            if (movieEntity != null) {

                emit(
                    Resource.success(
                        movieEntity.toMovieDto()
                    )
                )

            } else {

                emit(
                    Resource.error(
                        ErrorBean(
                            errorMessage = "Movie data is not available offline"
                        )
                    )
                )
            }

        } catch (e: Exception) {

            emit(
                responseHandler.handleException(e)
            )
        }

    }.catch {

        emit(
            responseHandler.handleException(it)
        )
    }

}