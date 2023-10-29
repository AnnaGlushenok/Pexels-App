package pexelsapp.pexelsapp.APIs

import pexelsapp.pexelsapp.data.Photos
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PhotoApi {
    @GET("v1/curated")
    suspend fun getCuratedPhotos(
        @Query("per_page") perPage: Int,
        @Header("Authorization") apiKey: String = API.API_KEY
    ): Response<Photos>

    @GET("v1/search")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("per_page") perPage: Int,
        @Header("Authorization") apiKey: String = API.API_KEY
    ): Response<Photos>
}

