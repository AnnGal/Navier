package an.maguste.android.navier.storage.mappers

import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.storage.entitys.MovieEntity

object MovieMapper {

    internal fun toMovieEntity(movieDomain: Movie) = MovieEntity(
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

    internal fun toMovieDomain(movieEntity: MovieEntity) = Movie(
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