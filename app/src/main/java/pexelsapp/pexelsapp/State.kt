package pexelsapp.pexelsapp

sealed class State<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T, message: String? = null) : State<T>(data, message)
    class Error<T>(message: String, data: T? = null) : State<T>(data, message)
    class Loading<T> : State<T>()
}