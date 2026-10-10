package com.app.movieexplorer2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        MovieEntity::class,
        MovieCategoryEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}