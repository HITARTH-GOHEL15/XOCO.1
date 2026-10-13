package com.app.movieexplorer2.ui.uiState

import com.app.movieexplorer2.data.remote.models.OtpVerifyRequest
import com.app.movieexplorer2.data.remote.models.OtpVerifyResponse
import com.app.movieexplorer2.data.remote.models.RegisterResponse
import com.app.movieexplorer2.utils.ErrorBean

data class VerifyUiState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: ErrorBean? = null,
    val data: OtpVerifyResponse? = null
)