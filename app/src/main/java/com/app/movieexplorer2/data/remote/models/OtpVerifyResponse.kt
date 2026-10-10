package com.app.movieexplorer2.data.remote.models

data class OtpVerifyResponse(
    val access: String,
    val user: List<UserData>,
    val status_code:Int
)
