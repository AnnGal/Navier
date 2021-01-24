package an.maguste.android.navier.storage.repository

import an.maguste.android.navier.data.Actor
import an.maguste.android.navier.storage.MoviesDatabase
import an.maguste.android.navier.storage.mappers.ActorMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ActorsRepository {
    suspend fun getAllActorsByMovie(movieId: Int): List<Actor>
    suspend fun rewriteActorsByMovieIntoDB(actors: List<Actor>, movieId: Int)
}

class ActorsRepositoryImpl : ActorsRepository {
    private val moviesDB = MoviesDatabase.instance

    /** request actors by movie id */
    override suspend fun getAllActorsByMovie(movieId: Int): List<Actor> =
        withContext(Dispatchers.IO) {
            moviesDB.actorsDao().getAllByMovieId(movieId).map { ActorMapper.toActorDomain(it) }
        }

    /** del actors and write it again. all by movie id */
    override suspend fun rewriteActorsByMovieIntoDB(actors: List<Actor>, movieId: Int) =
        withContext(Dispatchers.IO) {
            moviesDB.actorsDao().deleteByMovieId(movieId)
            moviesDB.actorsDao().insertAll(actors.map { ActorMapper.toActorEntity(it, movieId) })
        }
}