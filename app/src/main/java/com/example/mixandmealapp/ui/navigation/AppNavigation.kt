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
import com.example.mixandmealapp.ui.screens.search.SearchResultScreen
import com.example.mixandmealapp.ui.screens.settings.SettingsScreen
import com.example.mixandmealapp.ui.screens.search.SearchScreen
import com.example.mixandmealapp.ui.screens.upload.UploadScreen
import com.example.mixandmealapp.ui.screens.scan.ScanScreen
import com.example.mixandmealapp.ui.screens.splash.LoginSplashScreen
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
            startDestination = Navigation.SPLASHHOME,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Navigation.SPLASHHOME) {
                LoginSplashScreen(navController = navController)
            }
            composable(Navigation.HOME) {
                HomeScreen(navController = navController)
            }
            composable(Navigation.SETTINGS) {
                SettingsScreen(navController = navController)
            }
            composable(Navigation.LOGIN) {
                LoginScreen(navController = navController)
            }
            composable(Navigation.REGISTER) {
                RegisterScreen(navController = navController)
            }
            composable(Navigation.FAVOURITES) {
                FavouritesScreen(navController = navController)
            }
            composable(Navigation.SEARCH) {
                SearchResultScreen()
            }

            // Bottom bar bestemmingen
            composable(Navigation.UPLOAD) { UploadScreen(navController = navController) }
            composable(Navigation.SCAN) { ScanScreen() }
            composable(Navigation.SEARCH) { SearchScreen(navController = navController) }
            composable(Navigation.ACCOUNT) {
                AccountScreen(
                    onLoginClick = {
                        navController.navigate(Navigation.LOGIN) {
                            launchSingleTop = true
                        }
                    },
                    onLogout = {
                        navController.navigate(Navigation.LOGIN) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onSettingsClick = {
                        navController.navigate(Navigation.SETTINGS) {
                            launchSingleTop = true
                        }
                    },
                    onHomeClick = {
                        navController.navigate(Navigation.HOME) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onEditProfile = {navController.navigate(Navigation.EDIT_PROFILE)},
                    navController = navController
                )
            }
        }
    }
}