package com.sword.cats.domain.main

import com.sword.cats.ModelFactory.fakeBreed
import com.sword.cats.ModelFactory.fakeCatEntity
import com.sword.cats.domain.breeds.BreedsProcess
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MainInteractorImplTest {

    private lateinit var breedsProcess: BreedsProcess
    private lateinit var interactor: MainInteractor

    @Before
    fun setup() {
        breedsProcess = mockk()
        interactor = MainInteractorImpl(breedsProcess)
    }

    @Test
    fun `searchCatBreeds returns mapped CatBreedItem list when process succeeds`() = runTest {
        val breeds = listOf(
            fakeBreed(
                id = "abys",
                name = "Abyssinian",
                imageUrl = "https://image.url/cat.jpg"
            )
        )
        val catList = listOf(fakeCatEntity())

        coEvery { breedsProcess.search() } returns Result.success(breeds)
        coEvery { breedsProcess.save(catList) } returns Unit

        val result = interactor.searchCatBreeds()

        assertTrue(result.isSuccess)

        val items = result.getOrNull()
        assertNotNull(items)
        assertEquals(1, items!!.size)

        val item = items.first()
        assertEquals("abys", item.id)
        assertEquals("Abyssinian", item.name)
        assertEquals("https://image.url/cat.jpg", item.imageUrl)
        assertFalse(item.favorite)

        coVerify(exactly = 1) { breedsProcess.search() }
    }

    @Test
    fun `searchCatBreeds returns failure when process fails`() = runTest {
        val catList = listOf(fakeCatEntity())

        coEvery { breedsProcess.search() } returns Result.failure(Exception("Error"))
        coEvery { breedsProcess.getCatsFromDb() } returns Result.success(catList)

        val result = interactor.searchCatBreeds()

        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())

        coVerify(exactly = 1) { breedsProcess.search() }
    }

    @Test
    fun `searchCatBreeds returns failure when process success contains null`() = runTest {
        coEvery { breedsProcess.search() } returns Result.success(emptyList())
        coEvery { breedsProcess.save(emptyList()) } returns Unit

        val result = interactor.searchCatBreeds()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()!!.isEmpty())

        coVerify(exactly = 1) { breedsProcess.search() }
    }
}
