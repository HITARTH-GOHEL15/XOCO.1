package com.app.movieexplorer2.data.mapper

import com.app.movieexplorer2.data.models.Movie
import com.app.movieexplorer2.data.remote.models.MovieDto

fun List<MovieDto?>.toMovieList(): List<Movie> {

    return map { movie ->

        Movie(
            id = movie?.id?.toLong() ?: 0,
            title = movie?.title,
            description = movie?.overview,
            rating = movie?.voteAverage,
            poster = movie?.posterPath.orEmpty(),
            images = emptyList()
        )
    }

}