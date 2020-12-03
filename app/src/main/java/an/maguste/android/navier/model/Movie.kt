package an.maguste.android.navier.model

data class Movie(
        val title: String,
        val rating: Double,
        val posterImage: Int,
        val genres: List<String>,
        val reviews: Int,
        val duration: Int,
        val ageRating: String,
        val like: Boolean)
