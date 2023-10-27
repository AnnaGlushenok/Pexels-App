package pexelsapp.pexelsapp

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class API {
    companion object {
        val API_KEY = "8oNMTj6gLf4bARFR36MVJxryWmSQRuIUk7y8H2L7cnkrGCCJ1erx8C5c"
        private val URL = "https://api.pexels.com/"

        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api by lazy {
            retrofit.create(PhotoApi::class.java)
        }
    }
}