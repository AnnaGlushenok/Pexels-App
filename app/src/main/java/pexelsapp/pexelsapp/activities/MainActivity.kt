package pexelsapp.pexelsapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import pexelsapp.pexelsapp.R

class MainActivity : AppCompatActivity() {
    private lateinit var searchText: EditText
    private lateinit var clearButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val underline: View = findViewById(R.id.underline)
        val navController =
            (supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment).navController

        bottomNavigationView.setOnItemSelectedListener { item ->
            val part: Int = bottomNavigationView.width / 4
            val layoutParams = underline.layoutParams as ViewGroup.MarginLayoutParams
            when (item.itemId) {
                R.id.home_nav -> layoutParams.leftMargin = part + 20
                R.id.bookmark_nav -> layoutParams.leftMargin = part * 3 - 72
            }
            underline.layoutParams = layoutParams
            true
        }//8oNMTj6gLf4bARFR36MVJxryWmSQRuIUk7y8H2L7cnkrGCCJ1erx8C5c
        bottomNavigationView.setupWithNavController(navController)

        val customSearchView: View = layoutInflater.inflate(R.layout.search, null)
        searchText = customSearchView.findViewById(R.id.search_text)
        clearButton = customSearchView.findViewById(R.id.clear_button)
        Log.e("", searchText.text.toString())

        searchText.setOnFocusChangeListener { view, hasFocus ->
            Log.d("sdf", searchText.text.toString())
            if (hasFocus) {
                Log.d("Focus", "Focus")
                clearButton.visibility = View.VISIBLE
            } else {
                Log.d("Focus", "NoFocus")
                clearButton.visibility = View.INVISIBLE
            }
        }

//        val url = "https://api.pexels.com/v1/curated?per_page=1"
//        val apiKey = "8oNMTj6gLf4bARFR36MVJxryWmSQRuIUk7y8H2L7cnkrGCCJ1erx8C5c"
//        val baseUrl = "https://api.pexels.com/"
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl(baseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val apiService = retrofit.create(ApiService::class.java)
//
//        val call = apiService.getCuratedPhotos(apiKey, 3)
//        call.enqueue(object : Callback<SearchResult> {
//            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
//                if (response.isSuccessful) {
//                    val gson = Gson()
//                    val post = response.body()
//                    Log.d("Response", post.toString())
//                    Log.d("photos", post?.photos!![0].url)
//                    //  val res: SearchResult = gson.fromJson(post.toString(), SearchResult::class.java)
//                    Log.d("Response", post.toString())
//                } else {
//                    Log.e("Error", call.toString())
//                }
//            }
//
//            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
//                Log.e("Error", t.message.toString())
//            }
//        })
    }

    fun clear(view: View) {
        Log.e("", searchText.text.toString())
        searchText.text.clear()
    }
}