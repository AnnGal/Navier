package an.maguste.android.navier.adapters

import an.maguste.android.navier.R
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.databinding.ViewHolderMovieBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

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
    private val binding = ViewHolderMovieBinding.bind(itemView)

    fun bind(movie: Movie) {
        Glide.with(itemView.context)
            .load(movie.poster)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .apply(RequestOptions().centerCrop())
            .into(binding.poster)

        when {
            movie.adult -> binding.ageRating.text = itemView.resources.getString(R.string.age_rating_default)
            else -> binding.ageRating.visibility = View.INVISIBLE
        }

        binding.like.setImageResource(if (movie.like) R.drawable.ic_like else R.drawable.ic_like_empty)
        binding.genres.text = movie.genres.joinToString(", ") { it.name }
        binding.ratingBar.rating =  movie.ratings / 2
        binding.reviews.text = itemView.resources.getQuantityString(R.plurals.review, movie.reviews, movie.reviews)
        binding.title.text = movie.title
        binding.duration.text = itemView.resources.getString(R.string.duration_unit, movie.runtime)
    }

}

interface OnMovieClickListener{
    fun onClick(movie: Movie)
}