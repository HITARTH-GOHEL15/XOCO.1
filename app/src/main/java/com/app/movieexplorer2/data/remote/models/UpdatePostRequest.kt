package com.app.movieexplorer2.data.remote.models

data class UpdatePostRequest(
    val userId: Int,
    val title: String,
    val body: String
)