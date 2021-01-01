package an.maguste.android.navier.moviesdetail

import an.maguste.android.navier.R
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.data.State
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

    private var movieId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentMoviesDetailsBinding.inflate(inflater, container, false)

        movieId = FragmentMoviesDetailsArgs.fromBundle(requireArguments()).movieId

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

        viewModel.loadMovie(movieId)
    }

    private fun setObservers(){
        // observe movie information
        viewModel.selectedMovie.observe(viewLifecycleOwner, {
            setMovieData(it)
        })

        // observe status
        viewModel.state.observe(viewLifecycleOwner, { status ->
            when (status) {
                is State.Init, is State.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.layoutLostData.visibility = View.INVISIBLE
                }
                is State.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.layoutLostData.visibility = View.INVISIBLE
                }
                is State.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.layoutLostData.visibility = View.VISIBLE
                }
            }
        })
    }

    // set data on fragment
    private fun setMovieData(movie: Movie) {
        Glide.with(requireContext())
            .load(movie.backdropLink)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .apply(imageOption)
            .into(binding.poster)

        with(movie) {
            // remove age rating or put correct
            when {
                adult -> { binding.ageRating.text = resources.getString(R.string.age_rating_default) }
                else -> { binding.ageRating.visibility = View.INVISIBLE }
            }

            // set movie data
            binding.title.text = title
            binding.genres.text = genres?.joinToString(", ") { it.name }
            with (binding.ratingBar) {
                visibility = View.VISIBLE
                rating = ratings / 2
            }
            binding.reviews.text = resources.getQuantityString(R.plurals.review, reviews, reviews)
            binding.storylineLabel.visibility = View.VISIBLE
            binding.storylineText.text = overview

            // check actors list not empty
            when (actors?.isNotEmpty()) {
                true -> {
                    binding.cast.visibility = View.VISIBLE
                    (binding.recyclerView.adapter as? ActorAdapter)?.bindActor(actors)
                }
                else -> binding.cast.visibility = View.INVISIBLE
            }
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