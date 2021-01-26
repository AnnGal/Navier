package an.maguste.android.navier.services

import an.maguste.android.navier.api.MovieApi
import an.maguste.android.navier.api.dtotodomain.convertMovieDtoToDomain
import an.maguste.android.navier.movieslist.State
import an.maguste.android.navier.storage.repository.MoviesRepository
import android.content.Context
import android.util.Log
//import androidx.work.CoroutineWorker

import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/*class CoroutineDownloadWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            val data = downloadSynchronously("https://www.google.com")
            saveData(data)
            return Result.success()
        }

    }
}*/
class UpdateMovieWorker(context: Context, workerParams: WorkerParameters,
                        private val apiService: MovieApi,
                        private val repository: MoviesRepository
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        try {
            //withContext(Dispatchers.IO)
            // get genres
            /*val genres = apiService.getGenres()
            // get movie
            val moviesDto = apiService.getMovies()
            // get movie domain data
            val movies = convertMovieDtoToDomain(moviesDto.results, genres.genres)
            // don't rewrite with empty data
            if (!movies.isNullOrEmpty()) {
                repository.rewriteMoviesListIntoDB(movies)
            }*/
            Log.d("TryBackground", "UpdateWorker in charge ")
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}
/*
    override suspend fun doWork(): kotlin.Result = coroutineScope {
        try {
            applicationContext.assets.open(PLANT_DATA_FILENAME).use { inputStream ->
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val plantType = object : TypeToken<List<Plant>>() {}.type
                    val plantList: List<Plant> = Gson().fromJson(jsonReader, plantType)

                    val database = AppDatabase.getInstance(applicationContext)
                    database.plantDao().insertAll(plantList)

                    kotlin.Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            kotlin.Result.failure()
        }
    }
*/

