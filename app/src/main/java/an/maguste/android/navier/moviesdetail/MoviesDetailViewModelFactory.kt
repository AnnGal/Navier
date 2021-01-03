package an.maguste.android.navier.moviesdetail

import an.maguste.android.navier.App
import an.maguste.android.navier.api.RetrofitService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MoviesDetailViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MoviesDetailsViewModel::class.java -> MoviesDetailsViewModel(apiService = RetrofitService.movieApi)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T

}
