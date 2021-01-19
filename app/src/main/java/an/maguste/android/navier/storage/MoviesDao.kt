package an.maguste.android.navier.storage

import an.maguste.android.navier.storage.entitys.MovieEntity
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    @Update
    suspend fun update(movie: MovieEntity)

    @Delete
    suspend fun delete(movie: MovieEntity)

    @Query("SELECT * FROM MOVIE ORDER BY _id ASC")
    suspend fun getAll(): List<MovieEntity>
}
