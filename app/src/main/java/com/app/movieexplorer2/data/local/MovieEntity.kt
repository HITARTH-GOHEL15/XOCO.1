package com.app.movieexplorer2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(

    @PrimaryKey
    val id: Long,

    val originalLanguage: String?,
    val overview: String?,
    val popularity: Double?,
    val posterPath: String?,
    val releaseDate: String?,
    val title: String?,
    val voteAverage: Double?,

    val runtime: Int?,
    val revenue: Long?,
    val status: String?,
    val tagline: String?
)