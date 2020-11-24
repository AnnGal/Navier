package an.maguste.android.navier

import an.maguste.android.navier.model.ChangeFragment
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.card.MaterialCardView

class FragmentMoviesList : Fragment() {

    private var listener: ChangeFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // catch layout
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // set on card reaction
        view.findViewById<MaterialCardView>(R.id.card_view).setOnClickListener {
            listener?.toMovieDetail()
        }
    }

}