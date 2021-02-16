package an.maguste.android.navier

import an.maguste.android.navier.databinding.ActivityMainBinding
import an.maguste.android.navier.movieslist.FragmentMoviesListDirections
import an.maguste.android.navier.notifiactions.MovieNotifications
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    // view model
    private val viewModel: MainActivityViewModel by viewModels { MainActivityViewModelFactory() }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

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
            Intent.ACTION_VIEW -> {
                val id = intent.data?.lastPathSegment?.toLongOrNull()
                if (id != null) {
                    viewModel.showMovieFromNotification(id.toLong())
                    viewModel.showMovieFromNotificationComplete()

                    // bad decision?
                    val notifications = MovieNotifications(App.context())
                    notifications.initialize()
                    notifications.dismissNotification(id)
                }
            }
            // first start
            else -> viewModel.startBackgroundMovieCheck()
        }
    }

}