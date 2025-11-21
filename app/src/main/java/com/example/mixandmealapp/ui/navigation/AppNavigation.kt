package com.example.mixandmealapp.ui.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mixandmealapp.ui.components.Navigation
import com.example.mixandmealapp.ui.screens.account.AccountScreen
import com.example.mixandmealapp.ui.screens.auth.LoginScreen
import com.example.mixandmealapp.ui.screens.auth.RegisterScreen
import com.example.mixandmealapp.ui.screens.favorites.FavouritesScreen
import com.example.mixandmealtest.SettingsScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Navigation.HOME
    ) {

        composable(Navigation.HOME) {
            HomeScreen(
                onSettingsClick = {navController.navigate(Navigation.SETTINGS)},
                onAccountClick = {navController.navigate(Navigation.ACCOUNT)},
                onLoginClick = {navController.navigate(Navigation.LOGIN)},
                onRegisterClick = {navController.navigate(Navigation.REGISTER)},
                onFavouritesClick = {navController.navigate(Navigation.FAVOURITES)},
            )

        }

        composable(Navigation.SETTINGS) {
            SettingsScreen()
        }
        composable(Navigation.ACCOUNT) {
            AccountScreen(
                onHomeClick = {navController.navigate(Navigation.HOME)},
                onSettingsClick = {navController.navigate(Navigation.SETTINGS)}
            )
        }
        composable(Navigation.LOGIN) {
            LoginScreen()
        }
        composable(Navigation.REGISTER) {
            RegisterScreen()
        }
        composable(Navigation.FAVOURITES) {
            FavouritesScreen()
        }
    }
}
