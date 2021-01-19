package an.maguste.android.navier.storage

import an.maguste.android.navier.App
import an.maguste.android.navier.storage.entitys.MovieEntity
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    companion object {
        val instance: MoviesDatabase by lazy {
            Room.databaseBuilder(
                App.context(),
                MoviesDatabase::class.java,
                "Movies_db"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
}

/**
 * Get news article DAO
 */
/*abstract fun newsArticlesDao(): NewsArticlesDao

companion object {

    private const val databaseName = "news-db"

    fun buildDefault(context: Context) =
        Room.databaseBuilder(context, NewsDatabase::class.java, databaseName)
            .addMigrations(*NewsDatabaseMigration.allMigrations)
            .build()

    @VisibleForTesting
    fun buildTest(context: Context) =
        Room.inMemoryDatabaseBuilder(context, NewsDatabase::class.java)
            .build()
}*/

/*
@Database(entities = [Game::class], version = 1, exportSchema = false)
abstract class GameDatabase : RoomDatabase(){
    abstract val gameDao: GameDAO

    companion object {
        @Volatile
        private var INSTANCE: GameDatabase? = null

        fun getInstance(context: Context): GameDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GameDatabase::class.java,
                        "Favorite games database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}*/
