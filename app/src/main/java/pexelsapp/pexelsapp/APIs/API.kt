package pexelsapp.pexelsapp.APIs

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class API {
    companion object {
        val API_KEY = "8oNMTj6gLf4bARFR36MVJxryWmSQRuIUk7y8H2L7cnkrGCCJ1erx8C5c"
        private val URL = "https://api.pexels.com/"
        private lateinit var context: Context

        fun init(context: Context) {
            this.context = context
        }

        private val retrofit by lazy {
            val cacheSize = (10 * 1024 * 1024).toLong()
            val cache = Cache(context.cacheDir, cacheSize)

            val client = OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor { chain ->
                    var request = chain.request()

                    if (!isInternetConnected()) {
                        request = request.newBuilder()
                            .header(
                                "Cache-Control",
                                "public, only-if-cached, max-stale=" + 3600
                            )
                            .build()
                    }

                    val response = chain.proceed(request)
                    if (isInternetConnected()) {
                        response
                    } else {
                        response.newBuilder()
                            .header("Cache-Control", "public, max-age=3600")
                            .build()
                    }
                }
                .build()

            Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val photoApi by lazy {
            retrofit.create(PhotoApi::class.java)
        }

        val featuredCollectionsApi by lazy {
            retrofit.create(FeaturedCollectionsAPI::class.java)
        }

        private fun isInternetConnected(): Boolean {
            val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    else -> false
                }
            }
            return false
        }
    }
}