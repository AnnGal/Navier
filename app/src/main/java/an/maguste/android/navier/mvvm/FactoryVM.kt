package an.maguste.android.navier.mvvm

import an.maguste.android.navier.FragmentMoviesDetails
import an.maguste.android.navier.FragmentMoviesList
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MoviesViewModelFactory(private val context: Context,
                             private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        FragmentMoviesDetailsVM::class.java -> FragmentMoviesDetailsVM()
        FragmentMoviesListVM::class.java -> FragmentMoviesListVM(context = context,
                dispatcher = dispatcher)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T

}
