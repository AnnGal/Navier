package an.maguste.android.navier.mvvm

sealed class State {
    class Init : State()
    class Loading : State()
    class Error : State()
    class Success : State()
}