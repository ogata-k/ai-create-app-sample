package com.example.ai_sample.ui.feature.list

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.ai_sample.data.model.Item
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class ItemListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun itemListScreen_successState_showsItemsAndHandlesClicks() {
        // Given
        val items = listOf(
            Item(id = 1, title = "Item 1", body = "Body 1", userId = 1),
            Item(id = 2, title = "Item 2", body = "Body 2", userId = 1)
        )
        val state = ItemListState()
        var clickedId: Int? = null

        // When
        composeTestRule.setContent {
            val pagedItems = flowOf(PagingData.from(items)).collectAsLazyPagingItems()
            ItemListScreen(
                state = state,
                pagedItems = pagedItems,
                onIntent = { intent ->
                    if (intent is ItemListIntent.ItemClicked) {
                        clickedId = intent.itemId
                    }
                }
            )
        }

        // Then
        composeTestRule.onNodeWithText("Item 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Item 2").assertIsDisplayed()

        // Act
        composeTestRule.onNodeWithText("Item 1").performClick()

        // Verify click
        assert(clickedId == 1)
    }
}
