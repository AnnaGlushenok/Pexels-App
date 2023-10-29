package pexelsapp.pexelsapp.data

data class FeaturedCollections(
    val collections: List<Collection>,
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val prev_page: String,
    val total_results: Int
)

data class Collection(
    val description: Any,
    val id: String,
    val media_count: Int,
    val photos_count: Int,
    val `private`: Boolean,
    val title: String,
    val videos_count: Int
)