package an.maguste.android.navier

import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.data.ChangeFragment
import an.maguste.android.navier.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity()/*, ChangeFragment */{

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

/*    override fun toMovieDetail(movie: Movie) {
        val bundle = Bundle().apply {
            putParcelable(Movie::class.java.simpleName, movie)
        }

        supportFragmentManager.commit {
            add<FragmentMoviesDetails>(containerViewId = R.id.navigationFragment, args = bundle)
            addToBackStack(null)
        }
    }

    override fun toMoviesList() {
        supportFragmentManager.popBackStack()
    }*/
}