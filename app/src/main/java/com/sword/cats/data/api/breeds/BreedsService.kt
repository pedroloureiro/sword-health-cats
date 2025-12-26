package com.sword.cats.data.api.breeds

import com.sword.cats.data.api.breeds.models.CatBreedDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BreedsService {
    interface Api {
        @GET("v1/breeds")
        suspend fun search(
            @Query("q") searchQuery: String,
            @Query("attach_image") attachImage: Int? = 1
        ): Response<List<CatBreedDto>>
    }

    suspend fun search(searchQuery: String): Response<List<CatBreedDto>>
}

class BreedsServiceImpl(private val client: BreedsService.Api): BreedsService {
    override suspend fun search(searchQuery: String): Response<List<CatBreedDto>> {
        return client.search(searchQuery)
    }
}