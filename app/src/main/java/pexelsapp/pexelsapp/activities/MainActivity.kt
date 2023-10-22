package pexelsapp.pexelsapp.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import pexelsapp.pexelsapp.R

class MainActivity : AppCompatActivity() {
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
        }
        bottomNavigationView.setupWithNavController(navController)
    }
}