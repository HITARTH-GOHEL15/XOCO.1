package com.app.movieexplorer2.data.remote.models

data class OtpVerifyRequest(
    val email: String,
    val code: String
)
