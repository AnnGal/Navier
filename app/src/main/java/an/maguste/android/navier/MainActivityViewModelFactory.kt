package an.maguste.android.navier

import an.maguste.android.navier.api.RetrofitHolder
import an.maguste.android.navier.movieslist.MoviesListViewModel
import an.maguste.android.navier.storage.repository.RepositoryHolder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.create

class MainActivityViewModelFactory : ViewModelProvider.Factory {

    @ExperimentalSerializationApi
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MainActivityViewModel::class.java -> MainActivityViewModel(
            repository = RepositoryHolder.moviesRepository()
        )
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T

}
