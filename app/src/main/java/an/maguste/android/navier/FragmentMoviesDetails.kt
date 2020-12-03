package an.maguste.android.navier

import an.maguste.android.navier.adapters.ActorAdapter
import an.maguste.android.navier.model.Actor
import an.maguste.android.navier.model.ChangeFragment
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

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
        recycler?.hasFixedSize()

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

        val actorsList = listOf(
                Actor("Robert", "Downey Jr.", R.drawable.img_downey),
                Actor("Chris", "Evans", R.drawable.img_evans),
                Actor("Mark", "Ruffalo", R.drawable.img_ruffalo),
                Actor("Chris", "Hemsworth", R.drawable.img_hemsworth)
        )
    }
}