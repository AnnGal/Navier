package an.maguste.android.navier

import an.maguste.android.navier.model.ChangeFragment
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

private const val ARG_MOVIE_ID = "movie_id"

class FragmentMoviesDetails : Fragment() {

    private var listener: ChangeFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.toolbar).setOnClickListener {
            listener?.toMoviesList()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // catch listener
        listener = context as ChangeFragment
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
                FragmentMoviesDetails().apply {
                    arguments = Bundle().apply {
                        putString(ARG_MOVIE_ID, param1)
                    }
                }
    }
}