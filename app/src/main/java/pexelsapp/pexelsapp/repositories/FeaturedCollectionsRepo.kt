package pexelsapp.pexelsapp.repositories

import pexelsapp.pexelsapp.APIs.API

class FeaturedCollectionsRepo {
    suspend fun getFeaturedCollections(perPage: Int) =
        API.featuredCollectionsApi.getFeaturedCollections(perPage)
}