package com.example.mixandmealapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mixandmealapp.ui.navigation.Navigation
import com.example.mixandmealapp.ui.theme.BrandGreen
import com.example.mixandmealapp.ui.theme.BrandGrey
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme

enum class UserRole {
    USER,
    ADMIN
}

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavBar(
    navController: NavHostController,
    currentDestination: NavDestination?,
    userRole: UserRole // Voeg de rol toe als parameter
) {
    val isAdmin = userRole == UserRole.ADMIN
    // Definieer de items voor elke rol
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
        BottomNavItem("Analytics", Icons.Filled.Analytics, Navigation.ADMIN_ANALYTICS),
        BottomNavItem("Profile", Icons.Filled.Person, Navigation.ACCOUNT)
    )

    // Kies de juiste lijst op basis van de rol
    val items = when (userRole) {
        UserRole.USER -> userNavItems
        UserRole.ADMIN -> adminNavItems
    }

    // Shared palette from screenshot
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

                NavigationBarItem(
                    selected = selected,
                    enabled = true,
                    // Verberg label onder het Search-icoon
                    alwaysShowLabel = item.route != Navigation.SEARCH,
                    onClick = {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                        }
                    },
                    icon = {
                        // Search icon should always be the floating green circle as per design
                        val isSearchFloating = item.route == Navigation.SEARCH
                        if (isSearchFloating) {
                            // Floating green circle with white icon, overlapping the bar
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
                        // Geen label tonen voor Search
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

// Losse wrappers zodat je afzonderlijke composables hebt voor user en admin
@Composable
fun UserBottomNavBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    BottomNavBar(
        navController = navController,
        currentDestination = currentDestination,
        userRole = UserRole.USER
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
        userRole = UserRole.ADMIN
    )
}

// Preview voor de USER rol
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

// Preview voor de ADMIN rol
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
