package an.maguste.android.navier.data

import an.maguste.android.navier.BuildConfig
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Movie(
        @SerialName("id")
        val id: Int,
        @SerialName("title")
        val title: String,
        @SerialName("overview")
        val overview: String,
        @SerialName("poster_path")
        val poster: String,
        @SerialName("backdrop_path")
        val backdrop: String,
        @SerialName("vote_average")
        val ratings: Float,
        @SerialName("adult")
        val adult: Boolean,
        @SerialName("runtime")
        val runtime: Int? = null,
        @SerialName("vote_count")
        val reviews: Int,
        @SerialName("genre_ids")
        val genreIds: List<Int>? = listOf(),
        @SerialName("genres")
        var genres: List<Genre>? = listOf(),
        @SerialName("actors")
        val actors: List<Actor>? = listOf(),
        @SerialName("like")
        val like: Boolean? = false,
        @SerialName("homepage")
        val homepage: String = "",
        @SerialName("tagline")
        val tagline: String = ""
) : Parcelable {
        val backdropLink: String
                get() = BuildConfig.IMAGE_URL + backdrop
        val posterLink: String
                get() = BuildConfig.IMAGE_URL + poster
}

@Serializable
data class MoviesJson(
        val results: List<Movie>? = null
)
