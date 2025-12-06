package com.example.mixandmealapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
import com.example.mixandmealapp.ui.screens.fridge.FridgeScreen
import com.example.mixandmealapp.ui.screens.splash.LoginSplashScreen
import com.example.mixandmealapp.ui.screens.recipes.PopularRecipeScreen
import com.example.mixandmealapp.ui.screens.recipes.EditorsChoiceScreen
import com.example.mixandmealapp.ui.screens.recipes.RecipeDetailScreen
import com.example.mixandmealapp.ui.screens.settings.options.LanguageChoiceScreen

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
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Navigation.SPLASHHOME,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Navigation.SPLASHHOME) {
                LoginSplashScreen(
                    navController = navController,
                    onGoToLogin = {
                        navController.navigate(Navigation.LOGIN)
                    },
                    onGoToRegister = {
                            navController.navigate(Navigation.REGISTER)
                    },
                    onGoToHome = {
                        navController.navigate(Navigation.HOME)
                    }
                )
            }
            composable(
                route = Navigation.HOME + "?showPrivacy={showPrivacy}",
                arguments = listOf(
                    navArgument("showPrivacy") {
                        type = NavType.BoolType
                        defaultValue = false
                    }
                )
            ) { entry ->
                val showPrivacy = entry.arguments?.getBoolean("showPrivacy") ?: false
                HomeScreen(navController = navController, showPrivacy = showPrivacy)
            }
            composable(Navigation.SETTINGS) {
                SettingsScreen(navController = navController)
            }
            composable(Navigation.LOGIN) {
                LoginScreen(navController = navController)
            }
            composable(Navigation.REGISTER) {
                RegisterScreen(
                    navController = navController,
                    onGoToLogin = {
                        navController.navigate(Navigation.LOGIN)
                    }
                )
            }
            composable(Navigation.FAVOURITES) {
                FavouritesScreen(
                    navController = navController,
                    onItemClick = { navController.navigate(Navigation.RECIPE_DETAIL) }
                )
            }
            composable(Navigation.SEARCH) {
                SearchResultScreen()
            }

            // Bottom bar bestemmingen
            composable(Navigation.UPLOAD) { UploadScreen(navController = navController) }
            composable(Navigation.SCAN) { ScanScreen() }
            composable(Navigation.SEARCH) { SearchScreen(navController = navController) }
            composable(Navigation.FRIDGE) { FridgeScreen(navController = navController) }
            composable(Navigation.POPULAR_RECIPES) { PopularRecipeScreen(navController = navController) }
            composable(Navigation.EDITORS_CHOICE) { EditorsChoiceScreen(navController = navController) }
            composable(Navigation.RECIPE_DETAIL) {
                RecipeDetailScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Navigation.LANGUAGE_CHOICE) {
                LanguageChoiceScreen(navController = navController)
            }
            composable(Navigation.ACCOUNT) {
                AccountScreen(
                    onGoToLogin = {
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
                    onGoToSettings = {
                        navController.navigate(Navigation.SETTINGS) {
                            launchSingleTop = true
                        }
                    },
//                    onGoToHome = {
//                        navController.navigate(Navigation.HOME) {
//                            popUpTo(navController.graph.startDestinationId) { saveState = true }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
//                    },
                    onEditProfile = {navController.navigate(Navigation.EDIT_PROFILE)},
                    navController = navController
                )
            }

            // Andere bestemmingen
            composable(Navigation.LOGIN) {
                LoginScreen(
                    navController = navController,
                    onGoToRegister = {
                        navController.navigate(Navigation.REGISTER)
                    }
                )
            }

            // Admin Only
            // ADMIN HOME
            composable(Navigation.HOME_ADMIN) {
                HomeScreen(navController = navController, showPrivacy = false)
            }

// ADMIN UPLOAD
            composable(Navigation.UPLOAD_ADMIN) {
                UploadScreen(navController = navController)
            }

// ADMIN SCAN
            composable(Navigation.SCAN_ADMIN) {
                ScanScreen()
            }

// ANALYTICS SCREEN
            composable(Navigation.ANALYTICS) {
                AnalyticsScreen() // <-- You create this screen
            }

// ADMIN ACCOUNT
            composable(Navigation.ACCOUNT_ADMIN) {
                AccountScreen(
                    navController = navController,
                    onGoToLogin = {
                        navController.navigate(Navigation.LOGIN)
                    },
                    onLogout = {
                        navController.navigate(Navigation.LOGIN) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onGoToSettings = { navController.navigate(Navigation.SETTINGS) }
                )
            }

        }
    }
}

@Composable
fun AnalyticsScreen() {
    TODO("Not yet implemented")
}