package com.example.ai_sample.ui.feature.detail

import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.ai_sample.data.model.Item
import org.junit.Rule
import org.junit.Test

class ItemDetailScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun itemDetailScreen_loadingState_showsCircularProgressIndicator() {
        // Given
        val state = ItemDetailState(isLoading = true)

        // When
        composeTestRule.setContent {
            ItemDetailScreen(
                state = state,
                onIntent = {}
            )
        }

        // Then
        composeTestRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertIsDisplayed()
    }

    @Test
    fun itemDetailScreen_errorState_showsErrorMessage() {
        // Given
        val errorMessage = "Item not found"
        val state = ItemDetailState(error = errorMessage)

        // When
        composeTestRule.setContent {
            ItemDetailScreen(
                state = state,
                onIntent = {}
            )
        }

        // Then
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun itemDetailScreen_successState_showsItemDetailsAndHandlesBackClick() {
        // Given
        val item = Item(id = 1, title = "Detail Title", body = "Detail Body", userId = 1)
        val state = ItemDetailState(item = item)
        var backClicked = false

        // When
        composeTestRule.setContent {
            ItemDetailScreen(
                state = state,
                onIntent = { intent ->
                    if (intent is ItemDetailIntent.BackClicked) {
                        backClicked = true
                    }
                }
            )
        }

        // Then
        composeTestRule.onNodeWithText("Detail Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Detail Body").assertIsDisplayed()
        composeTestRule.onNodeWithText("User ID: 1").assertIsDisplayed()

        // Act
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Verify click
        assert(backClicked)
    }
}
