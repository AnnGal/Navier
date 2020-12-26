package an.maguste.android.navier.mvvm

import an.maguste.android.navier.data.Movie
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MoviesDetailsViewModel(private val movie: Movie) : ViewModel() {

    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie> get() = _selectedMovie

    fun setMovie(){
        _selectedMovie.value = movie
    }
}