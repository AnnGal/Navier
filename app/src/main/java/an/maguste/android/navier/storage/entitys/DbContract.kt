package an.maguste.android.navier.storage.entitys

import android.provider.BaseColumns

object DbContract {
    const val DATABASE_NAME = "Movies.db"

    /*
    * NOTE: In this case will be no many_to_many table between Movie and Actors,
    * this info will be stored in Actors table.
    * */

    object MovieContract {
        const val TABLE_NAME = "movie"

        const val COLUMN_NAME_ID = BaseColumns._ID
    }


    object ActorContract {
        const val TABLE_NAME = "actor"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_IMAGE = "image_url"
        const val COLUMN_NAME_MOVIE_ID = "movie_id"
    }
}