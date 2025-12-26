package com.sword.cats.domain.main

import com.sword.cats.ModelFactory.CAT_FAVOURITE_ID
import com.sword.cats.ModelFactory.CAT_ID
import com.sword.cats.ModelFactory.CAT_IMAGE_ID
import com.sword.cats.ModelFactory.CAT_NAME
import com.sword.cats.ModelFactory.buildCatDto
import com.sword.cats.ModelFactory.buildCatEntity
import com.sword.cats.ModelFactory.buildCatUiModel
import com.sword.cats.ModelFactory.buildFavouriteApiResponse
import com.sword.cats.data.api.breeds.BreedsService
import com.sword.cats.data.api.favourites.FavouritesService
import com.sword.cats.data.database.CatDao
import com.sword.cats.data.database.CatEntity
import com.sword.cats.presentation.models.CatUiModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class MainRepositoryImplTest {

    private lateinit var breedsService: BreedsService
    private lateinit var favouritesService: FavouritesService
    private lateinit var catDao: CatDao
    private lateinit var repository: MainRepository

    @Before
    fun setup() {
        breedsService = mockk()
        favouritesService = mockk()
        catDao = mockk()
        repository = MainRepositoryImpl(breedsService, favouritesService, catDao)
    }

    @Test
    fun `observeCats should emit an empty list when the database is empty`() = runTest {
        every { catDao.getCatsSortedByNameAsc() } returns flowOf(emptyList())

        val result = repository.observeCats().first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `observeCats should emit a list of correctly mapped CatUiModel objects`() = runTest {
        val entities = listOf(buildCatEntity())

        every { catDao.getCatsSortedByNameAsc() } returns flowOf(entities)
        val result = repository.observeCats().first()

        assertEquals(1, result.size)
        assertEquals(CAT_NAME, result.first().name)
        assertTrue(result.first().isFavourite)
        assertEquals(CAT_FAVOURITE_ID, result.first().favouriteId)
    }

    @Test
    fun `observeCats should emit new lists when the database is updated`() = runTest {
        val flow = MutableSharedFlow<List<CatEntity>>(replay = 1)
        every { catDao.getCatsSortedByNameAsc() } returns flow

        val emissions = mutableListOf<List<CatUiModel>>()

        val job = launch {
            repository.observeCats().collect {
                emissions.add(it)
            }
        }

        // 🔑 Let the collector start
        advanceUntilIdle()

        flow.emit(emptyList())
        flow.emit(listOf(buildCatEntity()))

        advanceUntilIdle()
        job.cancel()

        assertEquals(2, emissions.size)
    }

    @Test(expected = RuntimeException::class)
    fun `observeCats should handle database errors gracefully`() = runTest {
        every { catDao.getCatsSortedByNameAsc() } returns flow {
            throw RuntimeException("DB error")
        }

        repository.observeCats().first()
    }

    @Test
    fun `search should handle catsProcess search failure`() = runTest {
        val errorBody = "".toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { breedsService.search("") } returns Response.error(500, errorBody)
        coEvery { favouritesService.getFavourites() } returns Response.success(emptyList())

        repository.search("")

        coVerify(exactly = 0) { catDao.insertCats(any()) }
    }

    @Test
    fun `search should handle catsProcess getFavourites failure`() = runTest {
        val errorBody = "".toResponseBody("application/json".toMediaTypeOrNull())

        coEvery { breedsService.search("") } returns Response.success(emptyList())
        coEvery { favouritesService.getFavourites() } returns Response.error(500, errorBody)

        repository.search("")

        coVerify(exactly = 0) { catDao.insertCats(any()) }
    }

    @Test
    fun `search should insert new cats when the database is empty`() = runTest {
        val dto = buildCatDto()

        coEvery { breedsService.search("") } returns Response.success(listOf(dto))
        coEvery { favouritesService.getFavourites() } returns Response.success(emptyList())
        coEvery { catDao.getCatById(CAT_ID) } returns null
        coEvery { catDao.insertCats(any()) } just Runs

        repository.search("")

        coVerify {
            catDao.insertCats(match { it.size == 1 })
        }
    }


    @Test
    fun `search should not update db when there are no changes`() = runTest {
        val dto = buildCatDto()
        val entity = buildCatEntity(dto, emptyList())!!

        coEvery { breedsService.search("") } returns Response.success(listOf(dto))
        coEvery { favouritesService.getFavourites() } returns Response.success(emptyList())
        coEvery { catDao.getCatById(CAT_ID) } returns entity

        repository.search("")

        coVerify(exactly = 0) { catDao.insertCats(any()) }
    }

    @Test
    fun `search should not unfavorite a cat that is favourite locally but unfavorited remotely`() = runTest {
        val dto = buildCatDto()
        val local = buildCatEntity()

        coEvery { breedsService.search("") } returns Response.success(listOf(dto))
        coEvery { favouritesService.getFavourites() } returns Response.success(emptyList())
        coEvery { catDao.getCatById(CAT_ID) } returns local
        coEvery { favouritesService.markAsFavourite(any()) } returns
                Response.success(buildFavouriteApiResponse())
        coEvery { catDao.insertCats(any()) } just Runs

        repository.search("")

        coVerify {
            favouritesService.markAsFavourite(any())
            catDao.insertCats(match {
                it.first().isFavourite && it.first().favouriteId == CAT_FAVOURITE_ID
            })
        }
    }

    @Test
    fun `onFavouriteClick should favourite a cat that is unfavorite`() = runTest {
        val cat = buildCatUiModel(isFavourite = false, favouriteId = null)

        coEvery { catDao.updateFavourite(CAT_ID, true, null) } just Runs
        coEvery { favouritesService.markAsFavourite(CAT_IMAGE_ID) } returns
                Response.success(buildFavouriteApiResponse())
        coEvery { catDao.updateFavouriteId(CAT_ID, CAT_FAVOURITE_ID) } just Runs

        repository.onFavouriteClick(cat)

        coVerifyOrder {
            catDao.updateFavourite(CAT_ID, true, null)
            favouritesService.markAsFavourite(CAT_IMAGE_ID)
            catDao.updateFavouriteId(CAT_ID, CAT_FAVOURITE_ID)
        }
    }

    @Test
    fun `onFavouriteClick should handle unfavorite a cat that's favourite`() = runTest {
        val cat = buildCatUiModel(isFavourite = true, favouriteId = CAT_FAVOURITE_ID)

        coEvery { catDao.updateFavourite(CAT_ID, false, null) } just Runs
        coEvery { favouritesService.unmarkAsFavourite(CAT_FAVOURITE_ID) } returns Response.success(Unit)

        repository.onFavouriteClick(cat)

        coVerify {
            catDao.updateFavourite(CAT_ID, false, null)
        }
        coVerify(exactly = 1) {
            favouritesService.unmarkAsFavourite(CAT_FAVOURITE_ID)
        }
    }

}
