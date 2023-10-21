package pexelsapp.pexelsapp.activities

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import pexelsapp.pexelsapp.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val logo: View = findViewById(R.id.splash)
        logo.alpha = 1F
        val animator = ObjectAnimator.ofFloat(logo, "alpha", 0.5f)
        animator.duration = 2000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
        animator.start()

        Handler().postDelayed(
            {
                startActivity(Intent(this, MainActivity::class.java))
            }, 5000
        )
    }
}