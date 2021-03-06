package an.maguste.android.navier.movieslist

import an.maguste.android.navier.R
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.databinding.ViewHolderMovieBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions


class MovieAdapter(private var movieListener: OnMovieClickListener)
    : ListAdapter<Movie, MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.view_holder_movie, parent, false)
        )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)

        holder.bind(movie)
        holder.itemView.setOnClickListener { movieListener.onClick(movie) }
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

        if (movie.adult) {
            binding.ageRating.text = itemView.resources.getString(R.string.age_rating_default)
        } else {
            binding.ageRating.visibility = View.INVISIBLE
        }

        binding.like.setImageResource(if (movie.like) R.drawable.ic_like else R.drawable.ic_like_empty)
        binding.genres.text = movie.genres.joinToString(", ")
        binding.ratingBar.rating = movie.ratings
        binding.reviews.text =
            itemView.resources.getQuantityString(R.plurals.review, movie.reviews, movie.reviews)
        binding.title.text = movie.title
        movie.runtime?.let {
            binding.duration.text =
                itemView.resources.getString(R.string.duration_unit, movie.runtime)
        }
    }
}

class OnMovieClickListener(val clickListener: (movie: Movie) -> Unit) {
    fun onClick(movie: Movie) = clickListener(movie)
}

private class MovieDiffCallback: DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}