package com.app.movieexplorer2.ui

import com.app.movieexplorer2.data.models.Movie
import com.app.movieexplorer2.utils.ErrorBean

data class MovieUiState(
    val popularMovies: List<Movie> = emptyList(),
    val topMovies: List<Movie> = emptyList(),
    val upcomingMovies: List<Movie> = emptyList(),

    val popularLoaded: Boolean = false,
    val topLoaded: Boolean = false,
    val upcomingLoaded: Boolean = false,

    val isLoading: Boolean = false,
    val error: ErrorBean? = null
)