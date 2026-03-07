package com.example.ai_sample.ui.feature.list

import com.example.ai_sample.data.model.Item
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ItemListReducerTest {

    private val reducer = ItemListReducer()
    private val initialState = ItemListState()

    @Test
    fun `Loading mutation should set isLoading to true and clear error`() {
        val stateWithError = initialState.copy(error = "Old Error")
        val newState = reducer.reduce(stateWithError, ItemListMutation.Loading)

        assertEquals(true, newState.isLoading)
        assertNull(newState.error)
    }

    @Test
    fun `ItemsLoaded mutation should set items and set isLoading to false`() {
        val items = listOf(Item(1, "Title", "Body", 1))
        val loadingState = initialState.copy(isLoading = true)

        val newState = reducer.reduce(loadingState, ItemListMutation.ItemsLoaded(items))

        assertEquals(items, newState.items)
        assertEquals(false, newState.isLoading)
        assertNull(newState.error)
    }

    @Test
    fun `Error mutation should set error message and set isLoading to false`() {
        val loadingState = initialState.copy(isLoading = true)
        val errorMessage = "Critical Error"

        val newState = reducer.reduce(loadingState, ItemListMutation.Error(errorMessage))

        assertEquals(errorMessage, newState.error)
        assertEquals(false, newState.isLoading)
    }
}
