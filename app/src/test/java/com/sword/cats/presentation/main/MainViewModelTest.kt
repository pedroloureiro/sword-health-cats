package com.sword.cats.presentation.main

import com.sword.cats.ModelFactory.buildCatUiModel
import com.sword.cats.domain.main.MainRepository
import com.sword.cats.presentation.models.CatUiModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.BeforeTest

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: MainRepository
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @BeforeTest
    fun testSetup(){
        every { repository.observeCats() } returns MutableStateFlow(listOf(buildCatUiModel()))
        coEvery { repository.search("") } just Runs

        viewModel = MainViewModel(repository)
    }

    @Test
    fun `uiState initial state is Loading`() = runTest {
        every { repository.observeCats() } returns MutableStateFlow(emptyList())

        viewModel = MainViewModel(repository)

        assertEquals(MainUIState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `uiState emits Loaded when repository returns cats`() = runTest {
        val catsFlow = MutableStateFlow(listOf(buildCatUiModel()))
        every { repository.observeCats() } returns catsFlow

        viewModel = MainViewModel(repository)

        val job = launch {
            viewModel.uiState.collect { }
        }

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is MainUIState.Loaded)
        assertEquals(1, (state as MainUIState.Loaded).catList.size)

        job.cancel()
    }

    @Test
    fun `uiState emits Idle when repository returns an empty list`() = runTest {
        val catsFlow = MutableStateFlow(emptyList<CatUiModel>())
        every { repository.observeCats() } returns catsFlow

        viewModel = MainViewModel(repository)

        val job = launch {
            viewModel.uiState.collect { }
        }

        advanceUntilIdle()

        assertEquals(MainUIState.Idle, viewModel.uiState.value)
        job.cancel()
    }

    @Test
    fun `uiState respects WhileSubscribed sharing`() = runTest {
        val upstreamCollected = mutableListOf<Boolean>()

        val upstream = flow {
            upstreamCollected.add(true)
            emit(emptyList<CatUiModel>())
            awaitCancellation()
        }

        every { repository.observeCats() } returns upstream

        viewModel = MainViewModel(repository)

        // No collectors yet
        assertTrue(upstreamCollected.isEmpty())

        val job = launch {
            viewModel.uiState.collect { }
        }

        advanceUntilIdle()
        assertEquals(1, upstreamCollected.size)

        job.cancel()
    }

    @Test
    fun `search transitions uiState to Loading`() = runTest {
        every { repository.observeCats() } returns MutableStateFlow(listOf(buildCatUiModel()))
        coEvery { repository.search("") } just Runs

        viewModel = MainViewModel(repository)

        viewModel.search("")
        advanceUntilIdle()

        assertEquals(MainUIState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `search calls repository search method`() = runTest {
        every { repository.observeCats() } returns MutableStateFlow(emptyList())
        coEvery { repository.search("") } just Runs

        viewModel = MainViewModel(repository)

        viewModel.search("")
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.search("") }
    }

    @Test
    fun `search concurrent calls handling`() = runTest {
        every { repository.observeCats() } returns MutableStateFlow(emptyList())
        coEvery { repository.search("") } just Runs

        viewModel = MainViewModel(repository)

        repeat(3) { viewModel.search("") }
        advanceUntilIdle()

        coVerify(exactly = 3) { repository.search("") }
    }


    @Test
    fun `onFavouriteClick calls repository with correct favorite status`() = runTest {
        every { repository.observeCats() } returns MutableStateFlow(emptyList())
        coEvery { repository.onFavouriteClick(any()) } just Runs

        viewModel = MainViewModel(repository)

        val cat = buildCatUiModel()

        viewModel.onFavouriteClick(cat)
        advanceUntilIdle()

        coVerify {
            repository.onFavouriteClick(cat)
        }
    }

    @Test
    fun `onFavouriteClick toggles favorite from true to false`() = runTest {
        val cat = buildCatUiModel()

        every { repository.observeCats() } returns MutableStateFlow(emptyList())
        coEvery { repository.onFavouriteClick(cat) } just Runs

        viewModel = MainViewModel(repository)

        viewModel.onFavouriteClick(cat)
        advanceUntilIdle()

        coVerify {
            repository.onFavouriteClick(cat)
        }
    }


    @Test
    fun `onFavouriteClick toggles favorite from false to true`() = runTest {
        val cat = buildCatUiModel(isFavourite = false, favouriteId = null)

        every { repository.observeCats() } returns MutableStateFlow(emptyList())
        coEvery { repository.onFavouriteClick(cat) } just Runs

        viewModel = MainViewModel(repository)

        viewModel.onFavouriteClick(cat)
        advanceUntilIdle()

        coVerify {
            repository.onFavouriteClick(cat)
        }
    }

    @Test
    fun `onFavouriteClick concurrent calls on the same item`() = runTest {
        every { repository.observeCats() } returns MutableStateFlow(emptyList())
        coEvery { repository.onFavouriteClick(any()) } just Runs

        viewModel = MainViewModel(repository)
        val cat = buildCatUiModel()

        repeat(5) {
            viewModel.onFavouriteClick(cat)
        }

        advanceUntilIdle()

        coVerify(exactly = 5) {
            repository.onFavouriteClick(any())
        }
    }
}