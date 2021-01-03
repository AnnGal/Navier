package an.maguste.android.navier.movieslist

import an.maguste.android.navier.api.MovieApiService
import an.maguste.android.navier.api.convertFromJsonToMovie
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.data.loadMovies
import an.maguste.android.navier.data.State
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import org.jetbrains.annotations.TestOnly
import java.lang.Exception

class MoviesListViewModel(private val context: Context) : ViewModel() {

    private val _state = MutableLiveData<State>(State.Init)
    val state: LiveData<State> get() = _state

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

     /** get movies list from assets */
     @TestOnly
    fun loadMoviesFromAssets() {
        viewModelScope.launch {
            try {
                _state.value = State.Loading
                delay(DELAY)
                // throw Exception("Sudden error") // for test Exception
                val movies = loadMovies(context)
                _movies.value = movies
                _state.value = State.Success
            } catch (e: Exception){
                _state.value = State.Error
                Log.e(MoviesListViewModel::class.java.simpleName,"Error grab movies data ${e.message}")
            }
        }
    }

    /** get movies list from API */
    fun loadMovies(){
        viewModelScope.launch {
            try {
                _state.value = State.Loading
                // throw Exception("Sudden error") // for test Exception

                // get genres
                val genres = MovieApiService.retrofitService.getGenres()

                // get movie
                val moviesDto = MovieApiService.retrofitService.getMovies()

                // convert movies data
                val movies = moviesDto.results?.let { convertFromJsonToMovie(it, genres.genres) }

                _movies.value = movies
                _state.value = State.Success
            } catch (e: Exception){
                _state.value = State.Error
                Log.e(MoviesListViewModel::class.java.simpleName,"Error grab movies data ${e.message}")
            }
        }
    }

    companion object{
        const val DELAY: Long = 1_000
    }
}