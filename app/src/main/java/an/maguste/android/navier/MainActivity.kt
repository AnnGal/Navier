package an.maguste.android.navier

import an.maguste.android.navier.databinding.ActivityMainBinding
import an.maguste.android.navier.background.UpdateMovieWorkerRequest
import an.maguste.android.navier.background.UpdateMovieWorkerRequest.Companion.WORKER_MOVIE_UPDATE_NAME
import an.maguste.android.navier.movieslist.FragmentMoviesListDirections
import an.maguste.android.navier.movieslist.MoviesListViewModel
import an.maguste.android.navier.movieslist.MoviesListViewModelFactory
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.commitNow
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager

class MainActivity : AppCompatActivity() {

    // view model
    private val viewModel: MainActivityViewModel by viewModels { MainActivityViewModelFactory() }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        viewModel.startBackgroundMovieCheck()

        if (savedInstanceState == null) {
            intent?.let(::handleIntent)
        }

        setObservers()

        setContentView(view)
    }

    private fun setObservers() {
        viewModel.navigateToSelectedMovie.observe(this, { movie ->
            if (null != movie) {
                findNavController(R.id.navigationFragment)
                    .navigate(FragmentMoviesListDirections.actionToMoviesDetails(movie))
            }
        })
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            // Invoked when a dynamic shortcut is clicked.
            Intent.ACTION_VIEW -> {
                val id = intent.data?.lastPathSegment?.toLongOrNull()
                if (id != null) {
                    viewModel.showMovieFromNotification(id.toLong())
                }
            }
        }
    }

}