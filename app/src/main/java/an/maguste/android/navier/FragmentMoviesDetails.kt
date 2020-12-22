package an.maguste.android.navier

import an.maguste.android.navier.adapters.ActorAdapter
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.databinding.FragmentMoviesDetailsBinding
import an.maguste.android.navier.mvvm.FragmentMoviesDetailsVM
import an.maguste.android.navier.mvvm.MoviesDetailViewModelFactory
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
    private lateinit var viewModel: FragmentMoviesDetailsVM

    // ViewBinding
    private var _binding: FragmentMoviesDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentMoviesDetailsBinding.inflate(inflater, container, false)

        // view model
        val movie = FragmentMoviesDetailsArgs.fromBundle(requireArguments()).selectedMovie
        val viewModelFactory = MoviesDetailViewModelFactory(movie)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(FragmentMoviesDetailsVM::class.java)

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

        viewModel.setMovie()
    }

    private fun setObservers(){
        viewModel.selectedMovie.observe(viewLifecycleOwner, {
            setMovieData(it)
        })
    }

    // set data on fragment
    private fun setMovieData(movie: Movie) {
        Glide.with(requireContext())
            .load(movie.backdrop)
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
            binding.genres.text = genres.joinToString(", ") { it.name }
            binding.ratingBar.rating = ratings / 2
            binding.reviews.text = resources.getQuantityString(R.plurals.review, reviews, reviews)
            binding.storylineText.text = overview

            // check actors list not empty
            when {
                actors.isNotEmpty() -> (binding.recyclerView.adapter as? ActorAdapter)?.bindActor(actors)
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