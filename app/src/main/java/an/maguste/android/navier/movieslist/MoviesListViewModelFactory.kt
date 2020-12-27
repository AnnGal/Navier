package an.maguste.android.navier.movieslist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MoviesListViewModelFactory(private val context: Context,
                                 private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MoviesListViewModel::class.java -> MoviesListViewModel(context = context,
            dispatcher = dispatcher)
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T

}