package com.example.mixandmealapp.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mixandmealapp.models.enums.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mixandmealapp.ui.navigation.Navigation
import com.example.mixandmealapp.ui.theme.BrandGreen
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavBar(
    navController: NavHostController,
    currentDestination: NavDestination?,
    userRole: Role
) {
    val userNavItems = listOf(
        BottomNavItem("Home", Icons.Filled.Home, Navigation.HOME),
        BottomNavItem("Favourites", Icons.Filled.Favorite, Navigation.FAVOURITES),
        BottomNavItem("Search", Icons.Filled.Search, Navigation.SEARCH),
        BottomNavItem("Fridge", Icons.Filled.Kitchen, Navigation.FRIDGE),
        BottomNavItem("Profile", Icons.Filled.Person, Navigation.ACCOUNT)
    )

    val adminNavItems = listOf(
        BottomNavItem("Home", Icons.Filled.Home, Navigation.HOME),
        BottomNavItem("Upload", Icons.Filled.Edit, Navigation.UPLOAD),
        BottomNavItem("Search", Icons.Filled.Search, Navigation.SEARCH),
        BottomNavItem("Favourites", Icons.Filled.Favorite, Navigation.FAVOURITES),
        BottomNavItem("Profile", Icons.Filled.Person, Navigation.ACCOUNT)
    )

    val guestNavItems = listOf(
        BottomNavItem("Home", Icons.Filled.Home, Navigation.HOME),
        BottomNavItem("Favourites", Icons.Filled.Favorite, Navigation.LOGIN),
        BottomNavItem("Search", Icons.Filled.Search, Navigation.SEARCH),
        BottomNavItem("Fridge", Icons.Filled.Kitchen, Navigation.LOGIN),
        BottomNavItem("Profile", Icons.Filled.Person, Navigation.LOGIN)
    )

    // Select items based on role
    val items = when (userRole) {
        Role.USER -> userNavItems
        Role.ADMIN -> adminNavItems
        else -> guestNavItems
    }

    val unselectedGrey = Color(0xFFB0B8BF)

    Box {
        NavigationBar(
            modifier = Modifier.height(120.dp),
            containerColor = Color.White,
            tonalElevation = 8.dp
        ) {
            items.forEach { item ->
                val selected =
                    currentDestination?.hierarchy?.any { it.route == item.route } == true
                
                // Identify the "Search" item for special styling if needed
                val isFloatingAction = item.route == Navigation.SEARCH

                NavigationBarItem(
                    selected = selected,
                    enabled = true,
                    alwaysShowLabel = item.route != Navigation.SEARCH,
                    onClick = {
                        val target = item.route
                        val currentRoute = navController.currentDestination?.route

                        if (currentRoute?.split("?")?.first() == target.split("?")?.first()) {
                            // If we are already on Search, we might want to stay there but ensure it's clean.
                            // However, the issue states it should always route to SearchScreen.
                            // To prevent crash if something is wrong with current state, we can just navigate.
                            // But usually, if already on the destination, we don't navigate unless we want to clear backstack.
                            if (target != Navigation.SEARCH) return@NavigationBarItem
                        }

                        Log.d("BottomNavBar", "Navigating to: $target")
                        try {
                            navController.navigate(target) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true 
                            }
                        } catch (e: Exception) {
                            Log.e("BottomNavBar", "Navigation failed", e)
                        }
                    },
                    icon = {
                        // Apply special styling for Search for User/Admin if desired.
                        if (item.route == Navigation.SEARCH) {
                             Box(
                                modifier = Modifier
                                    .offset(y = (-10).dp)
                                    .size(60.dp)
                                    .shadow(8.dp, shape = CircleShape)
                                    .background(BrandGreen, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        } else {
                            val iconTint = if (selected) BrandGreen else unselectedGrey
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = iconTint
                            )
                        }
                    },
                    label = {
                        if (item.route != Navigation.SEARCH) {
                            val labelColor = if (selected) BrandGreen else unselectedGrey
                            Text(item.title, color = labelColor)
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandGreen,
                        selectedTextColor = BrandGreen,
                        unselectedIconColor = unselectedGrey,
                        unselectedTextColor = unselectedGrey,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Composable
fun UserBottomNavBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    BottomNavBar(
        navController = navController,
        currentDestination = currentDestination,
        userRole = Role.USER
    )
}

@Composable
fun AdminBottomNavBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    BottomNavBar(
        navController = navController,
        currentDestination = currentDestination,
        userRole = Role.ADMIN
    )
}

@Composable
fun GuestBottomNavBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    BottomNavBar(
        navController = navController,
        currentDestination = currentDestination,
        userRole = Role.GUEST
    )
}

@Preview(showBackground = true, name = "BottomNavBar - User")
@Composable
fun BottomNavBarUserPreview() {
    MixAndMealAppTheme {
        val navController = rememberNavController()
        val navDestination = NavDestination(Navigation.HOME).apply {
            this.route = Navigation.HOME
        }
        UserBottomNavBar(
            navController = navController,
            currentDestination = navDestination
        )
    }
}

@Preview(showBackground = true, name = "BottomNavBar - Admin")
@Composable
fun BottomNavBarAdminPreview() {
    MixAndMealAppTheme {
        val navController = rememberNavController()
        val navDestination = NavDestination(Navigation.HOME).apply {
            this.route = Navigation.HOME
        }
        AdminBottomNavBar(
            navController = navController,
            currentDestination = navDestination
        )
    }
}
