package pexelsapp.pexelsapp.repositories

import pexelsapp.pexelsapp.APIs.API
import pexelsapp.pexelsapp.data.Photo
import pexelsapp.pexelsapp.db.PhotoDatabase

class PhotoRepo(val photoDb: PhotoDatabase) {
    suspend fun getPhotos(perPage: Int) =
        API.photoApi.getCuratedPhotos(perPage)

    suspend fun searchPhotos(query: String, perPage: Int) =
        API.photoApi.searchPhotos(query, perPage)

    suspend fun upsert(photo: Photo) = photoDb.getPhotoDAO().upsert(photo)

    fun getSavedPhotos() = photoDb.getPhotoDAO().getAllPhotos()

    suspend fun deletePhoto(photo: Photo) = photoDb.getPhotoDAO().deletePhoto(photo)
}