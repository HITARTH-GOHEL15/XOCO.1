package com.app.movieexplorer2.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.movieexplorer2.data.remote.models.UpdatePostRequest
import com.app.movieexplorer2.data.remote.models.UpdateReviewPatchRequest
import com.app.movieexplorer2.data.repository.MovieRepository
import com.app.movieexplorer2.ui.uiState.GuestSessionUiState
import com.app.movieexplorer2.ui.uiState.MovieDetailUiState
import com.app.movieexplorer2.ui.uiState.PatchUiState
import com.app.movieexplorer2.ui.uiState.PutMovieUiState
import com.app.movieexplorer2.ui.uiState.RateMovieUiState
import com.app.movieexplorer2.utils.ErrorBean
import com.app.movieexplorer2.utils.NetworkMonitor
import com.app.movieexplorer2.utils.resource.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val networkMonitor: NetworkMonitor
): ViewModel() {

    private val _uiState =
        MutableStateFlow(MovieDetailUiState())

    val uiState: StateFlow<MovieDetailUiState> =
        _uiState.asStateFlow()

    private val _rateState =
        MutableStateFlow(RateMovieUiState())

    val rateState: StateFlow<RateMovieUiState> =
        _rateState.asStateFlow()

    private val _guestSessionState =
        MutableStateFlow(GuestSessionUiState())

    val guestSessionState: StateFlow<GuestSessionUiState> =
        _guestSessionState.asStateFlow()

    private var guestSessionId: String? = null

    private val _putState =
        MutableStateFlow(PutMovieUiState())

    val putState: StateFlow<PutMovieUiState> =
              _putState.asStateFlow()

    private val _patchState =
        MutableStateFlow(PatchUiState())

    val patchUiState: StateFlow<PatchUiState> =
                 _patchState.asStateFlow()

    fun getMovieDetails(movieId: Int) {

        val isOnline = networkMonitor.isOnline.value

        Log.d(
            "DETAIL_FLOW",
            "movieId=$movieId, isOnline=$isOnline"
        )

        if (isOnline) {
            Log.d("DETAIL_FLOW", "Using API")
            // ONLINE → API
            getMovieDetailsFromAPI(movieId)

        } else {
            Log.d("DETAIL_FLOW", "Using Room")
            // OFFLINE → ROOM
            getMovieDetailsFromDb(movieId)
        }
    }

    fun getMovieDetailsFromAPI(movieId: Int) {

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

    fun rateMovie(
        movieId: Int,
        rating: Double
    ) {

        val sessionId = guestSessionId

        if (sessionId.isNullOrEmpty()) {

            Log.e(
                "GUEST_SESSION",
                "Cannot rate movie: guest session is not ready"
            )

            _rateState.value =
                RateMovieUiState(
                    error = ErrorBean(
                        errorMessage = "Rating session is not ready"
                    ),
                    isLoading = false
                )

            return
        }

        repository.rateMovie(
            movieId = movieId,
            guestSession = sessionId,
            rating = rating
        )
            .onEach { result ->

                when (result.status) {

                    Status.LOADING -> {

                        _rateState.value =
                            RateMovieUiState(
                                isLoading = true
                            )
                    }

                    Status.SUCCESS -> {

                        _rateState.value =
                            RateMovieUiState(
                                success = true,
                                isLoading = false
                            )
                    }

                    Status.ERROR -> {

                        _rateState.value =
                            RateMovieUiState(
                                error = result.error,
                                isLoading = false
                            )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun createGuestSession() {

        repository.createGuestSession()
            .onEach { result ->

                when (result.status) {

                    Status.LOADING -> {

                        _guestSessionState.value =
                            GuestSessionUiState(
                                isLoading = true
                            )
                    }

                    Status.SUCCESS -> {

                        guestSessionId =
                            result.data?.guest_session_id

                        Log.d(
                            "GUEST_SESSION",
                            "ViewModel session = $guestSessionId"
                        )

                        _guestSessionState.value =
                            GuestSessionUiState(
                                isLoading = false,
                                success = true,
                                guestSessionId = guestSessionId
                            )
                    }

                    Status.ERROR -> {

                        _guestSessionState.value =
                            GuestSessionUiState(
                                isLoading = false,
                                error = result.error
                            )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun putMovie(
        id: Int,
        request: UpdatePostRequest
    ) {
        repository.putMovie(
            id = id,
            request = request
        )
            .onEach { result ->

                when (result.status) {

                    Status.LOADING -> {

                        _putState.value =
                            PutMovieUiState(
                                isLoading = true
                            )
                    }

                    Status.SUCCESS -> {

                        _putState.value =
                            PutMovieUiState(
                                success = true,
                                isLoading = false,
                                data = result.data
                            )
                    }

                    Status.ERROR -> {

                        _putState.value =
                            PutMovieUiState(
                                error = result.error,
                                isLoading = false
                            )
                    }
                }
            }
            .launchIn(viewModelScope)
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

    fun patchMovie() {

        viewModelScope.launch {

            val request = UpdateReviewPatchRequest(
                body = "This review was updated using PATCH"
            )

            repository.patchMovie(
                id = 1,
                request = request
            ).collect { result ->

                when (result.status) {

                    Status.LOADING -> {
                        Log.d(
                            "PATCH_MOVIE",
                            "Updating review..."
                        )
                    }

                    Status.SUCCESS -> {
                        Log.d(
                            "PATCH_MOVIE",
                            "Patched review = ${result.data}"
                        )
                    }

                    Status.ERROR -> {
                        Log.e(
                            "PATCH_MOVIE",
                            "Patch failed = ${result.error}"
                        )
                    }
                }
            }
        }
    }

    private fun getMovieDetailsFromDb(movieId: Int) {

        Log.d(
            "DETAIL_FLOW",
            "Calling movieDetailsFromDb($movieId)"
        )

        repository.movieDetailsFromDb(movieId)
            .onEach { state ->

                Log.d(
                    "DETAIL_FLOW",
                    "DB state=${state.status}, data=${state.data}, error=${state.error}"
                )

                when (state.status) {

                    Status.LOADING -> {

                        _uiState.value =
                            MovieDetailUiState(
                                isLoading = true
                            )
                    }

                    Status.SUCCESS -> {

                        _uiState.value =
                            _uiState.value.copy(
                                movie = state.data,
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
            }
            .launchIn(viewModelScope)
    }




}