package an.maguste.android.navier

import an.maguste.android.navier.adapters.MovieAdapter
import an.maguste.android.navier.adapters.OnMovieClickListener
import an.maguste.android.navier.data.ChangeFragment
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.data.loadMovies
import an.maguste.android.navier.databinding.FragmentMoviesListBinding
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.*


class FragmentMoviesList : Fragment() {

    private var listenerFragment: ChangeFragment? = null

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.d(FragmentMoviesList::class.java.simpleName,"CoroutineException: $exception")
    }

    private var scope = CoroutineScope(
            SupervisorJob() +
                    Dispatchers.IO +
                    exceptionHandler
    )

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // catch listener from activity
        listenerFragment = context as? ChangeFragment
    }

    override fun onDetach() {
        super.onDetach()
        listenerFragment = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set recycler grid
        binding.recyclerView.adapter = MovieAdapter(movieListener)
        binding.recyclerView.layoutManager = GridLayoutManager(context, getSpanCount())
        binding.recyclerView.hasFixedSize()

        // "upload" movies data
        getMovieData()
    }

    private fun getMovieData() {
        var moviesList: List<Movie>? = null
        scope.launch {
            // get movie list
            moviesList = loadMovies(requireContext())
            // send list into adapter
            (binding.recyclerView.adapter as? MovieAdapter)?.apply {
                moviesList?.let { bindMovie(it) }
            }
        }
    }

    // count of grid's columns depends from orientation
    private fun getSpanCount() =
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 3
            else -> 2
        }

    // on MovieCard click reaction
    private val movieListener = object: OnMovieClickListener {
        override fun onClick(movie: Movie) {
            listenerFragment?.toMovieDetail(movie)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scope.cancel()
        _binding = null
    }
}