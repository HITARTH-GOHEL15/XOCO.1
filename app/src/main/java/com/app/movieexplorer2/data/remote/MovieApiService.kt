package com.app.movieexplorer2.data.remote

import com.app.movieexplorer2.data.remote.models.GuestSessionResponse
import com.app.movieexplorer2.data.remote.models.LoginRequest
import com.app.movieexplorer2.data.remote.models.MovieDto
import com.app.movieexplorer2.data.remote.models.MovieResponseDto
import com.app.movieexplorer2.data.remote.models.OtpVerifyRequest
import com.app.movieexplorer2.data.remote.models.RatingRequest
import com.app.movieexplorer2.data.remote.models.RatingResponse
import com.app.xoco2.data.models.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponseDto>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovie(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponseDto>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovie(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MovieResponseDto>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movie_id: Int
    ): Response<MovieDto>

    @POST("authentication/guest_session/new")
    suspend fun createGuestSession():
            Response<GuestSessionResponse>


    @POST("movie/{movie_id}/rating")
    suspend fun rateMovie(
        @Path("movie_id") movieId: Int,
        @Query("guest_session_id") guestSessionId: String,
        @Body request: RatingRequest
    ): Response<RatingResponse>

    @GET("find/{external_id}")
    suspend fun searchMoviesById(

    )


}