package an.maguste.android.navier

import an.maguste.android.navier.databinding.ActivityMainBinding
import an.maguste.android.navier.services.UpdateMovieWorkerRequest
import an.maguste.android.navier.services.UpdateMovieWorkerRequest.Companion.WORKER_MOVIE_UPDATE_NAME
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val backgroundRequests = UpdateMovieWorkerRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        //WorkManager.getInstance(App.context()).enqueue(backgroundRequests.periodicRequest)
        //val work = PeriodicWorkRequestBuilder<SyncWork>(15,TimeUnit.MINUTES).build()

        //WorkManager.getInstance().enqueueUniquePeriodicWork("TaskTag",
        ///    ExistingPeriodicWorkPolicy.KEEP, work);

        WorkManager.getInstance(App.context()).enqueueUniquePeriodicWork(
            WORKER_MOVIE_UPDATE_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            backgroundRequests.periodicRequest
        )

        setContentView(view)
    }


}