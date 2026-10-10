package com.app.movieexplorer2.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

//Dao, database, entity, mapper,
// database migration
// SQL query, insert, update, delete query
// database relation(one to one, one to many, many to many), join
// type converter
// primary, foreign key
// suspend vs flow
// transaction


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieCategories(
        categories: List<MovieCategoryEntity>
    )

    @Query("""
        SELECT movies.*
        FROM movies
        INNER JOIN movie_categories
            ON movies.id = movie_categories.movieId
        WHERE movie_categories.category = :category
        ORDER BY movie_categories.position
    """)
    fun getMoviesByCategory(
        category: String
    ): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :movieId LIMIT 1")
    suspend fun getMovieById(movieId: Long): MovieEntity?

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()

    @Query("DELETE FROM movie_categories WHERE category = :category")
    suspend fun deleteCategory(category: String)

    @Transaction
    suspend fun replaceMovieCategory(
        category: String,
        movies: List<MovieEntity>,
        categories: List<MovieCategoryEntity>
    ) {
        deleteCategory(category)
        movies.forEach { movie ->
            saveMovieFromHome(movie)
        }
        insertMovieCategories(categories)
    }

    @Query("""
    UPDATE movies
    SET
        overview = :overview,
        posterPath = :posterPath,
        title = :title,
        voteAverage = :voteAverage
    WHERE id = :id
""")
    suspend fun updateMovieBasicInfo(
        id: Long,
        overview: String?,
        posterPath: String?,
        title: String?,
        voteAverage: Double?
    ): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMoviesIfNotExists(
        movies: List<MovieEntity>
    )

    @Transaction
    suspend fun saveMovieFromHome(movie: MovieEntity) {

        val updatedRows = updateMovieBasicInfo(
            id = movie.id,
            overview = movie.overview,
            posterPath = movie.posterPath,
            title = movie.title,
            voteAverage = movie.voteAverage
        )

        if (updatedRows == 0) {
            insertMoviesIfNotExists(listOf(movie))
        }
    }
}