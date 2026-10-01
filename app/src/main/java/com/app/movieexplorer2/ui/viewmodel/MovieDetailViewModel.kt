package com.app.movieexplorer2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.movieexplorer2.data.repository.MovieRepository
import com.app.movieexplorer2.ui.MovieDetailUiState
import com.app.movieexplorer2.ui.MovieUiState
import com.app.movieexplorer2.utils.resource.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {

    private val _uiState =
        MutableStateFlow(MovieDetailUiState())

    val uiState: StateFlow<MovieDetailUiState> =
        _uiState.asStateFlow()

    fun getMovieDetails(movieId: Int) {

        repository.movieDetails(movieId)
            .onEach { state ->
                 when (state.status) {
                     Status.LOADING -> {
                         _uiState.value = MovieDetailUiState(
                             isLoading = true
                         )
                     }

                     Status.SUCCESS -> {
                         val movie =
                             state.data

                         _uiState.value =
                             _uiState.value.copy(
                                 movie = movie,
                                 movieLoaded = true
                             )

                         updateLoadingState()
                     }
                     Status.ERROR -> {
                         _uiState.value =
                             _uiState.value.copy(
                                 isLoading = false,
                                 error = state.error
                             )
                     }
                 }
            }.launchIn(viewModelScope)
    }

    private fun updateLoadingState() {

        val state = _uiState.value

        if(
            state.movieLoaded
        ) {
            _uiState.value =
                state.copy(
                    isLoading = false
                )
        }
    }
}