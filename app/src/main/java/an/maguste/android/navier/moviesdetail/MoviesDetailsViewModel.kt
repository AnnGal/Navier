package an.maguste.android.navier.moviesdetail

import an.maguste.android.navier.api.MovieDbApiService
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.data.State
import an.maguste.android.navier.data.loadMovies
import an.maguste.android.navier.movieslist.MoviesListViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class MoviesDetailsViewModel() : ViewModel() {

    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie> get() = _selectedMovie

    private val _state = MutableLiveData<State>(State.Init)
    val state: LiveData<State> get() = _state
/*
    fun setMovie(){
        _selectedMovie.value = movie
    }
*/


    fun loadMovie(movieId: Int){
        viewModelScope.launch {
            try {
                _state.value = State.Loading
                delay(MoviesListViewModel.DELAY)

                val resultRequest = MovieDbApiService.retrofitService.getMovie(movieId = movieId)

                _selectedMovie.value = resultRequest
                _state.value = State.Success
            } catch (e: Exception){
                _state.value = State.Error
                Log.e(MoviesListViewModel::class.java.simpleName,"Error grab movies data ${e.message}")
            }
        }

   /*     viewModelScope.launch {
            Log.d("RetrofitTry", "try to load genres")

            //val resultRequest = movieApi.getGenresList()
            val resultRequest = MovieDbApiService.retrofitService.getMovie(movieId = movieId)
            Log.d("RetrofitTry", "finish: $resultRequest")
        }*/

    }


}