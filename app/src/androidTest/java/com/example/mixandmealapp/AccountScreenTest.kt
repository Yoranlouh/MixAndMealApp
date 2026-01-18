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

    @Test
    fun accountScreen_whenLoggedOut_displaysLoginPrompt() {
        composeTestRule.setContent {
            AccountScreen(
                navController = rememberNavController(),
                isLoggedIn = false
            )
        }

        // Checkt of de welkomstboodschap en de login-knop worden getoond
        composeTestRule.onNodeWithText("Welcome, please login or sign up!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Login or Sign up").assertIsDisplayed()

        // Checkt of de secties voor ingelogde gebruikers niet zichtbaar zijn
        composeTestRule.onNodeWithText("My Allergens", substring = true).assertDoesNotExist()
        composeTestRule.onNodeWithText("My Favourites").assertDoesNotExist()
    }

    @Test
    fun loginButton_whenLoggedOut_isClickable() {
        var loginClicked = false

        composeTestRule.setContent {
            AccountScreen(
                navController = rememberNavController(),
                isLoggedIn = false,
                onGoToLogin = { loginClicked = true }
            )
        }

        // Klikt op de login-knop
        composeTestRule.onNodeWithText("Login or Sign up").performClick()

        // CHeckt of de klik is uitgevoerd
        assert(loginClicked) { "onGoToLogin lambda should have been called." }
    }

    @Test
    fun viewAll_forSections_areClickable() {
        var navigatedToAllergens = false
        var navigatedToDiets = false
        var navigatedToFavourites = false
        var navigatedToFridge = false

        composeTestRule.setContent {
            AccountScreen(
                navController = rememberNavController(),
                isLoggedIn = true,
                onNavigateToAllergens = { navigatedToAllergens = true },
                onNavigateToDiets = { navigatedToDiets = true }
            )
        }

        // Vind en klik op alle "View All" knoppen
        val viewAllNodes = composeTestRule.onAllNodesWithText("View All")

        // Klik op "View All" voor allergenen (de eerste die wordt gevonden)
        viewAllNodes[0].performClick()

        // Klik op "View All" voor diëten (de tweede)
        viewAllNodes[1].performClick()

        // Klik op "View All" voor favorieten (de derde)
        viewAllNodes[2].performClick()

        // Klik op "View All" voor koelkast (de vierde)
        viewAllNodes[3].performClick()


        // Verifieer dat de juiste acties zijn aangeroepen
        assert(navigatedToAllergens) { "Navigation to Allergens did not trigger." }
        assert(navigatedToDiets) { "Navigation to Diets did not trigger." }
    }
}
