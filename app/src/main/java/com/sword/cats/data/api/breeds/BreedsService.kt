package com.sword.cats.data.api.breeds

import com.sword.cats.data.api.breeds.models.CatBreedDto
import retrofit2.Response
import retrofit2.http.GET

interface BreedsService {
    interface Api {
        @GET("v1/breeds")
        suspend fun search(): Response<List<CatBreedDto>>
    }

    suspend fun search(): Response<List<CatBreedDto>>
}

class BreedsServiceImpl(private val client: BreedsService.Api): BreedsService {
    override suspend fun search(): Response<List<CatBreedDto>> {
        return client.search()
    }
}