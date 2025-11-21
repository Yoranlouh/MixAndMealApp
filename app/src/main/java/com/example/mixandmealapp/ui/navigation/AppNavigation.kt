package com.example.mixandmealapp.ui.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mixandmealtest.SettingsScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onSettingsClick = {
                    navController.navigate("settings")
                }
                // Add other navigation actions here as needed
            )
        }

        composable("settings") {
            SettingsScreen()
        }
        // Add other composable destinations here as needed
    }
}
