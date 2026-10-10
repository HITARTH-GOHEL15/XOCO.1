package com.app.movieexplorer2.data.local.mapper

import com.app.movieexplorer2.data.local.MovieEntity
import com.app.movieexplorer2.data.models.Movie
import com.app.movieexplorer2.data.remote.models.MovieDto


fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title.orEmpty(),
        description = overview.orEmpty(),
        rating = voteAverage ?: 0.0,
        poster = posterPath.orEmpty(),
        images = emptyList()
    )
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,

        originalLanguage = null,
        overview = description,
        popularity = null,
        posterPath = poster,
        releaseDate = null,
        title = title,
        voteAverage = rating,

        runtime = null,
        revenue = null,
        status = null,
        tagline = null
    )
}

fun MovieDto.toEntity(): MovieEntity {
    return MovieEntity(
        id = requireNotNull(id).toLong(),

        originalLanguage = originalLanguage,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,

        runtime = runtime,
        revenue = revenue?.toLong(),
        status = status,
        tagline = tagline
    )
}

fun MovieEntity.toMovieDto(): MovieDto {
    return MovieDto(
        id = id.toInt(),
        originalLanguage = originalLanguage,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
        runtime = runtime,
        revenue = revenue?.toInt(),
        status = status,
        tagline = tagline
    )
}