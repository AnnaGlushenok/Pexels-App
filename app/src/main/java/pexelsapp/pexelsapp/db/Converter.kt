package pexelsapp.pexelsapp.db

import androidx.room.TypeConverter
import pexelsapp.pexelsapp.PhotoSource

// TODO
class Converter {
    @TypeConverter
    fun fromPhotoSource(photoSource: PhotoSource): String {
        return photoSource.large
    }

    @TypeConverter
    fun toPhotoSource(str: String): PhotoSource {
        return PhotoSource(str, str, str, str, str, str, str, str)
    }
}