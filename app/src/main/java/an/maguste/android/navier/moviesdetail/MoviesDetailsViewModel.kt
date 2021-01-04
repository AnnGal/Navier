package an.maguste.android.navier.moviesdetail

import an.maguste.android.navier.api.MovieApi
import an.maguste.android.navier.api.convertActorDtoToDomain
import an.maguste.android.navier.data.Actor
import an.maguste.android.navier.data.Movie
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MoviesDetailsViewModel(private val apiService: MovieApi) : ViewModel() {

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> get() = _movie

    private val _actors = MutableLiveData<List<Actor>>()
    val actors: LiveData<List<Actor>> get() = _actors

    fun setMovie(movie: Movie) {
        _movie.value = movie
        getActors(movie.id)
    }

    private fun getActors(movieId: Int) {
        viewModelScope.launch {
            try {
                // get actors
                val resultRequest = apiService.getActors(movieId = movieId)
                // convert actors data
                val actors = resultRequest.actors?.let { convertActorDtoToDomain(it) }

                _actors.value = actors

            } catch (e: Exception) {
                Log.e(
                    MoviesDetailsViewModel::class.java.simpleName,
                    "Error grab actors data ${e.message}"
                )
            }
        }
    }
}