package com.sword.cats.presentation.main


import com.sword.cats.domain.main.CatBreedItem
import com.sword.cats.domain.main.MainInteractor
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var interactor: MainInteractor
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        interactor = mockk()
        viewModel = MainViewModel(interactor)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search emits Loading then Loaded when interactor succeeds`() = runTest {
        val catItems = listOf(
            CatBreedItem(
                id = "abys",
                name = "Abyssinian",
                imageUrl = "https://image.url/cat.jpg"
            )
        )

        coEvery { interactor.searchCatBreeds() } coAnswers {
            delay(10)
            Result.success(catItems)
        }

        val emittedStates = mutableListOf<MainUIState>()
        val job = launch {
            viewModel.uiState.toList(emittedStates)
        }

        viewModel.search()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            listOf(
                MainUIState.Idle,
                MainUIState.Loading,
                MainUIState.Loaded(catItems)
            ),
            emittedStates
        )

        coVerify(exactly = 1) { interactor.searchCatBreeds() }

        job.cancel()
    }

    @Test
    fun `search stays in Loading when interactor fails`() = runTest {
        coEvery { interactor.searchCatBreeds() } returns Result.failure(Exception("Error"))

        val emittedStates = mutableListOf<MainUIState>()
        val job = launch {
            viewModel.uiState.toList(emittedStates)
        }

        viewModel.search()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(
            listOf(
                MainUIState.Idle,
                MainUIState.Loading
            ),
            emittedStates
        )

        coVerify(exactly = 1) { interactor.searchCatBreeds() }

        job.cancel()
    }
}