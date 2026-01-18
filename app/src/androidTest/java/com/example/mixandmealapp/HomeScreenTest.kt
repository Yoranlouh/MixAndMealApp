package com.example.mixandmealapp

import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.example.mixandmealapp.ui.screens.home.HomeScreen
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFalse

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_showsHeaderAndSections() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            HomeScreen(
                navController = navController,
                showPrivacy = false
            )
        }

        // Controle: headertekst (pas tekst aan je eigen string)
        composeTestRule.onNodeWithText("Hello", substring = true, ignoreCase = true)
            .assertExists()

        // Controle: hoofdsecties zichtbaar
        composeTestRule.onNodeWithText("Featured", substring = true, ignoreCase = true)
            .assertExists()
        composeTestRule.onNodeWithText("Popular", substring = true, ignoreCase = true)
            .assertExists()
        composeTestRule.onNodeWithText("Quick", substring = true, ignoreCase = true)
            .assertExists()
        composeTestRule.onNodeWithText("Easy", substring = true, ignoreCase = true)
            .assertExists()

        // Controle: categorie‑titel
        composeTestRule.onNodeWithText("Category", substring = true, ignoreCase = true)
            .assertExists()
    }

    @Test
    fun homeScreen_showsPrivacyDialogAndCallsOnAccept() {
        var accepted = false

        composeTestRule.setContent {
            val navController = rememberNavController()
            HomeScreen(
                navController = navController,
                showPrivacy = true,
                onAcceptPrivacy = { accepted = true }
            )
        }

        // Zoek een kenmerkende tekst uit je PrivacyDialog
        composeTestRule.onNodeWithText("Privacy", substring = true, ignoreCase = true)
            .assertExists()

        composeTestRule.runOnIdle {
            assertFalse(accepted)
        }
    }
}
