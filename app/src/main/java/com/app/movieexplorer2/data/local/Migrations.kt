import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(2, 3) {

    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE movies_new (
                id INTEGER NOT NULL,
                originalLanguage TEXT,
                overview TEXT,
                popularity REAL,
                posterPath TEXT,
                releaseDate TEXT,
                title TEXT,
                voteAverage REAL,
                runtime INTEGER,
                revenue INTEGER,
                status TEXT,
                tagline TEXT,
                PRIMARY KEY(id)
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            INSERT INTO movies_new (
                id,
                overview,
                voteAverage,
                posterPath,
                title
            )
            SELECT
                id,
                description,
                rating,
                poster,
                title
            FROM movies
            """.trimIndent()
        )

        db.execSQL("DROP TABLE movies")

        db.execSQL(
            """
            ALTER TABLE movies_new RENAME TO movies
            """.trimIndent()
        )
    }
}