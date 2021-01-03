package an.maguste.android.navier.moviesdetail

import an.maguste.android.navier.api.MovieApiService
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.data.State
import an.maguste.android.navier.movieslist.MoviesListViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class MoviesDetailsViewModel : ViewModel() {

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> get() = _movie

    fun setMovie(movie: Movie) {
        _movie.value = movie
        getActors(movie.id)
    }

    private fun getActors(movieId: Int) {
        viewModelScope.launch {
            try {
                val resultRequest = MovieApiService.retrofitService.getMovie(movieId = movieId)

                _movie.value = resultRequest
            } catch (e: Exception){
                Log.e(MoviesListViewModel::class.java.simpleName,"Error grab movies data ${e.message}")
            }
        }
    }
}