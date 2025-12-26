package com.sword.cats.data.api.breeds

import com.sword.cats.ModelFactory.buildCatDto
import com.sword.cats.data.api.breeds.models.CatBreedDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class BreedsServiceImplTest {
    private lateinit var api: BreedsService.Api
    private lateinit var service: BreedsService

    @Before
    fun setup() {
        api = mockk()
        service = BreedsServiceImpl(api)
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
        val errorResponse = Response.error<List<CatBreedDto>>(
            404,
            "Not Found".toResponseBody()
        )

        coEvery { api.search() } returns errorResponse

        val result = service.search()

        assertEquals(404, result.code())
        assertEquals(errorResponse, result)

        coVerify(exactly = 1) { api.search() }
    }
}