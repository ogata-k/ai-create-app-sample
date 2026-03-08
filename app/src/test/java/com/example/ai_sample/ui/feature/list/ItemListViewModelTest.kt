package com.example.ai_sample.ui.feature.list

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
class ItemListViewModelTest {

    private val repository: ItemRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `LoadItemsRequested succeeds should update state with items`() = runTest {
        // Given
        val items = listOf(Item(id = 1, title = "Title", body = "Body", userId = 1))
        coEvery { repository.getItems(any(), any()) } returns Result.success(items)

        // When
        val viewModel = ItemListViewModel(repository)

        // Then
        viewModel.state.test {
            // Initial state
            assertEquals(ItemListState(), awaitItem())

            // Advance to allow init -> LoadItemsRequested -> FetchItems to run
            advanceUntilIdle()

            // Loading state
            assertEquals(ItemListState(isLoading = true), awaitItem())

            // Success state
            val state = awaitItem()
            assertEquals(items, state.items)
            assertEquals(false, state.isLoading)
            assertEquals(null, state.error)
        }
    }

    @Test
    fun `LoadItemsRequested fails should update state with error and emit snackbar`() = runTest {
        // Given
        val errorMessage = "Network Error"
        coEvery {
            repository.getItems(
                any(),
                any()
            )
        } returns Result.failure(Exception(errorMessage))

        // When & Then
        turbineScope {
            val eventTurbine = AppEventBus.events.testIn(backgroundScope)
            val viewModel = ItemListViewModel(repository)
            val stateTurbine = viewModel.state.testIn(backgroundScope)

            // Initial state
            assertEquals(ItemListState(), stateTurbine.awaitItem())

            // Process actions
            advanceUntilIdle()

            // State transitions
            assertEquals(ItemListState(isLoading = true), stateTurbine.awaitItem())
            assertEquals(errorMessage, stateTurbine.awaitItem().error)

            // Event
            val event = eventTurbine.awaitItem()
            assert(event is AppEvent.ShowSnackbar)
            assertEquals(errorMessage, (event as AppEvent.ShowSnackbar).message)
        }
    }

    @Test
    fun `ItemClicked should emit NavigateToDetail effect`() = runTest {
        // Given
        coEvery { repository.getItems(any(), any()) } returns Result.success(emptyList())
        val viewModel = ItemListViewModel(repository)
        advanceUntilIdle()

        // When & Then
        viewModel.effects.test {
            viewModel.dispatch(ItemListIntent.ItemClicked(42))
            advanceUntilIdle()

            val effect = awaitItem()
            assert(effect is ItemListEffect.NavigateToDetail)
            assertEquals(42, (effect as ItemListEffect.NavigateToDetail).itemId)
        }
    }
}
