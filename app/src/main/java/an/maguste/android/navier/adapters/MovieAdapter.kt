package an.maguste.android.navier.adapters

import an.maguste.android.navier.R
import an.maguste.android.navier.model.Movie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(private var movieListener: OnMovieClickListener) : RecyclerView.Adapter<MovieViewHolder>() {

    private var moviesList = listOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
            MovieViewHolder(LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.view_holder_movie, parent, false))

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(moviesList[position])
        holder.itemView.setOnClickListener { movieListener.onClick(moviesList[position]) }
    }

    override fun getItemCount(): Int = moviesList.size

    fun bindMovie(newMoviesList: List<Movie>) {
        moviesList = newMoviesList
        notifyDataSetChanged()
    }

}

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val poster = itemView.findViewById<ImageView>(R.id.imgTitlePoster)
    private val ageRating = itemView.findViewById<TextView>(R.id.tvAgeRating)
    private val like = itemView.findViewById<ImageView>(R.id.ivLike)
    private val genres = itemView.findViewById<TextView>(R.id.tvGenres)
    private val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)
    private val reviews = itemView.findViewById<TextView>(R.id.tvReviews)
    private val title = itemView.findViewById<TextView>(R.id.tvTitle)
    private val duration = itemView.findViewById<TextView>(R.id.tvDuration)

    fun bind(movie: Movie) {
        poster.setImageResource(movie.posterImage)
        ageRating.text = movie.ageRating
        like.setImageResource(if (movie.like) R.drawable.ic_like else R.drawable.ic_like_empty)
        genres.text = movie.genres.joinToString(", ")
        ratingBar.rating = movie.rating.toFloat()
        reviews.text = itemView.resources.getQuantityString(R.plurals.review, movie.reviews, movie.reviews)
        title.text = movie.title
        duration.text = itemView.resources.getString(R.string.duration_unit, movie.duration)
    }
}

interface OnMovieClickListener{
    fun onClick(movie: Movie)
}