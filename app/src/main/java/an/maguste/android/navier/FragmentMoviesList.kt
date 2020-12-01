package an.maguste.android.navier

import an.maguste.android.navier.model.ChangeFragment
import an.maguste.android.navier.model.Movie
import an.maguste.android.navier.model.MovieAdapter
import an.maguste.android.navier.model.OnMovieClickListener
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        recycler?.layoutManager = GridLayoutManager(context, 2)

        // "upload" movies data
        setMovieData()
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

        // TODO set real data
        val moviesList = listOf<Movie>(
                Movie(title = "WWoman", rating = 1.0, posterImage = R.drawable.img_wonder_woman_1984,
                        genres = listOf<String>("Drama", "Romantic"), reviews = 121,
                        duration = 180, ageRating = "13+", like = false),
                Movie(title = "WWoman", rating = 1.0, posterImage = R.drawable.img_wonder_woman_1984,
                        genres = listOf<String>("Drama", "Romantic"), reviews = 121,
                        duration = 180, ageRating = "13+", like = false),
                Movie(title = "WWoman", rating = 1.0, posterImage = R.drawable.img_wonder_woman_1984,
                        genres = listOf<String>("Drama", "Romantic"), reviews = 121,
                        duration = 180, ageRating = "13+", like = false),
                Movie(title = "WWoman", rating = 1.0, posterImage = R.drawable.img_wonder_woman_1984,
                        genres = listOf<String>("Drama", "Romantic"), reviews = 121,
                        duration = 180, ageRating = "13+", like = false)

        )
    }
}