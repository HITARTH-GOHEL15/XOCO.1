package com.app.movieexplorer2.data.models

data class Movie(
    val id: Long,
    val title: String?,
    val description: String?,
    val rating: Double?,
    val poster: String?,
    val images: List<String>
)