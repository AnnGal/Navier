package an.maguste.android.navier.moviesdetail

import an.maguste.android.navier.data.Movie
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MoviesDetailViewModelFactory(private val movie: Movie) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MoviesDetailsViewModel::class.java -> MoviesDetailsViewModel(movie,)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T

}
