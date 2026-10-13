package com.app.movieexplorer2.data.remote.models

data class LoginResponse(
    val access: String,
    val refresh: String? = null,
    val user: UserData,
    val status_code:Int
)