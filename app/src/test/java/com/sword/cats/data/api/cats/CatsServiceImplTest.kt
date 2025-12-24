package com.sword.cats.data.api.cats

import com.sword.cats.ModelFactory.buildCatDto
import com.sword.cats.ModelFactory.buildCatFavouriteDto
import com.sword.cats.ModelFactory.buildFavouriteApiRequest
import com.sword.cats.ModelFactory.buildFavouriteApiResponse
import com.sword.cats.data.api.models.CatDto
import com.sword.cats.data.api.models.CatFavouriteDto
import com.sword.cats.data.api.models.FavouriteApiResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CatsServiceImplTest {
    private lateinit var api: CatsService.Api
    private lateinit var service: CatsService

    @Before
    fun setup() {
        api = mockk()
        service = CatsServiceImpl(api)
    }

    @Test
    fun `search returns successful response from api`() = runTest {
        val catDtoList = listOf(buildCatDto())
        val response = Response.success(catDtoList)

        coEvery { api.search() } returns response

        val result = service.search()

        assertEquals(response, result)
        assertEquals(catDtoList, result.body())

        coVerify(exactly = 1) { api.search() }
    }

    @Test
    fun `search returns error response from api`() = runTest {
        val errorResponse = Response.error<List<CatDto>>(
            404,
            "Not Found".toResponseBody()
        )

        coEvery { api.search() } returns errorResponse

        val result = service.search()

        assertEquals(404, result.code())
        assertEquals(errorResponse, result)

        coVerify(exactly = 1) { api.search() }
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
    fun `setFavourite returns successful response from api`() = runTest {
        val apiRequest = buildFavouriteApiRequest()
        val apiResponse = buildFavouriteApiResponse()
        val response = Response.success(apiResponse)

        coEvery { api.setFavourite(apiRequest) } returns response

        val result = service.setFavourite(apiRequest)

        assertEquals(response, result)
        assertEquals(apiResponse, result.body())

        coVerify(exactly = 1) { api.setFavourite(apiRequest) }
    }

    @Test
    fun `setFavourite returns error response from api`() = runTest {
        val apiRequest = buildFavouriteApiRequest()
        val errorResponse = Response.error<FavouriteApiResponse>(
            404,
            "Not Found".toResponseBody()
        )

        coEvery { api.setFavourite(apiRequest) } returns errorResponse

        val result = service.setFavourite(apiRequest)

        assertEquals(404, result.code())
        assertEquals(errorResponse, result)

        coVerify(exactly = 1) { api.setFavourite(apiRequest) }
    }

    @Test
    fun `deleteFavourite returns successful response from api`() = runTest {
        val favouriteId = "232413577"
        val response = Response.success(Unit)

        coEvery { api.deleteFavourite(favouriteId) } returns response

        val result = service.deleteFavourite(favouriteId)

        assertEquals(response, result)

        coVerify(exactly = 1) { api.deleteFavourite(favouriteId) }
    }

    @Test
    fun `deleteFavourite returns error response from api`() = runTest {
        val favouriteId = "232413577"
        val errorResponse = Response.error<Unit>(
            404,
            "Not Found".toResponseBody()
        )

        coEvery { api.deleteFavourite(favouriteId) } returns errorResponse

        val result = service.deleteFavourite(favouriteId)

        assertEquals(404, result.code())
        assertEquals(errorResponse, result)

        coVerify(exactly = 1) { api.deleteFavourite(favouriteId) }
    }
}