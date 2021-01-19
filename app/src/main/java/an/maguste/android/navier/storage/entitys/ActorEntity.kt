package an.maguste.android.navier.storage.entitys

import an.maguste.android.navier.storage.DbContract
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = DbContract.ActorContract.TABLE_NAME,
foreignKeys = [ForeignKey(
    entity = MovieEntity::class,
    parentColumns = arrayOf(DbContract.MovieContract.COLUMN_NAME_ID),
    childColumns = arrayOf(DbContract.ActorContract.COLUMN_NAME_MOVIE_ID),
    onDelete = CASCADE
)])
class ActorEntity(
    @PrimaryKey
    @ColumnInfo(name = DbContract.ActorContract.COLUMN_NAME_ID)
    val id: Long,
    val name: String,
    @ColumnInfo(name = DbContract.ActorContract.COLUMN_NAME_IMAGE)
    val image: String?,
    @ColumnInfo(name = DbContract.ActorContract.COLUMN_NAME_MOVIE_ID)
    val movie: Long
)