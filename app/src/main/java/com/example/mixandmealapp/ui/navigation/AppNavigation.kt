package com.example.mixandmealapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mixandmealapp.ui.components.BottomNavBar
import com.example.mixandmealapp.ui.components.UserBottomNavBar
import com.example.mixandmealapp.ui.screens.account.AccountScreen
import com.example.mixandmealapp.ui.screens.auth.LoginScreen
import com.example.mixandmealapp.ui.screens.auth.RegisterScreen
import com.example.mixandmealapp.ui.screens.favorites.FavouritesScreen
import com.example.mixandmealapp.ui.screens.home.HomeScreen
import com.example.mixandmealapp.ui.screens.search.SearchResultScreen
import com.example.mixandmealapp.ui.screens.settings.SettingsScreen
import com.example.mixandmealapp.ui.screens.search.SearchScreen
import com.example.mixandmealapp.ui.screens.upload.UploadScreen
import com.example.mixandmealapp.ui.screens.fridge.FridgeScreen
import com.example.mixandmealapp.ui.screens.splash.LoginSplashScreen
import com.example.mixandmealapp.ui.screens.recipes.PopularRecipeScreen
import com.example.mixandmealapp.ui.screens.recipes.EditorsChoiceScreen
import com.example.mixandmealapp.ui.screens.recipes.RecipeDetailScreen
import com.example.mixandmealapp.ui.screens.settings.options.LanguageChoiceScreen
import com.example.mixandmealapp.ui.screens.admin.AdminAnalyticsScreen
import com.example.mixandmealapp.ui.viewmodel.AuthUiState
import com.example.mixandmealapp.ui.viewmodel.AuthViewModel
import com.example.mixandmealapp.ui.viewmodel.FridgeViewModel
import com.example.mixandmealapp.ui.viewmodel.FavouritesViewModel

private val noBottomBarRoutes = listOf(
    Navigation.LOGIN,
    Navigation.REGISTER,
    Navigation.SETTINGS,
//    Navigation.SEARCH_RESULT
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Shared ViewModel instance for fridge across screens
    val fridgeViewModel = remember { FridgeViewModel() }
    // Shared ViewModel instance for favourites across screens
    val favouritesViewModel = remember { FavouritesViewModel() }

    // observe huidige route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = navBackStackEntry?.destination?.route !in noBottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                UserBottomNavBar(navController = navController, currentDestination = currentDestination)
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
                        // Verwijder de Splash uit de backstack zodat Home het nieuwe beginpunt is
                        navController.navigate(Navigation.HOME) {
                            popUpTo(Navigation.SPLASHHOME) { inclusive = true }
                            launchSingleTop = true
                        }
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
                    onItemClick = { navController.navigate(Navigation.RECIPE_DETAIL) },
                    viewModel = favouritesViewModel
                )
            }
            // Results page (separate from the main Search screen)
            composable(Navigation.SEARCH_RESULTS) { SearchResultScreen(navController = navController) }

            // Bottom bar bestemmingen
            composable(Navigation.UPLOAD) { UploadScreen(navController = navController) }
            // Bottom bar Search destination
            composable(Navigation.SEARCH) { SearchScreen(navController = navController) }
            composable(Navigation.FRIDGE) { FridgeScreen(navController = navController, viewModel = fridgeViewModel) }
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
            composable(Navigation.ADMIN_ANALYTICS) {
                AdminAnalyticsScreen()
            }
            composable(Navigation.ACCOUNT) {
                AccountScreen(
                    fridgeViewModel = fridgeViewModel,
                    favouritesViewModel = favouritesViewModel,
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
                    val viewModel: AuthViewModel = viewModel()
                    val state = viewModel.uiState

                    LoginScreen(
                        navController = navController,
                        onLogin = { email, password ->
                            viewModel.login(email, password)
                        },
                        onGoToRegister = { navController.navigate(Navigation.REGISTER) }
                    )

                    // React to state changes (navigation, error snackbar, etc.)
                    when (state) {
                        is AuthUiState.Success -> {
                            // e.g. navigate to home
                            LaunchedEffect(Unit) {
                                navController.navigate(Navigation.HOME) {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }
                        is AuthUiState.Error -> {
                            // show error UI or Snackbar
                        }
                        AuthUiState.Loading -> {
                            // show progress indicator
                        }
                        AuthUiState.Idle -> Unit
                    }
                }
            }
        }
    }
