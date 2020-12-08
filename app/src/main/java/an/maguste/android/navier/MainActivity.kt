package an.maguste.android.navier

import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.data.ChangeFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.add
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity(), ChangeFragment {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add<FragmentMoviesList>(R.id.fragment_place)
            }
        }
    }

    override fun toMovieDetail(movie: Movie) {
        //val fragmentDetail: FragmentMoviesDetails = FragmentMoviesDetails.newInstance(movie)
        Log.d("PARCEL", "movie sended = ${movie?.title}")
        val bundle = Bundle().apply {
            putParcelable(Movie::class.java.simpleName, movie)
        }

        //bundle.putParcelable(Movie::class.java.simpleName, movie)
        supportFragmentManager.commit {
            add<FragmentMoviesDetails>(containerViewId = R.id.fragment_place, args = bundle)
            addToBackStack(null)
        }
    }

    override fun toMoviesList() {
        supportFragmentManager.popBackStack()
    }
}