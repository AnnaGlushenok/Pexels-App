package pexelsapp.pexelsapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PhotoApi {
    @GET("v1/curated")
    suspend fun getCuratedPhotos(
        @Header("Authorization")
        apiKey: String ,
        @Query("per_page") perPage: Int
    ): Response<Photos>
}

