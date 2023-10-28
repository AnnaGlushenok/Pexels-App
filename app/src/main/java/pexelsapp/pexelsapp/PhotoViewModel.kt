package pexelsapp.pexelsapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response

class PhotoViewModel(private val photoRepo: PhotoRepo) : ViewModel() {
    val photos: MutableLiveData<Resource<Photos>> = MutableLiveData()
    val searchPhotos: MutableLiveData<Resource<Photos>> = MutableLiveData()
    private var photoPage = 1
    private val countPhotos = 6

    init {
        getPhotos()
    }

    fun getPhotos() = viewModelScope.launch {
        photos.postValue(Resource.Loading())
        val response = photoRepo.getPhotos(countPhotos)
        photos.postValue(handlePhotosResponse(response))
    }

    fun searchPhotos(query: String) = viewModelScope.launch {
        searchPhotos.postValue((Resource.Loading()))
        val response = photoRepo.searchPhotos(query, countPhotos)
        photos.postValue(handleSearchPhotosResponse(response))
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

    fun savePhoto(photo: Photo) = viewModelScope.launch {
        photoRepo.upsert(photo)
    }

    fun getSavedPhoto() = photoRepo.getSavedPhotos()

    fun deletePhoto(photo: Photo) = viewModelScope.launch {
        photoRepo.deletePhoto(photo)
    }
}