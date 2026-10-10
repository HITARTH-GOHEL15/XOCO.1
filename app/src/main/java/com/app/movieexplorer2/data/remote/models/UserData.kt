package com.app.movieexplorer2.data.remote.models

import com.google.gson.annotations.SerializedName
import kotlin.uuid.Uuid

data class UserData(
    val id: Int,
    val uuid: String,
    val reference: String,
    val email: String,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("signup_source")
    val signUpSource: String,

    @SerializedName("email_verified")
    val emailVerified: Boolean
)