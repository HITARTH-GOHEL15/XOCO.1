package com.app.movieexplorer2.ui.uiState

import com.app.movieexplorer2.data.remote.models.LoginResponse
import com.app.movieexplorer2.utils.ErrorBean
import okhttp3.Response

data class LoginUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: ErrorBean? = null,
    val data: LoginResponse? = null
)