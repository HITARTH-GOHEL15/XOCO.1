package com.app.movieexplorer2.data.remote.models

data class LoginResponse(
    val access: String,
    val user: UserData,
    val status_code:Int
)