package an.maguste.android.navier.moviesdetail

import an.maguste.android.navier.R
import an.maguste.android.navier.data.Actor
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.databinding.FragmentMoviesDetailsBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class FragmentMoviesDetails : Fragment() {

    // view model
    private lateinit var viewModel: MoviesDetailsViewModel

    // ViewBinding
    private var _binding: FragmentMoviesDetailsBinding? = null
    private val binding get() = _binding!!

    private var movie: Movie? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesDetailsBinding.inflate(inflater, container, false)

        movie = FragmentMoviesDetailsArgs.fromBundle(requireArguments()).selectedMovie

        val viewModelFactory = MoviesDetailViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MoviesDetailsViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = ActorAdapter()
        binding.recyclerView.hasFixedSize()

        binding.toolbar.setOnClickListener {
            requireActivity().onBackPressed()
        }

        setObservers()

        movie?.let {
            setMovieData(it)
            viewModel.getActors(it.id)
        }
    }

    private fun setObservers() {
        // observe actors data
        viewModel.actors.observe(viewLifecycleOwner, {
            setActorsData(it)
        })
    }

    // set data on fragment
    private fun setMovieData(movie: Movie) {
        movie.backdrop?.let {
            Glide.with(requireContext())
                .load(movie.backdrop)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .apply(imageOption)
                .into(binding.poster)
        }

        with(movie) {
            // remove age rating or put correct
            if (adult) {
                binding.ageRating.text = resources.getString(R.string.age_rating_default)
            } else {
                binding.ageRating.visibility = View.INVISIBLE
            }

            binding.title.text = title
            binding.genres.text = genres.joinToString(", ")
            with(binding.ratingBar) {
                visibility = View.VISIBLE
                rating = ratings
            }
            binding.reviews.text = resources.getQuantityString(R.plurals.review, reviews, reviews)

            // storyline
            overview?.let {
                binding.storylineLabel.visibility = View.VISIBLE
                binding.storylineText.text = overview
            }
        }
    }

    private fun setActorsData(actors: List<Actor>) {
        if (actors.isNotEmpty()) {
            binding.cast.visibility = View.VISIBLE
            (binding.recyclerView.adapter as? ActorAdapter)?.bindActor(actors)
        } else {
            binding.cast.visibility = View.INVISIBLE
        }
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