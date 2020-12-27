package an.maguste.android.navier.movieslist

import an.maguste.android.navier.App
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MoviesListViewModelFactory() : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
        MoviesListViewModel::class.java -> MoviesListViewModel(context = App.context())
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel")
    } as T

}

