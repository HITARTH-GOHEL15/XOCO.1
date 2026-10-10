package com.app.movieexplorer2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.movieexplorer2.app.MovieCategory
import com.app.movieexplorer2.data.mapper.toMovieList
import com.app.movieexplorer2.data.repository.MovieRepository
import com.app.movieexplorer2.ui.uiState.MovieUiState
import com.app.movieexplorer2.utils.NetworkMonitor
import com.app.movieexplorer2.utils.resource.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(MovieUiState())

    val uiState: StateFlow<MovieUiState> =
        _uiState.asStateFlow()


    private var popularDbJob: Job? = null
    private var topRatedDbJob: Job? = null
    private var upcomingDbJob: Job? = null
    fun getMovies() {

        _uiState.value = MovieUiState(isLoading = true)

        networkMonitor.isOnline
            .onEach { isOnline ->

                if (isOnline) {
                    // Network is available
                    getPopularMovies()
                    getTopRatedMovies()
                    getUpcomingMovies()

                } else {
                    // Network is unavailable
                    observePopularMovies()
                    observeTopRatedMovies()
                    observeUpcomingMovies()
                }
            }
            .launchIn(viewModelScope)
    }


    private fun getPopularMovies() {

        repository.popularMovies()
            .onEach { state ->

                when (state.status) {

                    Status.LOADING -> {
                    }

                    Status.SUCCESS -> {

                        val movies =
                            state.data
                                ?.results
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
                                popularLoaded = true,
                                error = state.error
                            )

                        updateLoadingState()
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
                    }

                    Status.SUCCESS -> {

                        val movies =
                            state.data
                                ?.results
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
                                topLoaded = true,
                                error = state.error
                            )

                        updateLoadingState()
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
                    }

                    Status.SUCCESS -> {

                        val movies =
                            state.data
                                ?.results
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
                                upcomingLoaded = true,
                                error = state.error
                            )

                        updateLoadingState()
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

    private fun observePopularMovies() {
        popularDbJob?.cancel()

        popularDbJob = repository.observeMoviesFromDb(MovieCategory.POPULAR)
            .onEach { movies ->
                _uiState.value = _uiState.value.copy(
                    popularMovies = movies,
                    popularLoaded = true,
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }

    private fun observeTopRatedMovies() {
        topRatedDbJob?.cancel()

        topRatedDbJob = repository.observeTopRatedMovies()
            .onEach { movies ->
                _uiState.value = _uiState.value.copy(
                    topMovies = movies,
                    topLoaded = true
                )
                updateLoadingState()
            }
            .launchIn(viewModelScope)
    }

    private fun observeUpcomingMovies() {
        upcomingDbJob?.cancel()

        upcomingDbJob = repository.observeUpcomingMovies()
            .onEach { movies ->
                _uiState.value = _uiState.value.copy(
                    upcomingMovies = movies,
                    upcomingLoaded = true
                )
                updateLoadingState()
            }
            .launchIn(viewModelScope)
    }
}