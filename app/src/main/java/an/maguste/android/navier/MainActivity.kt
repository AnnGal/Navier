package an.maguste.android.navier

import an.maguste.android.navier.model.ChangeFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace

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

    override fun toMovieDetail() {
        supportFragmentManager.commit {
            add<FragmentMoviesDetails>(R.id.fragment_place)
            addToBackStack(null)
        }
    }

    override fun toMoviesList() {
        supportFragmentManager.popBackStack()
    }
}