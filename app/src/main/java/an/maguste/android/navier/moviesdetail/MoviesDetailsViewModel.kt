package an.maguste.android.navier.moviesdetail

import an.maguste.android.navier.api.MovieApi
import an.maguste.android.navier.api.dtotodomain.convertActorDtoToDomain
import an.maguste.android.navier.data.Actor
import an.maguste.android.navier.movieslist.MoviesListViewModel
import an.maguste.android.navier.storage.MoviesRepositoryImpl
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MoviesDetailsViewModel(
    private val apiService: MovieApi,
    private val repository: MoviesRepositoryImpl
) : ViewModel() {

    private val _actors = MutableLiveData<List<Actor>>()
    val actors: LiveData<List<Actor>> get() = _actors

    fun getActors(movieId: Int) {
        loadActorsFromDb(movieId)
        loadActorsFromApi(movieId)
    }

    private fun loadActorsFromApi(movieId: Int) {
        viewModelScope.launch {
            try {
                // get actors
                val resultRequest = apiService.getActors(movieId = movieId)
                // get actors domain data
                val actors = convertActorDtoToDomain(resultRequest.actors)

                _actors.value = actors

                // do not rewrite with empty data
                if (!actors.isNullOrEmpty()) {
                    saveActorsLocally(movieId)
                }
            } catch (e: Exception) {
                Log.e(
                    MoviesDetailsViewModel::class.java.simpleName,
                    "Error grab actors data from API: ${e.message}"
                )
            }
        }
    }

    private fun saveActorsLocally(movieId: Int) {
        if (!actors.value.isNullOrEmpty()) {
            viewModelScope.launch {
                repository.rewriteActorsByMovieIntoDB(actors.value!!, movieId)
            }
        }
    }

    private fun loadActorsFromDb(movieId: Int) {
        viewModelScope.launch {
            try {
                // load actors from database
                val actorsDB = repository.getAllActorsByMovie(movieId)

                if (actorsDB.isNotEmpty()) {
                    _actors.value = actorsDB
                }

            } catch (e: Exception) {
                Log.e(
                    MoviesListViewModel::class.java.simpleName,
                    "Error grab actors data from DB: ${e.message}"
                )
            }
        }
    }
}