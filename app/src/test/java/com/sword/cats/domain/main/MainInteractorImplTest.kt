package com.sword.cats.domain.main

import com.sword.cats.ModelFactory.buildCatDto
import com.sword.cats.ModelFactory.buildCatEntity
import com.sword.cats.ModelFactory.buildCatFavouriteDto
import com.sword.cats.domain.cats.CatsProcess
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MainInteractorImplTest {

    private lateinit var catsProcess: CatsProcess
    private lateinit var interactor: MainInteractor

    @Before
    fun setup() {
        catsProcess = mockk()
        interactor = MainInteractorImpl(catsProcess)
    }

    @Test
    fun `searchCatBreeds returns mapped CatBreedItem list when process succeeds`() = runTest {
        val catDtoList = listOf(buildCatDto())
        val catEntityList = listOf(buildCatEntity())
        val catFavouriteDtoList = listOf(buildCatFavouriteDto())

        coEvery { catsProcess.search() } returns Result.success(catDtoList)
        coEvery { catsProcess.getFavourites() } returns Result.success(catFavouriteDtoList)
        coEvery { catsProcess.save(catEntityList) } returns Unit

        val result = interactor.search()

        assertTrue(result.isSuccess)

        val items = result.getOrNull()
        assertNotNull(items)
        assertEquals(1, items!!.size)

        val item = items.first()
        assertEquals("abys", item.id)
        assertEquals("Abyssinian", item.name)
        assertEquals("https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg", item.imageUrl)
        assertTrue(item.isFavourite)

        coVerify(exactly = 1) { catsProcess.search() }
    }

    @Test
    fun `searchCatBreeds returns failure when process fails`() = runTest {
        val catList = listOf(buildCatEntity())
        val catFavouriteDtoList = listOf(buildCatFavouriteDto())

        coEvery { catsProcess.search() } returns Result.failure(Exception("Error"))
        coEvery { catsProcess.searchDb() } returns Result.success(catList)
        coEvery { catsProcess.getFavourites() } returns Result.success(catFavouriteDtoList)

        val result = interactor.search()

        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())

        coVerify(exactly = 1) { catsProcess.search() }
    }

    @Test
    fun `searchCatBreeds returns failure when process success contains null`() = runTest {
        val catFavouriteDtoList = listOf(buildCatFavouriteDto())

        coEvery { catsProcess.search() } returns Result.success(emptyList())
        coEvery { catsProcess.save(emptyList()) } returns Unit
        coEvery { catsProcess.getFavourites() } returns Result.success(catFavouriteDtoList)

        val result = interactor.search()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()!!.isEmpty())

        coVerify(exactly = 1) { catsProcess.search() }
    }
}
