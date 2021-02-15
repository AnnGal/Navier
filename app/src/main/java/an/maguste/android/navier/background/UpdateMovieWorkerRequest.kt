package an.maguste.android.navier.background

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import java.util.concurrent.TimeUnit

class UpdateMovieWorkerRequest {
    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .setRequiresCharging(true)       // not working with virtual test devices
        //.setRequiresCharging(false)
        .build()

    val periodicRequestCoroutine =
        PeriodicWorkRequest.Builder(UpdateMovieWorker::class.java, WORKER_REPEAT_TIME, TimeUnit.HOURS)
            .setConstraints(constraints)
            .setInitialDelay(WORKER_DELAY_TIME, TimeUnit.SECONDS)
            .build()

    companion object {
        const val WORKER_MOVIE_UPDATE_NAME = "MovieUpload"
        private const val WORKER_DELAY_TIME: Long = 5L  // seconds
        private const val WORKER_REPEAT_TIME: Long = 8L  // hours
    }
}