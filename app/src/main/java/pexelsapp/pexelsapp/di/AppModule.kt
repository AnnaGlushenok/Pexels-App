package pexelsapp.pexelsapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pexelsapp.pexelsapp.APIs.PhotoApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideBaseURL() = "8oNMTj6gLf4bARFR36MVJxryWmSQRuIUk7y8H2L7cnkrGCCJ1erx8C5c"

    @Provides
    @Singleton
    fun provideRetrofitInstance(baseURL: String): PhotoApi =
        Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PhotoApi::class.java)

}