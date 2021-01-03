package an.maguste.android.navier.api

import an.maguste.android.navier.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun convertFromJsonToMovie(moviesDto: List<MovieDto>, genres: List<Genre>): List<Movie> =
    withContext(Dispatchers.Default) {
        val genresMap: Map<Int, Genre> = genres.associateBy { it.id }

        moviesDto.map { movieDto: MovieDto ->
            Movie(
                id = movieDto.id,
                title = movieDto.title,
                overview = movieDto.overview,
                poster = movieDto.posterLink,
                backdrop = movieDto.backdropLink,
                ratings = movieDto.ratings,
                adult = movieDto.adult,
                runtime = movieDto.runtime,
                reviews = movieDto.reviews,
                genres = movieDto.genreIds.map {
                    genresMap[it] ?: throw IllegalArgumentException("Genre not found")
                }
            )
        }
    }

suspend fun convertFromJsonToActor(actorsDto: List<ActorDto>): List<Actor> =
    withContext(Dispatchers.Default) {
        actorsDto.map { actorDto ->
            Actor(
                id = actorDto.id,
                name = actorDto.name,
                picture = actorDto.imageLink
            )
        }
    }
