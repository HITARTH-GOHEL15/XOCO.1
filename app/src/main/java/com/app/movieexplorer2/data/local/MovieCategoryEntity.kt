package com.app.movieexplorer2.data.local

import androidx.room.Entity

@Entity(
    tableName = "movie_categories",
    primaryKeys = ["movieId", "category"]
)
data class MovieCategoryEntity(
    val movieId: Long,
    val category: String,
    val position: Int
)