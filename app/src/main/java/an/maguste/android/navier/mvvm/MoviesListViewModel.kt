package an.maguste.android.navier.mvvm

import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.data.loadMovies
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.lang.Exception

class MoviesListViewModel(private val context: Context,
                          private val dispatcher: CoroutineDispatcher) : ViewModel() {

    private val _state = MutableLiveData<State>(State.Init())
    val state: LiveData<State> get() = _state

    private val _moviesData = MutableLiveData<List<Movie>>()
    val moviesData: LiveData<List<Movie>> get() = _moviesData

    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie> get() = _selectedMovie

    init {
        parseMovieAsset()
    }

    /** get movie list from assets */
    private fun parseMovieAsset() {
        viewModelScope.launch {
            try {
                _state.value = State.Loading()
                delay(DELAY)
                // throw Exception("Sudden error") // for test Exception
                val movies = loadMovies(context, dispatcher)
                _moviesData.value = movies
                _state.value = State.Success()
            } catch (e: Exception){
                _state.value = State.Error()
                Log.e(MoviesListViewModel::class.java.simpleName,"Error grab movies data ${e.message}")
            }
        }
    }

    /** on MovieCard click */
    fun selectMovie(movie: Movie){
        _selectedMovie.value = movie
    }

    fun selectMovieShown(){
        _selectedMovie.value = null
    }

    companion object{
        const val DELAY: Long = 1_000
    }

}