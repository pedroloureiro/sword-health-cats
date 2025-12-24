package com.sword.cats.domain.cats

import com.sword.cats.ModelFactory
import com.sword.cats.ModelFactory.buildCatDto
import com.sword.cats.data.api.cats.CatsService
import com.sword.cats.data.api.models.CatDto
import com.sword.cats.data.api.models.CatFavouriteDto
import com.sword.cats.data.api.models.FavouriteApiResponse
import com.sword.cats.data.database.CatDao
import com.sword.cats.data.database.CatEntity
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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
        val breeds = listOf(buildCatDto())
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

    @Test
    fun `save calls catDao insertCats`() = runTest {
        val cats = listOf(ModelFactory.buildCatEntity())

        coEvery { catDao.insertCats(cats) } just Runs

        process.save(cats)

        coVerify(exactly = 1) { catDao.insertCats(cats) }
    }

    @Test
    fun `searchDb returns success result when dao returns data`() = runTest {
        val cats = listOf(ModelFactory.buildCatEntity())
        val flow = flowOf(cats)

        every { catDao.getAllCats() } returns flow

        val result = process.searchDb()

        assertTrue(result.isSuccess)
        assertEquals(cats, result.getOrNull())

        verify(exactly = 1) { catDao.getAllCats() }
    }

    @Test
    fun `searchDb returns failure result when dao returns error`() = runTest {
        val exception = RuntimeException("Database error")
        val flow = flow<List<CatEntity>> { throw exception }

        coEvery { catDao.getAllCats() } returns flow

        val result = process.searchDb()

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())

        coVerify(exactly = 1) { catDao.getAllCats() }
    }

    @Test
    fun `getFavourites returns success result when service response is successful`() = runTest {
        val favourites = listOf(ModelFactory.buildCatFavouriteDto())
        val response = Response.success(favourites)

        coEvery { service.getFavourites() } returns response

        val result = process.getFavourites()

        assertTrue(result.isSuccess)
        assertEquals(favourites, result.getOrNull())

        coVerify(exactly = 1) { service.getFavourites() }
    }

    @Test
    fun `getFavourites returns failure result when service response is error`() = runTest {
        val response = Response.error<List<CatFavouriteDto>>(500, "Internal error".toResponseBody())

        coEvery { service.getFavourites() } returns response

        val result = process.getFavourites()

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())

        coVerify(exactly = 1) { service.getFavourites() }
    }

    @Test
    fun `setFavourite returns success result when service response is successful`() = runTest {
        val request = ModelFactory.buildFavouriteApiRequest()
        val responseBody = ModelFactory.buildFavouriteApiResponse()
        val response = Response.success(responseBody)

        coEvery { service.setFavourite(request) } returns response

        val result = process.setFavourite(request)

        assertTrue(result.isSuccess)
        assertEquals(responseBody, result.getOrNull())

        coVerify(exactly = 1) { service.setFavourite(request) }
    }

    @Test
    fun `setFavourite returns failure result when service response is error`() = runTest {
        val request = ModelFactory.buildFavouriteApiRequest()
        val response = Response.error<FavouriteApiResponse>(500, "Internal error".toResponseBody())

        coEvery { service.setFavourite(request) } returns response

        val result = process.setFavourite(request)

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())

        coVerify(exactly = 1) { service.setFavourite(request) }
    }

    @Test
    fun `deleteFavourite returns success result when service response is successful`() = runTest {
        val favouriteId = "123"
        val response = Response.success(Unit)

        coEvery { service.deleteFavourite(favouriteId) } returns response

        val result = process.deleteFavourite(favouriteId)

        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())

        coVerify(exactly = 1) { service.deleteFavourite(favouriteId) }
    }

    @Test
    fun `deleteFavourite returns failure result when service response is error`() = runTest {
        val favouriteId = "123"
        val response = Response.error<Unit>(500, "Internal error".toResponseBody())

        coEvery { service.deleteFavourite(favouriteId) } returns response

        val result = process.deleteFavourite(favouriteId)

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())

        coVerify(exactly = 1) { service.deleteFavourite(favouriteId) }
    }
}