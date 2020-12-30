package an.maguste.android.navier.movieslist


import an.maguste.android.navier.api.MovieDbApiService
//import an.maguste.android.navier.api.RetrofitModule.movieApi
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

     /** get movie list from assets */
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



    fun loadMovies(){
        viewModelScope.launch {
            try {
                _state.value = State.Loading
                // throw Exception("Sudden error") // for test Exception
                val genres = MovieDbApiService.retrofitService.getGenres()
                val movie = MovieDbApiService.retrofitService.getMovies()

                val genresMap = genres.genres.associateBy { it.id }

                for (mov in movie.results!!){
                    mov.genres = mov.genreIds?.map {
                        genresMap[it] ?: throw IllegalArgumentException("Genre not found")
                    }
                }

                _movies.value = movie.results
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