package an.maguste.android.navier

import an.maguste.android.navier.model.Actor
import an.maguste.android.navier.model.ActorAdapter
import an.maguste.android.navier.model.ChangeFragment
import an.maguste.android.navier.model.MovieAdapter
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class FragmentMoviesDetails : Fragment() {

    private var listener: ChangeFragment? = null
    private var recycler: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.recyclerView)
        recycler?.adapter = ActorAdapter()

        view.findViewById<Button>(R.id.toolbar).setOnClickListener {
            listener?.toMoviesList()
        }

        setActorsData()
    }

    private fun setActorsData() {
        (recycler?.adapter as? ActorAdapter)?.bindActor(actorsList)
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

    companion object {
        private const val ARG_MOVIE_ID = "movie_id"

        @JvmStatic
        fun newInstance(movieId: String) =
                FragmentMoviesDetails().apply {
                    arguments = Bundle().apply {
                        putString(ARG_MOVIE_ID, movieId)
                    }
                }

        val actorsList = listOf<Actor>(
                Actor("Someone", "Cool", R.drawable.img_evans),
                Actor("Someone", "Cool", R.drawable.img_evans),
                Actor("Someone", "Cool", R.drawable.img_evans),
                Actor("Someone", "Cool", R.drawable.img_evans)
        )
    }
}