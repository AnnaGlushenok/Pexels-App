package pexelsapp.pexelsapp.activities

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import pexelsapp.pexelsapp.R
import pexelsapp.pexelsapp.data.Photo
import pexelsapp.pexelsapp.db.PhotoDatabase
import pexelsapp.pexelsapp.repositories.FeaturedCollectionsRepo
import pexelsapp.pexelsapp.repositories.PhotoRepo
import pexelsapp.pexelsapp.viewModels.PhotoViewModel
import pexelsapp.pexelsapp.viewModels.PhotoViewModelFactory

class PictureActivity : AppCompatActivity() {
    lateinit var viewModel: PhotoViewModel
    private lateinit var bookmark: ImageButton
    private lateinit var picture: ImageView
    private lateinit var exploreTextButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        val photoRepo = PhotoRepo(PhotoDatabase(this))
        val featuredCollections = (FeaturedCollectionsRepo())
        val viewModelFactory = PhotoViewModelFactory(photoRepo, featuredCollections, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[PhotoViewModel::class.java]
        bookmark = findViewById(R.id.bookmark_btn)
        exploreTextButton = findViewById(R.id.explore_text_button)
        picture = findViewById(R.id.picture)

        val photo = intent.getSerializableExtra("photo") as Photo
        Glide.with(this)
            .load(photo.src.original)
            .placeholder(R.drawable.image_placeholder_icon)
            .into(picture)
        findViewById<TextView>(R.id.author).text = photo.photographer
        findViewById<LinearLayout>(R.id.download).setOnClickListener {
            download(photo)
        }
        findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE

        bookmark.setOnClickListener {
            viewModel.savePhoto(photo)
            bookmark.setImageResource(R.drawable.bookmark_icon_active)
            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
        }
        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        exploreTextButton.setOnClickListener {
            startActivity(Intent(this, HomeFragment::class.java))
        }
    }

    private fun download(photo: Photo) {
        try {
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val req = DownloadManager.Request(Uri.parse(photo.src.original))
            req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                .setMimeType("images/jpeg")
                .setAllowedOverRoaming(false)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(photo.photographer + " " + photo.alt)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    photo.photographer + " " + photo.alt + ".jpeg"
                )
            downloadManager.enqueue(req)
            Toast.makeText(this, "Downloaded", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e("err", e.message.toString())
            Toast.makeText(this, "Downloaded failed", Toast.LENGTH_LONG).show()
        }
    }
}