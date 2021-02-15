package an.maguste.android.navier

import an.maguste.android.navier.background.UpdateMovieWorkerRequest
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.movieslist.FragmentMoviesListDirections
import an.maguste.android.navier.movieslist.State
import an.maguste.android.navier.storage.repository.MoviesRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: MoviesRepository) : ViewModel()  {

    private val backgroundRequests = UpdateMovieWorkerRequest()

    // it really recommended decision for MVVM navigation from google lessons
    private val _navigateToSelectedMovie = MutableLiveData<Movie>()
    val navigateToSelectedMovie: LiveData<Movie> get() = _navigateToSelectedMovie

    /** check new Movie from background */
    fun startBackgroundMovieCheck(){
        WorkManager.getInstance(App.context()).enqueueUniquePeriodicWork(
            UpdateMovieWorkerRequest.WORKER_MOVIE_UPDATE_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            backgroundRequests.periodicRequestCoroutine
        )
    }

    /** when intent returns to app from notification -  show selected movie*/
    fun showMovieFromNotification(movieId: Long){
        // check Movie in DB
        viewModelScope.launch {
            val movie = repository.getMovieById(movieId)
            _navigateToSelectedMovie.value = movie
        }
    }

    /** set to null after navigation to avoid false jumps*/
    fun howMovieFromNotificationComplete() {
        _navigateToSelectedMovie.value = null
    }
}