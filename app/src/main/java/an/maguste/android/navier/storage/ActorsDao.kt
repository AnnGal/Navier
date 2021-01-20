package an.maguste.android.navier.storage

import an.maguste.android.navier.storage.entitys.ActorEntity
import androidx.room.*

@Dao
interface ActorsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<ActorEntity>)

    @Query("DELETE FROM ACTOR WHERE movie_id == :movieId")
    suspend fun deleteByMovieId(movieId: Int)

    @Query("SELECT * FROM ACTOR WHERE movie_id == :movieId ORDER BY _id ASC")
    suspend fun getAllByMovieId(movieId: Int): List<ActorEntity>
}
