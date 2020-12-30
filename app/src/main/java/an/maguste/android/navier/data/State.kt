package an.maguste.android.navier.data

sealed class State {
    object Init : State()
    object Loading : State()
    object Error : State()
    object Success : State()
}