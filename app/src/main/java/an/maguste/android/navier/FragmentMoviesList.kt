package an.maguste.android.navier

import an.maguste.android.navier.adapters.MovieAdapter
import an.maguste.android.navier.adapters.OnMovieClickListener
import an.maguste.android.navier.model.ChangeFragment
import an.maguste.android.navier.model.Movie
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FragmentMoviesList : Fragment() {

    private var recycler: RecyclerView? = null

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
        setMovieData()
    }

    // count of grid's columns depends from orientation
    private fun getSpanCount() =
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 3
            else -> 2
        }

    private fun setMovieData() {
        (recycler?.adapter as? MovieAdapter)?.apply {
            bindMovie(moviesList)
        }
    }

    // on MovieCard click reaction
    private val movieListener = object: OnMovieClickListener {
        override fun onClick(movie: Movie) {
            listenerFragment?.toMovieDetail()
        }
    }

    companion object{
        private var listenerFragment: ChangeFragment? = null

        val moviesList = listOf(
                Movie(title = "Avengers: End Game", rating = 4.0, posterImage = R.drawable.img_avengers,
                        genres = listOf("Action", "Adventure", "Drama"), reviews = 125,
                        duration = 137, ageRating = "13+", like = false),
                Movie(title = "Tenet", rating = 5.0, posterImage = R.drawable.img_tenet,
                        genres = listOf("Action", "Sci-Fi", "Thriller"), reviews = 98,
                        duration = 97, ageRating = "16+", like = true),
                Movie(title = "Black Widow", rating = 4.0, posterImage = R.drawable.img_black_widow,
                        genres = listOf("Action", "Adventure", "Sci-Fi"), reviews = 38,
                        duration = 102, ageRating = "13+", like = false),
                Movie(title = "Wonder Woman 1984", rating = 5.0, posterImage = R.drawable.img_wonder_woman_1984,
                        genres = listOf("Action", "Adventure", "Fantasy"), reviews = 74,
                        duration = 120, ageRating = "13+", like = false)

        )
    }
}