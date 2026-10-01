package com.app.movieexplorer2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.movieexplorer2.data.mapper.toMovieList
import com.app.movieexplorer2.data.repository.MovieRepository
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
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(MovieUiState())

    val uiState: StateFlow<MovieUiState> =
        _uiState.asStateFlow()


    fun getMovies() {

        _uiState.value = MovieUiState(
            isLoading = true
        )

        getPopularMovies()
        getTopRatedMovies()
        getUpcomingMovies()
    }


    private fun getPopularMovies() {

        repository.popularMovies()
            .onEach { state ->

                when (state.status) {

                    Status.LOADING -> {
                        // Loading is already handled by getMovies()
                    }

                    Status.SUCCESS -> {

                        val movies =
                            state.data?.results
                                .orEmpty()
                                .toMovieList()

                        _uiState.value =
                            _uiState.value.copy(
                                popularMovies = movies,
                                popularLoaded = true
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

            }
            .launchIn(viewModelScope)
    }


    private fun getTopRatedMovies() {

        repository.topRatedMovies()
            .onEach { state ->

                when (state.status) {

                    Status.LOADING -> {
                        // Loading is already handled by getMovies()
                    }

                    Status.SUCCESS -> {

                        val movies =
                            state.data?.results
                                .orEmpty()
                                .toMovieList()

                        _uiState.value =
                            _uiState.value.copy(
                                topMovies = movies,
                                topLoaded = true
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

            }
            .launchIn(viewModelScope)
    }


    private fun getUpcomingMovies() {

        repository.upcomingMovies()
            .onEach { state ->

                when (state.status) {

                    Status.LOADING -> {
                        // Loading is already handled by getMovies()
                    }

                    Status.SUCCESS -> {

                        val movies =
                            state.data?.results
                                .orEmpty()
                                .toMovieList()

                        _uiState.value =
                            _uiState.value.copy(
                                upcomingMovies = movies,
                                upcomingLoaded = true
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

            }
            .launchIn(viewModelScope)
    }


    private fun updateLoadingState() {

        val state = _uiState.value

        if (
            state.popularLoaded &&
            state.topLoaded &&
            state.upcomingLoaded
        ) {
            _uiState.value =
                state.copy(
                    isLoading = false
                )
        }
    }
}