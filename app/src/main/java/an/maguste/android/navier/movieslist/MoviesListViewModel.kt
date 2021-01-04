package an.maguste.android.navier.movieslist

import an.maguste.android.navier.api.MovieApi
import an.maguste.android.navier.api.convertFromJsonToMovie
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.data.State
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.lang.Exception

class MoviesListViewModel(private val apiService: MovieApi) :
    ViewModel() {

    private val _state = MutableLiveData<State>(State.Init)
    val state: LiveData<State> get() = _state

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    /** get movies list from API */
    fun loadMovies() {
        viewModelScope.launch {
            try {
                _state.value = State.Loading

                // get genres
                val genres = apiService.getGenres()
                // get movie
                val moviesDto = apiService.getMovies()
                // convert movies data
                val movies = moviesDto.results?.let { convertFromJsonToMovie(it, genres.genres) }

                _movies.value = movies
                _state.value = State.Success
            } catch (e: Exception) {
                _state.value = State.Error
                Log.e(
                    MoviesListViewModel::class.java.simpleName,
                    "Error grab movies data ${e.message}"
                )
            }
        }
    }
}