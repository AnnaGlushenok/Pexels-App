package pexelsapp.pexelsapp

import pexelsapp.pexelsapp.db.PhotoDatabase

class PhotoRepo(val photoDb: PhotoDatabase) {
    suspend fun getPhotos(perPage: Int) =
        API.api.getCuratedPhotos(API.API_KEY, perPage)
}