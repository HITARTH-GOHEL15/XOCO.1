package com.app.movieexplorer2.data.remote.models

data class OtpVerifyResponse(
    val access: String,
    val refresh: String? = null,
    val user: UserData,
    val status_code:Int
)
