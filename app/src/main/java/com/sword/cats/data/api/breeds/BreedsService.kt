package com.sword.cats.data.api.breeds

import retrofit2.Response
import retrofit2.http.GET

interface BreedsService {
    interface Api {
        @GET("v1/breeds")
        suspend fun search(): Response<List<Breed>>
    }

    suspend fun search(): Response<List<Breed>>
}

class BreedsServiceImpl(private val client: BreedsService.Api): BreedsService {
    override suspend fun search(): Response<List<Breed>> {
        return client.search()
    }
}

data class Breed(
    val weight: Weight,
    val id: String,
    val name: String,
    val cfa_url: String? = null,
    val vetstreet_url: String? = null,
    val vcahospitals_url: String? = null,
    val temperament: String,
    val origin: String,
    val country_codes: String,
    val country_code: String,
    val description: String,
    val life_span: String,
    val indoor: Int,
    val lap: Int? = null,
    val alt_names: String? = null,
    val adaptability: Int,
    val affection_level: Int,
    val child_friendly: Int,
    val dog_friendly: Int,
    val energy_level: Int,
    val grooming: Int,
    val health_issues: Int,
    val intelligence: Int,
    val shedding_level: Int,
    val social_needs: Int,
    val stranger_friendly: Int,
    val vocalisation: Int,
    val experimental: Int,
    val hairless: Int,
    val natural: Int,
    val rare: Int,
    val rex: Int,
    val suppressed_tail: Int,
    val short_legs: Int,
    val wikipedia_url: String? = null,
    val hypoallergenic: Int,
    val reference_image_id: String? = null,
    val image: Image? = null
)

data class Weight(
    val imperial: String,
    val metric: String
)

data class Image(
    val id: String,
    val width: Int,
    val height: Int,
    val url: String
)