package com.app.movieexplorer2.ui.uiState

import com.app.movieexplorer2.utils.ErrorBean

data class GuestSessionUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val guestSessionId: String? = null,
    val error: ErrorBean? = null
)