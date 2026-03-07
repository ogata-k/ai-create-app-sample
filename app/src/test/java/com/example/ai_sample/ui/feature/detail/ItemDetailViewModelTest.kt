package com.example.ai_sample.ui.feature.detail

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.ai_sample.core.AppEvent
import com.example.ai_sample.core.AppEventBus
import com.example.ai_sample.data.model.Item
import com.example.ai_sample.data.repository.ItemRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ItemDetailViewModelTest {

    private val repository: ItemRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private val itemId = 1

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Full flow - LoadItemRequested intent results in Loading and Success mutations`() =
        runTest {
            // Given
            val item = Item(id = itemId, title = "Title", body = "Body", userId = 1)
            coEvery { repository.getItem(itemId) } returns Result.success(item)

            // When
            val viewModel = ItemDetailViewModel(repository, itemId)

            // Then
            viewModel.state.test {
                // 1. Initial State
                assertEquals(ItemDetailState(), awaitItem())

                // Allow init (Intent.LoadItemRequested) to execute
                // Intent -> Action.FetchItem -> Mutation.Loading -> Mutation.ItemLoaded

                // 2. Intent -> ... -> Mutation.Loading
                assertEquals(ItemDetailState(isLoading = true), awaitItem())

                // 3. Intent -> ... -> Mutation.ItemLoaded
                val finalState = awaitItem()
                assertEquals(item, finalState.item)
                assertEquals(false, finalState.isLoading)
            }
        }

    @Test
    fun `Full flow - LoadItemRequested intent results in Loading and Error mutations`() = runTest {
        // Given
        val errorMessage = "Not Found"
        coEvery { repository.getItem(itemId) } returns Result.failure(Exception(errorMessage))

        // When & Then
        turbineScope {
            val eventTurbine = AppEventBus.events.testIn(backgroundScope)
            val viewModel = ItemDetailViewModel(repository, itemId)

            viewModel.state.test {
                assertEquals(ItemDetailState(), awaitItem()) // Initial

                // 1. Intent -> ... -> Mutation.Loading
                assertEquals(ItemDetailState(isLoading = true), awaitItem())

                // 2. Intent -> ... -> Mutation.Error
                assertEquals(errorMessage, awaitItem().error)

                // Verify Side Effect (AppEvent)
                val event = eventTurbine.awaitItem()
                assert(event is AppEvent.ShowSnackbar)
                assertEquals(errorMessage, (event as AppEvent.ShowSnackbar).message)
            }
        }
    }

    @Test
    fun `Manual Intent Dispatch - LoadItemRequested triggers refresh flow`() = runTest {
        // Given
        val item1 = Item(id = itemId, title = "First", body = "Body", userId = 1)
        val item2 = Item(id = itemId, title = "Second", body = "Body", userId = 1)

        coEvery { repository.getItem(itemId) } returnsMany listOf(
            Result.success(item1),
            Result.success(item2)
        )

        val viewModel = ItemDetailViewModel(repository, itemId)

        viewModel.state.test {
            // Skip initial load
            skipItems(3) // Initial, Loading, Success(item1)

            // When - Manually dispatching intent again
            viewModel.dispatch(ItemDetailIntent.LoadItemRequested(itemId))

            // Then - Verify intent -> action -> mutation flow
            // 1. Mutation.Loading
            assertEquals(true, awaitItem().isLoading)

            // 2. Mutation.ItemLoaded(item2)
            val state = awaitItem()
            assertEquals(item2, state.item)
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun `Intent to Effect flow - BackClicked triggers NavigateBack effect`() = runTest {
        // Given
        coEvery { repository.getItem(itemId) } returns Result.success(mockk())
        val viewModel = ItemDetailViewModel(repository, itemId)
        advanceUntilIdle() // Finish init

        // When & Then
        viewModel.effects.test {
            // Intent.BackClicked -> Action.HandleBack -> Effect.NavigateBack
            viewModel.dispatch(ItemDetailIntent.BackClicked)

            val effect = awaitItem()
            assert(effect is ItemDetailEffect.NavigateBack)
        }
    }
}
