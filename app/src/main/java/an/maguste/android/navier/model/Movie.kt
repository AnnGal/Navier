package an.maguste.android.navier.model

data class Movie(
        val title: String,
        val rating: Double,
        val poster_image : Int,
        val genres: ArrayList<String>,
        val duration: Int,
        val reviews: Int) {

    // show reviews count as string
    val review_string: String
        get() = if (reviews == 1) "$reviews review" else "$reviews reviews"
}
