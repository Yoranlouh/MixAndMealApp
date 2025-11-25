package com.example.mixandmealapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mixandmealapp.ui.components.BottomNavBar
import com.example.mixandmealapp.ui.screens.account.AccountScreen
import com.example.mixandmealapp.ui.screens.auth.LoginScreen
import com.example.mixandmealapp.ui.screens.auth.RegisterScreen
import com.example.mixandmealapp.ui.screens.favorites.FavouritesScreen
import com.example.mixandmealapp.ui.screens.home.HomeScreen
import com.example.mixandmealapp.ui.screens.settings.SettingsScreen
import com.example.mixandmealapp.ui.theme.BrandGreen

private val noBottomBarRoutes = listOf(
    Navigation.LOGIN,
    Navigation.REGISTER,
    Navigation.SETTINGS,
//    Navigation.SEARCH_RESULT
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // observe huidige route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = navBackStackEntry?.destination?.route !in noBottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController = navController, currentDestination = currentDestination)
            }
        },
        floatingActionButton = {
            if (showBottomBar) {
                FloatingActionButton(
                    onClick = { navController.navigate("scan") },
                    shape = CircleShape,
                    containerColor = BrandGreen,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Filled.DocumentScanner, "Scan a recipe")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Navigation.HOME,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(Navigation.HOME) {
                HomeScreen(navController = navController)
            }

            composable(Navigation.SETTINGS) {
                SettingsScreen(navController = navController)
            }
            composable(Navigation.ACCOUNT) {
                AccountScreen(
                    onHomeClick = {navController.navigate(Navigation.HOME)},
                    onSettingsClick = {navController.navigate(Navigation.SETTINGS)},
                    navController = navController
                )
            }
            composable(Navigation.LOGIN) {
                LoginScreen(navController = navController)
            }
            composable(Navigation.REGISTER) {
                RegisterScreen(navController = navController)
            }
            composable(Navigation.FAVOURITES) {
                FavouritesScreen()
            }
//            composable(Navigation.SEARCH_RESULT) {
//                SearchResultScreen()
//            }
             // TODO: Add composable for "scan", "upload", "search", "profile"
        }
    }
}