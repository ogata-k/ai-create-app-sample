package com.example.ai_sample.ui.feature.detail

import com.example.ai_sample.data.model.Item
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ItemDetailReducerTest {

    private val reducer = ItemDetailReducer()
    private val initialState = ItemDetailState()

    @Test
    fun `Loading mutation should set isLoading to true and clear error`() {
        val stateWithError = initialState.copy(error = "Old Error")
        val newState = reducer.reduce(stateWithError, ItemDetailMutation.Loading)

        assertEquals(true, newState.isLoading)
        assertNull(newState.error)
    }

    @Test
    fun `ItemLoaded mutation should set item and set isLoading to false`() {
        val item = Item(1, "Title", "Body", 1)
        val loadingState = initialState.copy(isLoading = true)

        val newState = reducer.reduce(loadingState, ItemDetailMutation.ItemLoaded(item))

        assertEquals(item, newState.item)
        assertEquals(false, newState.isLoading)
        assertNull(newState.error)
    }

    @Test
    fun `Error mutation should set error message and set isLoading to false`() {
        val loadingState = initialState.copy(isLoading = true)
        val errorMessage = "Not Found"

        val newState = reducer.reduce(loadingState, ItemDetailMutation.Error(errorMessage))

        assertEquals(errorMessage, newState.error)
        assertEquals(false, newState.isLoading)
    }
}
