package an.maguste.android.navier.storage.entitys

import an.maguste.android.navier.storage.DbContract
import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = DbContract.ActorContract.TABLE_NAME,
    foreignKeys = [ForeignKey(
        entity = MovieEntity::class,
        parentColumns = arrayOf(DbContract.MovieContract.COLUMN_NAME_ID),
        childColumns = arrayOf(DbContract.ActorContract.COLUMN_NAME_MOVIE_ID),
        onDelete = CASCADE
    )],
    indices = [Index(value = [DbContract.ActorContract.COLUMN_NAME_MOVIE_ID])]
)
class ActorEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DbContract.ActorContract.COLUMN_NAME_ID)
    val id: Long?,
    @ColumnInfo(name = DbContract.ActorContract.COLUMN_NAME_ACTOR_ID)
    val actorId: Int,
    val name: String,
    @ColumnInfo(name = DbContract.ActorContract.COLUMN_NAME_IMAGE)
    val image: String?,
    @ColumnInfo(name = DbContract.ActorContract.COLUMN_NAME_MOVIE_ID)
    val movie: Long
)