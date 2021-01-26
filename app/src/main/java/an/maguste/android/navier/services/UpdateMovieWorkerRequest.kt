package an.maguste.android.navier.services

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import java.util.concurrent.TimeUnit

class UpdateMovieWorkerRequest {
    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .setRequiresCharging(true)
        .build()

    val periodicRequest = PeriodicWorkRequest.Builder(UpdateMovieWorker::class.java,  15L, TimeUnit.MINUTES)
        //.setConstraints(constraints)
        .setInitialDelay(10L, TimeUnit.SECONDS)
        .build()

    companion object{
        const val WORKER_MOVIE_UPDATE_NAME = "MovieUpload"
    }
}