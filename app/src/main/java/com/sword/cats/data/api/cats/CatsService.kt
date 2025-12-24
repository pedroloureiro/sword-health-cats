package com.sword.cats.data.api.cats

import com.sword.cats.data.api.models.CatDto
import retrofit2.Response
import retrofit2.http.GET

interface CatsService {
    interface Api {
        @GET("v1/breeds")
        suspend fun search(): Response<List<CatDto>>
    }

    suspend fun search(): Response<List<CatDto>>
}

class CatsServiceImpl(private val client: CatsService.Api): CatsService {
    override suspend fun search(): Response<List<CatDto>> {
        return client.search()
    }
}