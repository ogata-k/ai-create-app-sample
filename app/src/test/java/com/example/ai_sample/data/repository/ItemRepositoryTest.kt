package com.example.ai_sample.data.repository

import com.example.ai_sample.data.api.ApiService
import com.example.ai_sample.data.model.Item
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ItemRepositoryTest {

    private val apiService: ApiService = mockk()
    private val repository = ItemRepository(apiService)

    @Test
    fun `getItems success returns Result success with items`() = runTest {
        // Given
        val items = listOf(Item(id = 1, title = "Title", body = "Body", userId = 1))
        coEvery { apiService.getItems(any(), any()) } returns items

        // When
        val result = repository.getItems()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(items, result.getOrNull())
    }

    @Test
    fun `getItems failure returns Result failure`() = runTest {
        // Given
        val exception = Exception("API Error")
        coEvery { apiService.getItems(any(), any()) } throws exception

        // When
        val result = repository.getItems()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `getItem success returns Result success with item`() = runTest {
        // Given
        val item = Item(id = 1, title = "Title", body = "Body", userId = 1)
        coEvery { apiService.getItem(1) } returns item

        // When
        val result = repository.getItem(1)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(item, result.getOrNull())
    }

    @Test
    fun `getItem failure returns Result failure`() = runTest {
        // Given
        val exception = Exception("Not Found")
        coEvery { apiService.getItem(1) } throws exception

        // When
        val result = repository.getItem(1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
