package pexelsapp.pexelsapp

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Photos(
    val total_results: Int,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val next_page: String?
)

@Entity(
    tableName = "photos"
)
data class Photo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    val photographer_url: String,
    val photographer_id: Int,
    val avg_color: String,
    val src: PhotoSource,
    val liked: Boolean,
    val alt: String
)

data class PhotoSource(
    val original: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val small: String,
    val portrait: String,
    val landscape: String,
    val tiny: String
)


