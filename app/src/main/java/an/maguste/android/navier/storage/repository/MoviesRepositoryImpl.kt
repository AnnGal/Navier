package an.maguste.android.navier.storage.repository

import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.storage.MoviesDatabase
import an.maguste.android.navier.storage.mappers.MovieMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface MoviesRepository {
    suspend fun getAllMovies(): List<Movie>
    suspend fun writeMovieIntoDB(movie: Movie)
    suspend fun rewriteMoviesListIntoDB(movies: List<Movie>)
    suspend fun getMovieById(id: Long): Movie?
}

class MoviesRepositoryImpl : MoviesRepository {
    private val moviesDB = MoviesDatabase.instance

    /** add movies data into db*/
    override suspend fun writeMovieIntoDB(movie: Movie) = withContext(Dispatchers.IO) {
        moviesDB.moviesDao().insert(MovieMapper.toMovieEntity(movie))
    }

    /** del movies and write new movies data set again */
    override suspend fun rewriteMoviesListIntoDB(movies: List<Movie>) =
        withContext(Dispatchers.IO) {
            moviesDB.moviesDao().deleteAll()
            moviesDB.moviesDao().insertAll(movies.map { MovieMapper.toMovieEntity(it) })
        }

    /** request movies from db */
    override suspend fun getAllMovies(): List<Movie> = withContext(Dispatchers.IO) {
        moviesDB.moviesDao().getAll().map { MovieMapper.toMovieDomain(it) }
    }


    /** get concrete movie */
    override suspend fun getMovieById(id: Long): Movie? = withContext(Dispatchers.IO) {
        val movie = moviesDB.moviesDao().getMovie(id)

        if (movie != null){
            MovieMapper.toMovieDomain(movie)
        } else null
    }
}