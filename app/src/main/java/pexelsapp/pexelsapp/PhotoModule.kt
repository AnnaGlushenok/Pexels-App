package pexelsapp.pexelsapp

import javax.inject.Inject

abstract class PhotoModule {
    @Inject
    abstract fun contributeMainActivity()
}