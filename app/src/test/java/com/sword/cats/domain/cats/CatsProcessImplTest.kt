package com.sword.cats.domain.cats

import com.sword.cats.ModelFactory.fakeBreed
import com.sword.cats.data.api.cats.CatsService
import com.sword.cats.data.api.models.CatDto
import com.sword.cats.data.database.CatDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CatsProcessImplTest {

    private lateinit var service: CatsService
    private lateinit var process: CatsProcess
    private lateinit var catDao: CatDao

    @Before
    fun setup() {
        service = mockk()
        catDao = mockk()
        process = CatsProcessImpl(service, catDao)
    }

    @Test
    fun `search returns success result when service response is successful`() = runTest {
        val breeds = listOf(fakeBreed())
        val response = Response.success(breeds)

        coEvery { service.search() } returns response

        val result = process.search()

        assertTrue(result.isSuccess)
        assertEquals(breeds, result.getOrNull())

        coVerify(exactly = 1) { service.search() }
    }

    @Test
    fun `search returns failure result when service response is error`() = runTest {
        val response = Response.error<List<CatDto>>(
            500,
            "Internal error".toResponseBody()
        )

        coEvery { service.search() } returns response

        val result = process.search()

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())

        coVerify(exactly = 1) { service.search() }
    }
}