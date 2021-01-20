package an.maguste.android.navier

import an.maguste.android.navier.storage.MoviesRepositoryImpl
import android.app.Application
import android.content.Context

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        private var context: Context? = null
        fun context(): Context = context ?: throw IllegalStateException()

        private val repository by lazy { MoviesRepositoryImpl() }
        fun repository(): MoviesRepositoryImpl = repository
    }
}