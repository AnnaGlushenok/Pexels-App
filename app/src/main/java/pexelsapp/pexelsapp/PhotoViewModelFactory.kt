package pexelsapp.pexelsapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PhotoViewModelFactory(private val photoRepo: PhotoRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PhotoViewModel(photoRepo) as T
    }
}