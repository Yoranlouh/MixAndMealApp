package com.example.mixandmealapp

import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.mixandmealapp.ui.screens.auth.RegisterScreen
import org.junit.Rule
import org.junit.Test

class RegisterScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testGoToLogin_IsClickable() {
        var goToLoginClicked = false

        composeTestRule.setContent {
            val navController = rememberNavController()
            RegisterScreen(
                navController = navController,
                onGoToLogin = { goToLoginClicked = true }
            )
        }

        composeTestRule.onNodeWithText("Login", substring = true, ignoreCase = true)
            .performClick()

        assert(goToLoginClicked)
    }

    @Test
    fun testValidation_ShowsErrors_WhenFieldsInvalid() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            RegisterScreen(
                navController = navController
            )
        }

        // Direct op "Create account" klikken zonder iets in te vullen
        composeTestRule.onNode(
            hasText("Create account", ignoreCase = true) and
                    hasAnyAncestor(isRoot()) and
                    hasClickAction()
        ).performClick()

        // Verwacht foutmeldingen, teksten moeten overeenkomen:
        // username_empty_error, invalid_email, password_min_error
        composeTestRule.onNodeWithText("Username cannot be empty", substring = true, ignoreCase = true)
            .assertExists()
        composeTestRule.onNodeWithText("Invalid email", substring = true, ignoreCase = true)
            .assertExists()
        composeTestRule.onNodeWithText("Password must be at least", substring = true, ignoreCase = true)
            .assertExists()
    }
}