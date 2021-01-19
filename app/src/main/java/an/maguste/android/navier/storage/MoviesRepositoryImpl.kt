package an.maguste.android.navier.storage

import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.storage.entitys.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


interface MoviesRepository {
    suspend fun getAllMovies(): List<Movie>
    suspend fun writeMovieIntoDB(movie: Movie)
    suspend fun rewriteMoviesListIntoDB(movies: List<Movie>)
    //suspend fun addNewAndGetUpdated(): List<Location>
    //suspend fun deleteByIdAndGetUpdated(id: Long): List<Location>
}

class MoviesRepositoryImpl : MoviesRepository {
    private val moviesDB = MoviesDatabase.instance

    override suspend fun writeMovieIntoDB(movie: Movie) = withContext(Dispatchers.IO) {
        moviesDB.moviesDao().insert(toEntity(movie))
    }

    override suspend fun rewriteMoviesListIntoDB(movies: List<Movie>) {
        moviesDB.moviesDao().deleteAll()
        moviesDB.moviesDao().insertAll(movies.map { toEntity(it) })
    }

    override suspend fun getAllMovies(): List<Movie> = withContext(Dispatchers.IO) {
        moviesDB.moviesDao().getAll().map { toDomain(it) }
    }

    private fun toEntity(movieDomain: Movie) = MovieEntity(
        id = movieDomain.id.toLong(),
        title = movieDomain.title,
        overview = movieDomain.overview,
        poster = movieDomain.poster,
        backdrop = movieDomain.backdrop,
        ratings = movieDomain.ratings,
        adult = movieDomain.adult,
        runtime = movieDomain.runtime,
        reviews = movieDomain.reviews,
        genres = movieDomain.genres.joinToString(","),
        like = movieDomain.like
    )

    private fun toDomain(movieEntity: MovieEntity) = Movie(
        id = movieEntity.id.toInt(),
        title = movieEntity.title,
        overview = movieEntity.overview,
        poster = movieEntity.poster,
        backdrop = movieEntity.backdrop,
        ratings = movieEntity.ratings,
        adult = movieEntity.adult,
        runtime = movieEntity.runtime,
        reviews = movieEntity.reviews,
        genres = movieEntity.genres.split(",").map { it.trim() },
        like = movieEntity.like
    )
}