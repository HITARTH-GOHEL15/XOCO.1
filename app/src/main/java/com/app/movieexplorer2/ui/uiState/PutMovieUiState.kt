package com.app.movieexplorer2.ui.uiState

import com.app.movieexplorer2.data.remote.models.MockPostResponse
import com.app.movieexplorer2.utils.ErrorBean

data class PutMovieUiState(

    val isLoading: Boolean = false,
    val success: Boolean = false,
    val data: MockPostResponse? = null,
    val error: ErrorBean? = null
)
