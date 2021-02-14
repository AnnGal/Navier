package an.maguste.android.navier.background

import an.maguste.android.navier.api.MovieApi
import an.maguste.android.navier.api.RetrofitHolder
import an.maguste.android.navier.api.dtotodomain.convertMovieDtoToDomain
import an.maguste.android.navier.storage.repository.RepositoryHolder
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.create

class UpdateMovieWorker(
    context: Context,
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

                // don't rewrite with empty data
                if (!movies.isNullOrEmpty()) {
                    repository.rewriteMoviesListIntoDB(movies)
                }



                Result.success()
            } catch (e: Exception) {
                Result.failure()

            }
        }
    }
}