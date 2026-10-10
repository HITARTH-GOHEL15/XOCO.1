package com.app.xoco2.data.models

data class RegisterRequest(
    val email: String,
    val password: String,
    val fullName: String,
    val marketingMessages: Boolean? = false
)