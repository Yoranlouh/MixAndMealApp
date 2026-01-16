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
import com.example.mixandmealapp.ui.screens.auth.LoginScreen
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoginFlow_EntersText_AndClicksLogin() {
        var capturedEmail = ""
        var capturedPassword = ""

        composeTestRule.setContent {
            // Mocking the NavController required by the LoginScreen
            val navController = rememberNavController()

            LoginScreen(
                navController = navController,
                onLogin = { email, password ->
                    capturedEmail = email
                    capturedPassword = password
                }
            )
        }

        // 1. Enter Email
        composeTestRule.onNodeWithText("Email")
            .performTextInput("test@example.com")

        // 2. Enter Password
        composeTestRule.onNodeWithText("Password")
            .performTextInput("password123")

        // 3. Click the Login Button
        // We filter by Role.Button to avoid clashing with the "Login" text in the TopAppBar
        composeTestRule.onNode(
            hasText("Login", ignoreCase = true) and hasAnyAncestor(isRoot()) and hasClickAction()
        ).performClick()

        // Alternative specific approach if the above is still ambiguous:
        // composeTestRule.onNode(hasText("Login") and hasRole(Role.Button)).performClick()

        // 4. Verify results
        composeTestRule.runOnIdle {
            assert(capturedEmail == "test@example.com")
            assert(capturedPassword == "password123")
        }
    }

    @Test
    fun testNavigationToRegister_IsClickable() {
        var registerClicked = false

        composeTestRule.setContent {
            val navController = rememberNavController()
            LoginScreen(
                navController = navController,
                onGoToRegister = { registerClicked = true }
            )
        }

        // Find the "Don't have an account? Register" text button
        // We use substring = true in case the string resource has trailing spaces or specific formatting
        composeTestRule.onNodeWithText("Register", substring = true, ignoreCase = true)
            .performClick()

        assert(registerClicked)
    }
}
