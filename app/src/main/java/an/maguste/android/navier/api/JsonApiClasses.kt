package an.maguste.android.navier.api

import an.maguste.android.navier.BuildConfig
import an.maguste.android.navier.data.Genre
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviesDto(
    val results: List<MovieDto>? = null
)

@Serializable
data class GenresDto(
    val genres: List<Genre>
)

@Serializable
data class MovieDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("overview")
    val overview: String?,
    @SerialName("poster_path")
    val poster: String?,
    @SerialName("backdrop_path")
    val backdrop: String?,
    @SerialName("vote_average")
    val ratings: Float,
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("runtime")
    val runtime: Int? = null,
    @SerialName("vote_count")
    val reviews: Int,
    @SerialName("genre_ids")
    val genreIds: List<Int>
) {
    val backdropLink: String?
        get() = backdrop?.let { BuildConfig.IMAGE_URL + backdrop }
    val posterLink: String?
        get() = poster?.let { BuildConfig.IMAGE_URL + poster }
}

@Serializable
data class ActorsListDto(
    @SerialName("cast")
    val actors: List<ActorDto>? = null
)

@Serializable
data class ActorDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("profile_path")
    val image: String? = null
) {
    val imageLink: String?
        get() = image?.let { BuildConfig.IMAGE_URL + image }
}
