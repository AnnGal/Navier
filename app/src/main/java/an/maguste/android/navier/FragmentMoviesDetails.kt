package an.maguste.android.navier

import an.maguste.android.navier.adapters.ActorAdapter
import an.maguste.android.navier.data.ChangeFragment
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.databinding.FragmentMoviesDetailsBinding
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class FragmentMoviesDetails : Fragment() {

    private var listener: ChangeFragment? = null
    private var recycler: RecyclerView? = null

    private var _binding: FragmentMoviesDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentMoviesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.recyclerView)
        recycler?.adapter = ActorAdapter()
        recycler?.hasFixedSize()

        view.findViewById<Button>(R.id.toolbar).setOnClickListener {
            listener?.toMoviesList()
        }

        Log.d("PARCEL", "try to get movie 1 ${requireArguments().isEmpty}")
        val movie: Movie? = requireArguments().getParcelable(Movie::class.java.simpleName)
        Log.d("PARCEL", "movie income 1 = ${movie?.title}")

        movie?.let{ setMovieData(it) }
    }

    private fun setMovieData(movie: Movie) {
        Glide.with(requireContext())
            .load(movie.backdrop)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .apply(imageOption)
            .into(binding.imgTitlePoster)

        movie.apply {
            // remove age rating or put correct
            when {
                !adult -> { binding.tvAgeRating.visibility = View.INVISIBLE }
                else -> { binding.tvAgeRating.text = resources.getString(R.string.age_rating_default) }
            }

            // set movie data
            binding.tvTitle.text = title
            binding.tvGenres.text = genres.joinToString(", ") { it.name }
            binding.ratingBar.rating = ratings / 2
            binding.tvReviews.text = resources.getQuantityString(R.plurals.review, reviews, reviews)
            binding.tvStorylineText.text = overview

            // check actors list not empty
            when {
                actors.isNotEmpty() -> (recycler?.adapter as? ActorAdapter)?.bindActor(actors)
                else -> binding.tvCast.visibility = View.INVISIBLE
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // catch listener
        listener = context as? ChangeFragment
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.empty_photo)
            .fallback(R.drawable.empty_photo)
    }

}