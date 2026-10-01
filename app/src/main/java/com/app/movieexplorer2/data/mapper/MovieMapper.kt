package com.app.movieexplorer2.data.mapper

import com.app.movieexplorer2.data.models.Movie
import com.app.movieexplorer2.data.remote.MovieDto
import com.app.movieexplorer2.data.remote.MovieResponseDto
import kotlin.collections.mapNotNull

fun List<MovieDto?>.toMovieList(): List<Movie> {

    return map { movie ->

        Movie(
            id = movie?.id?.toLong(),
            title = movie?.title,
            description = movie?.overview,
            rating = movie?.voteAverage,
            poster = movie?.posterPath.orEmpty(),
            images = emptyList()
        )
    }

}