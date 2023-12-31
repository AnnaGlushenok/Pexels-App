package pexelsapp.pexelsapp.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import pexelsapp.pexelsapp.R
import pexelsapp.pexelsapp.databinding.ActivityMainBinding
import pexelsapp.pexelsapp.db.PhotoDatabase
import pexelsapp.pexelsapp.repositories.FeaturedCollectionsRepo
import pexelsapp.pexelsapp.repositories.PhotoRepo
import pexelsapp.pexelsapp.viewModels.PhotoViewModel
import pexelsapp.pexelsapp.viewModels.PhotoViewModelFactory

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var viewModel: PhotoViewModel
    lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        val photoRepo = PhotoRepo(PhotoDatabase(this))
        val featuredCollections = FeaturedCollectionsRepo()
        val viewModelFactory = PhotoViewModelFactory(photoRepo, featuredCollections, application)
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
        }
        bottomNavigationView.setupWithNavController(navController)
    }
}