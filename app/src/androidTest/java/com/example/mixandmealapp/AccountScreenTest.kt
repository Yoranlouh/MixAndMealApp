package com.example.mixandmealapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.mixandmealapp.ui.screens.account.AccountScreen
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest


class AccountScreenTest : KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // AS-01
    @Test
    fun accountScreen_whenLoggedIn_displaysSections() {
        composeTestRule.setContent {
            AccountScreen(
                navController = rememberNavController(),
                isLoggedIn = true
            )
        }

        // Checkt of de titel "Account" wordt weergegeven
        composeTestRule.onNodeWithText("Account").assertIsDisplayed()

        // Checkt of alle secties voor een ingelogde gebruiker zichtbaar zijn
        composeTestRule.onNodeWithText("My Allergens", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("My Diets", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("My Favourites").assertIsDisplayed()
        composeTestRule.onNodeWithText("My Fridge", substring = true).assertIsDisplayed()
    }

    // AS-02
    @Test
    fun accountScreen_whenLoggedOut_displaysLoginPrompt() {
        composeTestRule.setContent {
            AccountScreen(
                navController = rememberNavController(),
                isLoggedIn = false
            )
        }

        // Checkt of de secties voor ingelogde gebruikers niet zichtbaar zijn
        composeTestRule.onNodeWithText("My Allergens", substring = true).assertDoesNotExist()
        composeTestRule.onNodeWithText("My Favourites").assertDoesNotExist()
    }
}
