package an.maguste.android.navier.model

data class Movie(
        val title: String,
        val rating: Double,
        val posterImage: Int,
        val genres: List<String>,
        val reviews: Int,
        val duration: Int,
        val ageRating: String,
        val like: Boolean) {

    // show reviews count as string
    val reviewString: String
        get() = if (reviews == 1) "$reviews review" else "$reviews reviews"
    val durationString: String
        get() = "$duration MIN"
}
