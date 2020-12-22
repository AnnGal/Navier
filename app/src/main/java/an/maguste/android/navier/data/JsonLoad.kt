package an.maguste.android.navier.data

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val jsonFormat = Json { ignoreUnknownKeys = true }

@Serializable
private class JsonGenre(val id: Int, val name: String)

@Serializable
private class JsonActor(
        val id: Int,
        val name: String,
        @SerialName("profile_path")
        val profilePicture: String
)

@Serializable
private class JsonMovie(
        val id: Int,
        val title: String,
        @SerialName("poster_path")
        val posterPicture: String,
        @SerialName("backdrop_path")
        val backdropPicture: String,
        val runtime: Int,
        @SerialName("genre_ids")
        val genreIds: List<Int>,
        val actors: List<Int>,
        @SerialName("vote_average")
        val ratings: Float,
        val overview: String,
        val adult: Boolean,
        @SerialName("vote_count")
        val reviews: Int
)

private suspend fun loadGenres(context: Context,
                               dispatcher: CoroutineDispatcher): List<Genre> = withContext(dispatcher) {
    val data = readAssetFileToString(context, "genres.json")
    parseGenres(data)
}

internal fun parseGenres(data: String): List<Genre> {
    val jsonGenres = jsonFormat.decodeFromString<List<JsonGenre>>(data)
    return jsonGenres.map { Genre(id = it.id, name = it.name) }
}

private fun readAssetFileToString(context: Context, fileName: String): String {
    return context.assets.open(fileName).use { stream ->
        stream.bufferedReader().readText()
    }
}

private suspend fun loadActors(context: Context,
                               dispatcher: CoroutineDispatcher): List<Actor> = withContext(dispatcher) {
    val data = readAssetFileToString(context, "people.json")
    parseActors(data)
}

internal fun parseActors(data: String): List<Actor> {
    val jsonActors = jsonFormat.decodeFromString<List<JsonActor>>(data)
    return jsonActors.map { Actor(id = it.id, name = it.name, picture = it.profilePicture) }
}

internal suspend fun loadMovies(context: Context,
                                dispatcher: CoroutineDispatcher): List<Movie> = withContext(dispatcher) {
    val genresMap = loadGenres(context, dispatcher)
    val actorsMap = loadActors(context, dispatcher)

    val data = readAssetFileToString(context, "data.json")
    parseMovies(data, genresMap, actorsMap)
}

internal fun parseMovies(
        data: String,
        genres: List<Genre>,
        actors: List<Actor>
): List<Movie> {
    val genresMap = genres.associateBy { it.id }
    val actorsMap = actors.associateBy { it.id }

    val jsonMovies = jsonFormat.decodeFromString<List<JsonMovie>>(data)
    return jsonMovies.map { jsonMovie ->
        Movie(
                id = jsonMovie.id,
                title = jsonMovie.title,
                overview = jsonMovie.overview,
                poster = jsonMovie.posterPicture,
                backdrop = jsonMovie.backdropPicture,
                ratings = jsonMovie.ratings,
                adult = jsonMovie.adult,
                runtime = jsonMovie.runtime,
                reviews = jsonMovie.reviews,
                genres = jsonMovie.genreIds.map {
                    genresMap[it] ?: throw IllegalArgumentException("Genre not found")
                },
                actors = jsonMovie.actors.map {
                    actorsMap[it] ?: throw IllegalArgumentException("Actor not found")
                }

        )
    }
}