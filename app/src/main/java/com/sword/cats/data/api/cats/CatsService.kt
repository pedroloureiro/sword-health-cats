package com.sword.cats.data.api.cats

import com.sword.cats.data.api.models.CatDto
import com.sword.cats.data.api.models.CatFavouriteDto
import com.sword.cats.data.api.models.FavouriteApiRequest
import com.sword.cats.data.api.models.FavouriteApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CatsService {
    interface Api {
        @GET("v1/breeds")
        suspend fun search(): Response<List<CatDto>>

        @GET("v1/favourites")
        suspend fun getFavourites(): Response<List<CatFavouriteDto>>

        @POST("v1/favourites")
        suspend fun setFavourite(@Body request: FavouriteApiRequest): Response<FavouriteApiResponse>
    }

    suspend fun search(): Response<List<CatDto>>
    suspend fun getFavourites(): Response<List<CatFavouriteDto>>
    suspend fun setFavourite(request: FavouriteApiRequest): Response<FavouriteApiResponse>
}

class CatsServiceImpl(private val client: CatsService.Api): CatsService {
    override suspend fun search(): Response<List<CatDto>> {
        return client.search()
    }

    override suspend fun getFavourites(): Response<List<CatFavouriteDto>> {
        return client.getFavourites()
    }

    override suspend fun setFavourite(request: FavouriteApiRequest): Response<FavouriteApiResponse> {
        return client.setFavourite(request)
    }
}