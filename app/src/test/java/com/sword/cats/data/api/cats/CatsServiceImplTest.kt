package com.sword.cats.data.api.cats

import com.sword.cats.ModelFactory.fakeBreed
import com.sword.cats.data.api.models.CatDto
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
        val breeds = listOf(fakeBreed())
        val response = Response.success(breeds)

        coEvery { api.search() } returns response

        val result = service.search()

        assertEquals(response, result)
        assertEquals(breeds, result.body())

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
}