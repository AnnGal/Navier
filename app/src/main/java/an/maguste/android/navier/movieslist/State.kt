package an.maguste.android.navier.movieslist

sealed class State {
    object Init : State()
    object Loading : State()
    object Error : State()
    object EmptyDataSet : State()
    object Success : State()
}