package an.maguste.android.navier.storage

import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.storage.entitys.MovieEntity


interface MoviesRepository {
    suspend fun getAllMovies(): List<Movie>

    //suspend fun addNewAndGetUpdated(): List<Location>
    //suspend fun deleteByIdAndGetUpdated(id: Long): List<Location>
}

class Repository: MoviesRepository {
    private val moviesDB = MoviesDatabase.instance

   /* private fun writeIntoDB(movie: Movie){
        MoviesDatabase.instance.moviesDao().insert()
    }*/

    override suspend fun getAllMovies(): List<Movie> {
        TODO("Not yet implemented")
    }
/*

    private fun toEntity(movieDomain: Movie) = MovieEntity(
        title = location.title,
        lat = location.latitude,
        lon = location.longitude
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
            reviews = movieEntity.reviews
            genres = movieEntity.
            like =
    )  */
}