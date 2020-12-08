package an.maguste.android.navier

import an.maguste.android.navier.adapters.MovieAdapter
import an.maguste.android.navier.adapters.OnMovieClickListener
import an.maguste.android.navier.data.ChangeFragment
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.data.loadMovies
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*


class FragmentMoviesList : Fragment() {

    private var recycler: RecyclerView? = null
    private var listenerFragment: ChangeFragment? = null
    private var moviesList: List<Movie>? = null

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
    ): View? {
        // catch layout
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
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
        recycler = view.findViewById(R.id.recyclerView)
        recycler?.adapter = MovieAdapter(movieListener)
        recycler?.layoutManager = GridLayoutManager(context, getSpanCount())
        recycler?.hasFixedSize()

        // "upload" movies data
        getMovieData()
    }

    private fun getMovieData() {
        scope.launch {
            moviesList = context?.let { loadMovies(it) }
            setMovieData()
        }
    }

    // count of grid's columns depends from orientation
    private fun getSpanCount() =
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 3
            else -> 2
        }

    private fun setMovieData() {
        (recycler?.adapter as? MovieAdapter)?.apply {
            moviesList?.let { bindMovie(it) }
        }
    }

    // on MovieCard click reaction
    private val movieListener = object: OnMovieClickListener {
        override fun onClick(movie: Movie) {
            listenerFragment?.toMovieDetail(movie)
        }
    }

}