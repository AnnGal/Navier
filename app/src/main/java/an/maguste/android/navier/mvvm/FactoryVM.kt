package an.maguste.android.navier.mvvm

import an.maguste.android.navier.data.Movie
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

// Movies list
class MoviesListViewModelFactory(private val context: Context,
                                 private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        FragmentMoviesListVM::class.java -> FragmentMoviesListVM(context = context,
                dispatcher = dispatcher)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T

}

// Movies detail
class MoviesDetailViewModelFactory(private val movie: Movie) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        FragmentMoviesDetailsVM::class.java -> FragmentMoviesDetailsVM(movie,)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T

}
