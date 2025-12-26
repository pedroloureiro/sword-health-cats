package com.sword.cats.data.api.cats

import com.sword.cats.data.api.models.CatDto
import com.sword.cats.data.api.models.CatFavouriteDto
import com.sword.cats.data.api.models.FavouriteApiRequest
import com.sword.cats.data.api.models.FavouriteApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CatsService {
    interface Api {
        @GET("v1/breeds")
        suspend fun search(): Response<List<CatDto>>

        @GET("v1/favourites")
        suspend fun getFavourites(): Response<List<CatFavouriteDto>>

        @POST("v1/favourites")
        suspend fun markAsFavourite(@Body request: FavouriteApiRequest): Response<FavouriteApiResponse>

        @DELETE("v1/favourites/{favourite_id}")
        suspend fun unmarkAsFavourite(@Path("favourite_id") favouriteId: String): Response<Unit>
    }

    suspend fun search(): Response<List<CatDto>>
    suspend fun getFavourites(): Response<List<CatFavouriteDto>>
    suspend fun markAsFavourite(imageId: String): Response<FavouriteApiResponse>
    suspend fun unmarkAsFavourite(favouriteId: String): Response<Unit>
}

class CatsServiceImpl(private val client: CatsService.Api): CatsService {
    companion object {
        private const val SUB_ID = "my-user-1234"
    }

    override suspend fun search(): Response<List<CatDto>> {
        return client.search()
    }

    override suspend fun getFavourites(): Response<List<CatFavouriteDto>> {
        return client.getFavourites()
    }

    override suspend fun markAsFavourite(imageId: String): Response<FavouriteApiResponse> {
        val request = FavouriteApiRequest(imageId, subId = SUB_ID)
        return client.markAsFavourite(request)
    }

    override suspend fun unmarkAsFavourite(favouriteId: String): Response<Unit> {
        return client.unmarkAsFavourite(favouriteId)
    }
}