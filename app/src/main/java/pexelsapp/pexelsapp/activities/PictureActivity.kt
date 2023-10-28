package pexelsapp.pexelsapp.activities

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import pexelsapp.pexelsapp.Photo
import pexelsapp.pexelsapp.R
import java.io.File

class PictureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)
        val photo = intent.getSerializableExtra("photo") as Photo
        Glide.with(this)
            .load(photo.src.original)
            .into(findViewById(R.id.picture))
        findViewById<TextView>(R.id.author).text = photo.photographer

        findViewById<LinearLayout>(R.id.download).setOnClickListener {
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
}