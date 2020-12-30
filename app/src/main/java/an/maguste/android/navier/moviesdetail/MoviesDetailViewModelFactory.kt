package an.maguste.android.navier.moviesdetail

import an.maguste.android.navier.data.Movie
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MoviesDetailViewModelFactory() : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MoviesDetailsViewModel::class.java -> MoviesDetailsViewModel()
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T

}
