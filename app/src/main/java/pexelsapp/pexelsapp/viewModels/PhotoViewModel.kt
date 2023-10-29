package pexelsapp.pexelsapp.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pexelsapp.pexelsapp.PhotoApplication
import pexelsapp.pexelsapp.Resource
import pexelsapp.pexelsapp.data.FeaturedCollections
import pexelsapp.pexelsapp.data.Photo
import pexelsapp.pexelsapp.data.Photos
import pexelsapp.pexelsapp.repositories.FeaturedCollectionsRepo
import pexelsapp.pexelsapp.repositories.PhotoRepo
import retrofit2.Response
import java.io.IOException

class PhotoViewModel(
    private val photoRepo: PhotoRepo,
    private val featuredCollectionsRepo: FeaturedCollectionsRepo,
    app: Application
) :
    AndroidViewModel(app) {
    val photos: MutableLiveData<Resource<Photos>> = MutableLiveData()
    val searchPhotos: MutableLiveData<Resource<Photos>> = MutableLiveData()
    val featuredCollections: MutableLiveData<Resource<FeaturedCollections>> = MutableLiveData()
    private var photoPage = 1
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
        safeGetPhotos(COUNT_PHOTOS)
    }

    fun searchPhotos(query: String) = viewModelScope.launch {
        safeSearchPhotos(query, COUNT_PHOTOS)
    }

    private fun handlePhotosResponse(response: Response<Photos>): Resource<Photos> {
        if (response.isSuccessful) {
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchPhotosResponse(response: Response<Photos>): Resource<Photos> {
        if (response.isSuccessful) {
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleFeaturedCollectionsResponse(response: Response<FeaturedCollections>): Resource<FeaturedCollections> {
        if (response.isSuccessful) {
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }
    fun savePhoto(photo: Photo) = viewModelScope.launch {
        photoRepo.upsert(photo)
    }

    fun getSavedPhoto() = photoRepo.getSavedPhotos()

    fun deletePhoto(photo: Photo) = viewModelScope.launch {
        photoRepo.deletePhoto(photo)
    }

    private suspend fun safeSearchPhotos(query: String, countPhotos: Int) {
        searchPhotos.postValue(Resource.Loading())
        try {
            if (isInternetConnected()) {
                val response = photoRepo.searchPhotos(query, countPhotos)
                searchPhotos.postValue(handleSearchPhotosResponse(response))
            } else {
                searchPhotos.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchPhotos.postValue(Resource.Error("No internet connection"))
                else -> searchPhotos.postValue(Resource.Error("Err"))
            }
        }
    }

    private suspend fun safeGetPhotos(countPhotos: Int) {
        photos.postValue(Resource.Loading())
        try {
            if (isInternetConnected()) {
                val response = photoRepo.getPhotos(countPhotos)
                photos.postValue(handlePhotosResponse(response))
            } else {
                photos.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> photos.postValue(Resource.Error("No internet connection"))
                else -> photos.postValue(Resource.Error("Err"))
            }
        }
    }

    private suspend fun safeGetFeaturedCollections(countPhotos: Int) {
        featuredCollections.postValue(Resource.Loading())
        try {
            if (isInternetConnected()) {
                val response = featuredCollectionsRepo.getFeaturedCollections(countPhotos)
                featuredCollections.postValue(handleFeaturedCollectionsResponse(response))
            } else {
                featuredCollections.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> photos.postValue(Resource.Error("No internet connection"))
                else -> photos.postValue(Resource.Error("Err"))
            }
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