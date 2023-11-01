package pexelsapp.pexelsapp.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pexelsapp.pexelsapp.Error
import pexelsapp.pexelsapp.PhotoApplication
import pexelsapp.pexelsapp.State
import pexelsapp.pexelsapp.data.FeaturedCollections
import pexelsapp.pexelsapp.data.Photo
import pexelsapp.pexelsapp.data.Photos
import pexelsapp.pexelsapp.repositories.FeaturedCollectionsRepo
import pexelsapp.pexelsapp.repositories.PhotoRepo
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val photoRepo: PhotoRepo,
    private val featuredCollectionsRepo: FeaturedCollectionsRepo,
    app: Application
) :
    AndroidViewModel(app) {
    val photos: MutableLiveData<State<Photos>> = MutableLiveData()
    val searchPhotos: MutableLiveData<State<Photos>> = MutableLiveData()
    val featuredCollections: MutableLiveData<State<FeaturedCollections>> =
        MutableLiveData()
    private var _isLoadingProgressBar = MutableLiveData<Boolean>()
    val isLoadingProgressBar: LiveData<Boolean>
        get() = _isLoadingProgressBar

    private val COUNT_PHOTOS = 6
    private val COUNT_COLLECTIONS = 7

    init {
        getPhotos()
        getFeaturedCollections()
    }

    fun getFeaturedCollections() = viewModelScope.launch {
        safeGetFeaturedCollections(COUNT_COLLECTIONS)
    }

    fun getPhotos() = viewModelScope.launch {
        _isLoadingProgressBar.value = false
        _isLoadingProgressBar.value = true
        safeGetPhotos(COUNT_PHOTOS)
    }

    fun searchPhotos(query: String) = viewModelScope.launch {
        _isLoadingProgressBar.value = false
        _isLoadingProgressBar.value = true
        safeSearchPhotos(query, COUNT_PHOTOS)
    }

    private fun handlePhotosResponse(
        response: Response<Photos>,
        message: String? = null
    ): State<Photos> = handleResponse(response, message)

    private fun handleSearchPhotosResponse(
        response: Response<Photos>,
        message: String? = null
    ): State<Photos> = handleResponse(response, message)

    private fun <T> handleResponse(
        response: Response<T>,
        message: String? = null
    ): State<T> {
        if (response.isSuccessful) {
            response.body()?.let { res ->
                if (message != null)
                    return State.Success(res, message)
                return State.Success(res)
            }
        }
        return State.Error(response.message())
    }

    private fun handleFeaturedCollectionsResponse(response: Response<FeaturedCollections>): State<FeaturedCollections> =
        handleResponse(response)

    fun savePhoto(photo: Photo) = viewModelScope.launch {
        photoRepo.upsert(photo)
    }

    fun getSavedPhoto() = photoRepo.getSavedPhotos()

    fun deletePhoto(photo: Photo) = viewModelScope.launch {
        photoRepo.deletePhoto(photo)
    }

    private suspend fun safeSearchPhotos(query: String, countPhotos: Int) {
        searchPhotos.postValue(State.Loading())
        safeGetPhotos(searchPhotos, photoRepo.searchPhotos(query, countPhotos))
    }

    private suspend fun safeGetPhotos(countPhotos: Int) {
        photos.postValue(State.Loading())
        safeGetPhotos(photos, photoRepo.getPhotos(countPhotos))
    }

    private fun safeGetPhotos(photos: MutableLiveData<State<Photos>>, response: Response<Photos>) {
        try {
            if ((response.body() == null) and !isInternetConnected())
                photos.postValue(State.Error(Error.NO_INTERNET_CONNECTION.name))
            else if (isInternetConnected() and response.body()?.photos!!.isNotEmpty())
                photos.postValue(handlePhotosResponse(response))
            else if (isInternetConnected() and response.body()?.photos!!.isEmpty())
                photos.postValue(State.Error(Error.NO_DATA.name))
            else if ((response.body() != null) and !isInternetConnected())
                photos.postValue(
                    handleSearchPhotosResponse(
                        response,
                        Error.NO_INTERNET_CONNECTION.name
                    )
                )
        } catch (e: IOException) {
            photos.postValue(State.Error(e.message.toString()))
        }
    }

    private suspend fun safeGetFeaturedCollections(countPhotos: Int) {
        featuredCollections.postValue(State.Loading())
        try {
            if (isInternetConnected()) {
                featuredCollections.postValue(
                    handleFeaturedCollectionsResponse(
                        featuredCollectionsRepo.getFeaturedCollections(countPhotos)
                    )
                )
            } else {
                featuredCollections.postValue(State.Error(Error.NO_INTERNET_CONNECTION.name))
            }
        } catch (e: IOException) {
            photos.postValue(State.Error(e.message.toString()))
        }
    }

    private fun isInternetConnected(): Boolean {
        val connectivityManager = getApplication<PhotoApplication>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.activeNetworkInfo?.run {
            return when (type) {
                TYPE_WIFI -> true
                TYPE_MOBILE -> true
                else -> false
            }
        }
        return false
    }
}