package an.maguste.android.navier.moviesdetail

import an.maguste.android.navier.api.MovieApi
import an.maguste.android.navier.api.dtotodomain.convertActorDtoToDomain
import an.maguste.android.navier.data.Actor
import an.maguste.android.navier.movieslist.MoviesListViewModel
import an.maguste.android.navier.storage.repository.ActorsRepositoryImpl
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MoviesDetailsViewModel(
    private val apiService: MovieApi,
    private val repository: ActorsRepositoryImpl
) : ViewModel() {

    private val _actors = MutableLiveData<List<Actor>>()
    val actors: LiveData<List<Actor>> get() = _actors

    fun getActors(movieId: Int) {
        viewModelScope.launch {
            loadActorsFromDb(movieId)
            loadActorsFromApi(movieId)
        }
    }

    private suspend fun loadActorsFromApi(movieId: Int) {
        try {
            // get actors
            val resultRequest = apiService.getActors(movieId = movieId)
            // get actors domain data
            val actors = convertActorDtoToDomain(resultRequest.actors)

            _actors.value = actors

            // do not rewrite with empty data
            if (!actors.isNullOrEmpty()) {
                repository.rewriteActorsByMovieIntoDB(actors, movieId)
            }
        } catch (e: Exception) {
            Log.e(
                MoviesDetailsViewModel::class.java.simpleName,
                "Error grab actors data from API: ${e.message}"
            )
        }
    }

    private suspend fun loadActorsFromDb(movieId: Int) {
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