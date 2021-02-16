package an.maguste.android.navier.background

import an.maguste.android.navier.api.MovieApi
import an.maguste.android.navier.api.RetrofitHolder
import an.maguste.android.navier.api.dtotodomain.convertMovieDtoToDomain
import an.maguste.android.navier.data.Movie
import an.maguste.android.navier.notifiactions.MovieNotifications
import an.maguste.android.navier.storage.repository.RepositoryHolder
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.create

class UpdateMovieWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @Suppress("EXPERIMENTAL_API_USAGE")
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val api = RetrofitHolder.retrofit.create<MovieApi>()
                val repository = RepositoryHolder.moviesRepository()

                // get genres
                val genres = api.getGenres()
                // get movie
                val moviesDto = api.getMovies()
                // get movie domain data
                val movies = convertMovieDtoToDomain(moviesDto.results, genres.genres)

                // get old movies from DB
                val oldMovies = repository.getAllMovies()

                // don't rewrite with empty data
                if (!movies.isNullOrEmpty()) {
                    // write new movies into DB
                    repository.rewriteMoviesListIntoDB(movies)
                }

                // if we have any movies - find new best movie and show notification
                if (!movies.isNullOrEmpty() || !oldMovies.isNullOrEmpty()) {
                    checkNewMoviesForNotification(oldMovies, movies)
                }

                Result.success()
            } catch (e: Exception) {
                Result.failure()

            }
        }
    }

    private fun checkNewMoviesForNotification(oldMovies: List<Movie>, movies: List<Movie>?) {
        // find new best movie or just best movie
        val movie = movies?.subtract(oldMovies)?.maxByOrNull { it.ratings }
            ?: oldMovies.maxByOrNull { it.ratings }

        if (movie != null) {
            sayNotification(movie)
        }
    }

    private fun sayNotification(movie: Movie) {
        val notifications = MovieNotifications(context)
        notifications.initialize()
        notifications.showNotification(movie)
    }
}