package an.maguste.android.navier.movieslist

import an.maguste.android.navier.App
import an.maguste.android.navier.api.MovieApi
import an.maguste.android.navier.api.dtotodomain.convertMovieDtoToDomain
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.notifiactions.MovieNotifications
import an.maguste.android.navier.storage.repository.MoviesRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.lang.Exception

class MoviesListViewModel(
    private val apiService: MovieApi,
    private val repository: MoviesRepository
) : ViewModel() {

    private val _state = MutableLiveData<State>(State.Init)
    val state: LiveData<State> get() = _state

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies



    /** get movies list from DB first. After that try to reach API via internet */
    fun loadMovies() {
        viewModelScope.launch {
            _state.value = State.Loading
            loadMoviesFromDb()
            loadMoviesFromApi()
        }


    }



    private suspend fun loadMoviesFromApi() {
        try {
            // if we got movies from db - don't change state
            if (state.value != State.Success) {
                _state.value = State.Loading
            }

            // get genres
            val genres = apiService.getGenres()
            // get movie
            val moviesDto = apiService.getMovies()
            // get movie domain data
            val movies = convertMovieDtoToDomain(moviesDto.results, genres.genres)

            _movies.value = movies
            _state.value = State.Success

            // don't rewrite with empty data
            if (!movies.isNullOrEmpty()) {
                repository.rewriteMoviesListIntoDB(movies)
                sayNotification(movies[1])
            }

        } catch (e: Exception) {
            // if we didn't receive data from DB before - show error connection
            if (state.value != State.Success) {
                _state.value = State.Error
            }
            // log error anyway
            Log.e(
                MoviesListViewModel::class.java.simpleName,
                "Error grab movies data from API: ${e.message}"
            )
        }
    }

    private fun sayNotification(movie: Movie) {
        val notifications = MovieNotifications(App.context())
        notifications.initialize()
        notifications.showNotification(movie)
    }

    private suspend fun loadMoviesFromDb() {
        try {
            // load movies from database
            val moviesDB = repository.getAllMovies()

            // if there are any movies - show them and show success state
            if (moviesDB.isNotEmpty()) {
                _movies.value = moviesDB
                _state.value = State.Success
            }
        } catch (e: Exception) {
            Log.e(
                MoviesListViewModel::class.java.simpleName,
                "Error grab movies data from DB: ${e.message}"
            )
        }
    }


}