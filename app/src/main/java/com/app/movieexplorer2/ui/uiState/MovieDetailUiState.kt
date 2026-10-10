package com.app.movieexplorer2.ui.uiState

import com.app.movieexplorer2.data.remote.models.MovieDto
import com.app.movieexplorer2.utils.ErrorBean

data class MovieDetailUiState(
    val movie: MovieDto? = null,
    val isLoading: Boolean = false,
    val error: ErrorBean? = null,

    val movieLoaded: Boolean = false
)