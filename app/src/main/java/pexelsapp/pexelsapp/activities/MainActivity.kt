package pexelsapp.pexelsapp.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import pexelsapp.pexelsapp.PhotoRepo
import pexelsapp.pexelsapp.PhotoViewModel
import pexelsapp.pexelsapp.PhotoViewModelFactory
import pexelsapp.pexelsapp.R
import pexelsapp.pexelsapp.db.PhotoDatabase

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: PhotoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val photoRepo = PhotoRepo(PhotoDatabase(this))
        val viewModelFactory = PhotoViewModelFactory(photoRepo)
        viewModel = ViewModelProvider(this, viewModelFactory)[PhotoViewModel::class.java]
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
    }
}