package an.maguste.android.navier.storage

import an.maguste.android.navier.storage.entitys.MovieEntity
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Update
    suspend fun update(movie: MovieEntity)

    @Delete
    suspend fun delete(movie: MovieEntity)

    @Query("DELETE FROM MOVIE")
    suspend fun deleteAll()

    @Query("SELECT * FROM MOVIE ORDER BY _id ASC")
    suspend fun getAll(): List<MovieEntity>

    @Query("SELECT * FROM MOVIE WHERE _id = :movieId LIMIT 1")
    suspend fun getMovie(movieId: Long): MovieEntity?

}
