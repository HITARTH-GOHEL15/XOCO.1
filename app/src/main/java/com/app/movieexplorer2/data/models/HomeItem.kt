package com.app.movieexplorer2.data.models

sealed interface HomeItem {

    data class Header(
        val movies: List<Movie>
    ) : HomeItem

    data class MovieSection(
        val id: String,
        val title: String,
        val movies: List<Movie>
    ) : HomeItem
}