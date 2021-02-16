package an.maguste.android.navier.moviesdetail

import an.maguste.android.navier.api.MovieApi
import an.maguste.android.navier.api.dtotodomain.convertActorDtoToDomain
import an.maguste.android.navier.data.Actor
import an.maguste.android.navier.movieslist.MoviesListViewModel
import an.maguste.android.navier.storage.repository.ActorsRepositoryImpl
import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class MoviesDetailsViewModel(
    private val apiService: MovieApi,
    private val repository: ActorsRepositoryImpl
) : ViewModel() {

    private val _actors = MutableLiveData<List<Actor>>()
    val actors: LiveData<List<Actor>> get() = _actors

    private var _calendarIntent = MutableLiveData<Intent>()
    val calendarIntent: LiveData<Intent> get() = _calendarIntent

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

    fun scheduleMoveIntoCalendar(movieName: String, dateAndTime: Calendar) {
        val intent = Intent(Intent.ACTION_INSERT)

        intent.type = "vnd.android.cursor.item/event"
        intent.putExtra(CalendarContract.Events.TITLE, movieName)
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, dateAndTime.timeInMillis)

        intent.putExtra(CalendarContract.Events.ALL_DAY, true)
        intent.putExtra(CalendarContract.Events.STATUS, 1)
        intent.putExtra(CalendarContract.Events.VISIBLE, 1)
        intent.putExtra(CalendarContract.Events.HAS_ALARM, 1)

        _calendarIntent.value = intent
    }

    fun scheduleMoveDone() {
        _calendarIntent.value = null
    }
}