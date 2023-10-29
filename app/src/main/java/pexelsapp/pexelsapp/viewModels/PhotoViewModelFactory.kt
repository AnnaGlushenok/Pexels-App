package pexelsapp.pexelsapp.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pexelsapp.pexelsapp.repositories.FeaturedCollectionsRepo
import pexelsapp.pexelsapp.repositories.PhotoRepo

class PhotoViewModelFactory(
    private val photoRepo: PhotoRepo,
    private val featuredCollectionsRepo: FeaturedCollectionsRepo,
    private val app: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PhotoViewModel(photoRepo, featuredCollectionsRepo, app) as T
    }
}