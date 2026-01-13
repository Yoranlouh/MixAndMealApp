package com.example.mixandmealapp.ui.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mixandmealapp.models.enums.Role
import com.example.mixandmealapp.ui.components.AdminBottomNavBar
import com.example.mixandmealapp.ui.components.GuestBottomNavBar
import com.example.mixandmealapp.ui.components.UserBottomNavBar
import com.example.mixandmealapp.ui.screens.account.AccountScreen
import com.example.mixandmealapp.ui.screens.account.MyAllergensScreen
import com.example.mixandmealapp.ui.screens.account.MyDietsScreen
import com.example.mixandmealapp.ui.screens.admin.AdminAnalyticsScreen
import com.example.mixandmealapp.ui.screens.auth.LoginScreen
import com.example.mixandmealapp.ui.screens.auth.RegisterScreen
import com.example.mixandmealapp.ui.screens.favorites.FavouritesScreen
import com.example.mixandmealapp.ui.screens.fridge.FridgeScreen
import com.example.mixandmealapp.ui.screens.home.HomeScreen
import com.example.mixandmealapp.ui.screens.recipes.RecipeDetailScreen
import com.example.mixandmealapp.ui.screens.search.SearchResultScreen
import com.example.mixandmealapp.ui.screens.search.SearchScreen
import com.example.mixandmealapp.ui.screens.settings.SettingsScreen
import com.example.mixandmealapp.ui.screens.settings.options.LanguageChoiceScreen
import com.example.mixandmealapp.ui.screens.splash.LoginSplashScreen
import com.example.mixandmealapp.ui.screens.upload.UploadScreen
import com.example.mixandmealapp.ui.viewmodel.AuthViewModel
import com.example.mixandmealapp.ui.viewmodel.FavouritesViewModel
import com.example.mixandmealapp.ui.viewmodel.FridgeViewModel
import com.example.mixandmealapp.ui.viewmodel.HomeViewModel
import com.example.mixandmealapp.ui.viewmodel.LocaleViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

private val noBottomBarRoutes = listOf(
    Navigation.LOGIN,
    Navigation.REGISTER,
    Navigation.SETTINGS,
    Navigation.SPLASHHOME,
    Navigation.MY_ALLERGENS,
    Navigation.MY_DIETS
)

@Composable
fun AppNavigation(
    localeViewModel: LocaleViewModel,
    onCameraClick: () -> Unit,
    onPhotoPick: (callback: (Uri?) -> Unit) -> Unit
) {
    val navController = rememberNavController()
    
    // Shared ViewModel instances
    val fridgeViewModel = remember { FridgeViewModel() }
    val favouritesViewModel : FavouritesViewModel = koinViewModel()
    val homeViewModel: HomeViewModel = koinInject()

    // Check role on app start
    LaunchedEffect(Unit) {
        homeViewModel.authenticateRole()
    }

    // Observe role state
    val user by homeViewModel.role.collectAsState()

    // Log role changes for debugging
    LaunchedEffect(user) {
        Log.d("AppNavigation", "Observed role change: ${user.role}")
    }

    // Observe current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = navBackStackEntry?.destination?.route !in noBottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                // Determine current role enum safely
                val currentRoleEnum: Role = try {
                    // Normalize the role string: trim whitespace and uppercase
                    var roleString = user.role.trim().uppercase()
                    
                    // Handle "ROLE_" prefix if present (common in Spring Security)
//                    if (roleString.startsWith("ROLE_")) {
//                        roleString = roleString.removePrefix("ROLE_")
//                    }
                    
                    Log.d("AppNavigation", "Parsing role string: '$roleString'")
                    Role.valueOf(roleString)
                } catch (e: Exception) {
                    Log.e("AppNavigation", "Failed to parse role: '${user.role}'", e)
                    Role.GUEST
                }
                // Show correct bottom bar based on role
                when (currentRoleEnum) {
                    Role.USER -> {
                        UserBottomNavBar(
                            navController = navController,
                            currentDestination = currentDestination
                        )
                    }
                    Role.ADMIN -> {
                        AdminBottomNavBar(
                            navController = navController,
                            currentDestination = currentDestination
                        )
                    }
                    else -> {
                        GuestBottomNavBar(
                            navController = navController,
                            currentDestination = currentDestination
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Navigation.SPLASHHOME,
            modifier = Modifier.padding(paddingValues)
        ) {
            // --- SPLASH ---
            composable(Navigation.SPLASHHOME) {
                LoginSplashScreen(
                    navController = navController,
                    onGoToLogin = { navController.navigate(Navigation.LOGIN) },
                    onGoToRegister = { navController.navigate(Navigation.REGISTER) },
                    // FIX HERE:
                    onGoToHome = {
                        // 1. Force the app to forget previous sessions
                        homeViewModel.logout()

                        // 2. Navigate to Home
                        navController.navigate(Navigation.HOME) {
                            popUpTo(Navigation.SPLASHHOME) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // --- HOME ---
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

            // --- AUTH ---
            composable(Navigation.LOGIN) {
                // Inject AuthViewModel. Koin should provide it with the correct singleton HomeViewModel
                val viewModel: AuthViewModel = koinViewModel()
                val state by viewModel.uiState.collectAsState()

                LoginScreen(
                    navController = navController,
                    onLogin = { email, password ->
                        viewModel.login(email, password)
                    },
                    onGoToRegister = { navController.navigate(Navigation.REGISTER) }
                )
            }
            composable(Navigation.REGISTER) {
                RegisterScreen(
                    navController = navController,
                    onGoToLogin = { navController.navigate(Navigation.LOGIN) }
                )
            }

            // --- MAIN TABS & FEATURES ---
            composable(Navigation.SETTINGS) { SettingsScreen(navController = navController) }

            composable(Navigation.FAVOURITES) {
                FavouritesScreen(
                    navController = navController,
                    onItemClick = { recipeId ->
                        navController.navigate("${Navigation.RECIPE_DETAIL}/$recipeId")
                    },
                    viewModel = favouritesViewModel
                )
            }

            composable(Navigation.SEARCH) { SearchScreen(navController = navController) }
            composable(Navigation.SEARCH_RESULTS) { SearchResultScreen(navController = navController) }


            composable(Navigation.UPLOAD) {
                UploadScreen(
                    navController = navController,
                    onPhotoPick = onPhotoPick,
                    token = ""
                )
            }

            composable(Navigation.FRIDGE) { 
                FridgeScreen(navController = navController, viewModel = fridgeViewModel) 
            }
            
            composable(
                route = "${Navigation.RECIPE_DETAIL}/{recipeId}",
                arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
            ) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 1
                RecipeDetailScreen(
                    recipeId = recipeId,
                    onBack = { navController.popBackStack() }
                )
            }
            
            composable(Navigation.LANGUAGE_CHOICE) {
                LanguageChoiceScreen(
                    navController = navController,
                    localeViewModel = localeViewModel
                )
            }

            composable(Navigation.ACCOUNT) {
                AccountScreen(
                    fridgeViewModel = fridgeViewModel,
                    favouritesViewModel = favouritesViewModel,
                    onGoToLogin = {
                        navController.navigate(Navigation.LOGIN) { launchSingleTop = true }
                    },
                    onLogout = {
                        // Clear role on logout
                        homeViewModel.logout()
                        navController.navigate(Navigation.LOGIN) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onGoToSettings = {
                        navController.navigate(Navigation.SETTINGS) { launchSingleTop = true }
                    },
                    onEditProfile = { navController.navigate(Navigation.EDIT_PROFILE) },
                    onNavigateToAllergens = { navController.navigate(Navigation.MY_ALLERGENS) },
                    onNavigateToDiets = { navController.navigate(Navigation.MY_DIETS) },
                    navController = navController,
                    isLoggedIn = user.role != "Guest" // Pass isLoggedIn state
                )
            }

            composable(Navigation.MY_ALLERGENS) {
                MyAllergensScreen(onBack = { navController.popBackStack() })
            }

            composable(Navigation.MY_DIETS) {
                MyDietsScreen(onBack = { navController.popBackStack() })
            }

            // --- ADMIN ROUTES ---
            // Note: These use the same Composables as users for now, 
            // but the bottom bar will differ (Access to Analytics, etc.)
            composable(Navigation.ADMIN_ANALYTICS) {
                AdminAnalyticsScreen()
            }
        }
    }
}
