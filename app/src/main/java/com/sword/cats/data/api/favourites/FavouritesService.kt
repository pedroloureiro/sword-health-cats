package com.sword.cats.data.api.favourites

import com.sword.cats.data.api.favourites.models.CatFavouriteDto
import com.sword.cats.data.api.favourites.models.CatFavouriteRequest
import com.sword.cats.data.api.favourites.models.CatFavouriteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FavouritesService {
    interface Api {
        @GET("v1/favourites")
        suspend fun getFavourites(): Response<List<CatFavouriteDto>>

        @POST("v1/favourites")
        suspend fun markAsFavourite(@Body request: CatFavouriteRequest): Response<CatFavouriteResponse>

        @DELETE("v1/favourites/{favourite_id}")
        suspend fun unmarkAsFavourite(@Path("favourite_id") favouriteId: String): Response<Unit>
    }

    suspend fun getFavourites(): Response<List<CatFavouriteDto>>
    suspend fun markAsFavourite(imageId: String): Response<CatFavouriteResponse>
    suspend fun unmarkAsFavourite(favouriteId: String): Response<Unit>
}

class FavouritesServiceImpl(private val client: FavouritesService.Api): FavouritesService {
    companion object {
        private const val SUB_ID = "my-user-1234"
    }

    override suspend fun getFavourites(): Response<List<CatFavouriteDto>> {
        return client.getFavourites()
    }

    override suspend fun markAsFavourite(imageId: String): Response<CatFavouriteResponse> {
        val request = CatFavouriteRequest(imageId, subId = SUB_ID)
        return client.markAsFavourite(request)
    }

    override suspend fun unmarkAsFavourite(favouriteId: String): Response<Unit> {
        return client.unmarkAsFavourite(favouriteId)
    }
}