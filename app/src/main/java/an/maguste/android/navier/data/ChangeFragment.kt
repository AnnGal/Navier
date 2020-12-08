package an.maguste.android.navier.data

import an.maguste.android.navier.data.Movie

interface ChangeFragment {
    fun toMovieDetail(movie: Movie)
    fun toMoviesList()
}