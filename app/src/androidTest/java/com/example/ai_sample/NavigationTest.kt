package com.example.ai_sample

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun app_startsAtItemList_andCanNavigateToDetail() {
        // Wait for items to load and verify we are on the List screen
        // The title in ItemListScreen is "AI Sample Items (Paged)"
        composeTestRule.waitUntilAtLeastOneExists(hasText("AI Sample Items (Paged)"), 5000)
        composeTestRule.onNodeWithText("AI Sample Items (Paged)").assertIsDisplayed()

        // Find the first item and click it
        // Since it's a paged list, we wait for an item to appear
        composeTestRule.waitUntilAtLeastOneExists(hasClickAction(), 10000)

        composeTestRule.onAllNodes(hasClickAction())
            .onFirst()
            .performClick()

        // Verify we navigated to the Detail screen
        // Title in ItemDetailScreen is "Item Details"
        composeTestRule.onNodeWithText("Item Details").assertIsDisplayed()

        // Navigate back
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Verify we are back on the List screen
        composeTestRule.onNodeWithText("AI Sample Items (Paged)").assertIsDisplayed()
    }
}
