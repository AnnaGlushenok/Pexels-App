package pexelsapp.pexelsapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response

class PhotoViewModel(private val photoRepo: PhotoRepo) : ViewModel() {
    val photos: MutableLiveData<Resource<Photos>> = MutableLiveData()
    var photoPage = 1

    init {
        getPhotos(10)
    }

    fun getPhotos(perPage: Int) = viewModelScope.launch {
        photos.postValue(Resource.Loading())
        val response = photoRepo.getPhotos(perPage)
        photos.postValue(handleResponse(response))
    }

    private fun handleResponse(response: Response<Photos>): Resource<Photos> {
        if (response.isSuccessful) {
            response.body()?.let { res ->
                return Resource.Success(res)
            }
        }
        return Resource.Error(response.message())
    }
//        private var photos = MutableLiveData<List<Photo>>()
//    val photos: LiveData<Photos>
//        get() = photoRepo.photos
//
//    fun getPopularPhotos() {
//        val retrofit = API.getAPI()
//
//        val apiService = retrofit.create(PhotoApi::class.java)
//
//        apiService.getCuratedPhotos(API.API_KEY, 3)
//            .enqueue(object : Callback<Photos> {
//                override fun onResponse(call: Call<Photos>, response: Response<Photos>) {
//                    if (response.isSuccessful) {
//                        val post = response.body()
//                        photos.value = response.body()!!.photos
//                        Log.d("Response", post.toString())
//                    } else {
//                        Log.e("Error", call.toString())
//                    }
//                }
//
//                override fun onFailure(call: Call<Photos>, t: Throwable) {
//                    Log.e("Error", t.message.toString())
//                }
//            })
//    }
//
//    fun observePhotos(): LiveData<List<Photo>> {
//        return photos
//    }
}