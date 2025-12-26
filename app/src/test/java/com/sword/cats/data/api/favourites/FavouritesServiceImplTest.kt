package com.sword.cats.data.api.favourites

import com.sword.cats.ModelFactory.CAT_IMAGE_ID
import com.sword.cats.ModelFactory.buildCatFavouriteDto
import com.sword.cats.ModelFactory.buildFavouriteApiRequest
import com.sword.cats.ModelFactory.buildFavouriteApiResponse
import com.sword.cats.data.api.favourites.models.CatFavouriteDto
import com.sword.cats.data.api.favourites.models.CatFavouriteResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class FavouritesServiceImplTest {
    private lateinit var api: FavouritesService.Api
    private lateinit var service: FavouritesService

    @Before
    fun setup() {
        api = mockk()
        service = FavouritesServiceImpl(api)
    }

    @Test
    fun `getFavourites returns successful response from api`() = runTest {
        val catFavouriteList = listOf(buildCatFavouriteDto())
        val response = Response.success(catFavouriteList)

        coEvery { api.getFavourites() } returns response

        val result = service.getFavourites()

        assertEquals(response, result)
        assertEquals(catFavouriteList, result.body())

        coVerify(exactly = 1) { api.getFavourites() }
    }

    @Test
    fun `getFavourites returns error response from api`() = runTest {
        val errorResponse = Response.error<List<CatFavouriteDto>>(
            404,
            "Not Found".toResponseBody()
        )

        coEvery { api.getFavourites() } returns errorResponse

        val result = service.getFavourites()

        assertEquals(404, result.code())
        assertEquals(errorResponse, result)

        coVerify(exactly = 1) { api.getFavourites() }
    }

    @Test
    fun `markAsFavourite returns successful response from api`() = runTest {
        val apiRequest = buildFavouriteApiRequest()
        val apiResponse = buildFavouriteApiResponse()
        val response = Response.success(apiResponse)

        coEvery { api.markAsFavourite(apiRequest) } returns response

        val result = service.markAsFavourite(CAT_IMAGE_ID)

        assertEquals(response, result)
        assertEquals(apiResponse, result.body())

        coVerify(exactly = 1) { api.markAsFavourite(apiRequest) }
    }

    @Test
    fun `markAsFavourite returns error response from api`() = runTest {
        val apiRequest = buildFavouriteApiRequest()
        val errorResponse = Response.error<CatFavouriteResponse>(
            404,
            "Not Found".toResponseBody()
        )

        coEvery { api.markAsFavourite(apiRequest) } returns errorResponse

        val result = service.markAsFavourite(CAT_IMAGE_ID)

        assertEquals(404, result.code())
        assertEquals(errorResponse, result)

        coVerify(exactly = 1) { api.markAsFavourite(apiRequest) }
    }

    @Test
    fun `unmarkAsFavourite returns successful response from api`() = runTest {
        val favouriteId = "232413577"
        val response = Response.success(Unit)

        coEvery { api.unmarkAsFavourite(favouriteId) } returns response

        val result = service.unmarkAsFavourite(favouriteId)

        assertEquals(response, result)

        coVerify(exactly = 1) { api.unmarkAsFavourite(favouriteId) }
    }

    @Test
    fun `unmarkAsFavourite returns error response from api`() = runTest {
        val favouriteId = "232413577"
        val errorResponse = Response.error<Unit>(
            404,
            "Not Found".toResponseBody()
        )

        coEvery { api.unmarkAsFavourite(favouriteId) } returns errorResponse

        val result = service.unmarkAsFavourite(favouriteId)

        assertEquals(404, result.code())
        assertEquals(errorResponse, result)

        coVerify(exactly = 1) { api.unmarkAsFavourite(favouriteId) }
    }
}