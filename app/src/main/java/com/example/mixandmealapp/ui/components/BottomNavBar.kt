package com.example.mixandmealapp.ui.components

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
import androidx.compose.material.icons.filled.DocumentScanner
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
        BottomNavItem("Scan", Icons.Filled.DocumentScanner, Navigation.SCAN),
        BottomNavItem("Analytics", Icons.Filled.Analytics, Navigation.ADMIN_ANALYTICS),
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
                
                // Identify the "Scan" or "Search" item for special styling if needed
                val isFloatingAction = item.route == Navigation.SCAN || (item.route == Navigation.SEARCH && userRole != Role.ADMIN) // Example condition

                NavigationBarItem(
                    selected = selected,
                    enabled = true,
                    alwaysShowLabel = item.route != Navigation.SEARCH && item.route != Navigation.SCAN,
                    onClick = {
                        val target = if (item.route == Navigation.HOME) {
                            "${Navigation.HOME}?showPrivacy=false"
                        } else item.route

                        navController.navigate(target) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(Navigation.HOME + "?showPrivacy={showPrivacy}") { saveState = true }
                        }
                    },
                    icon = {
                        // Apply special styling for Scan button for Admin, or Search for User if desired.
                        // Based on previous user request, Admin Scan was prominent.
                        if (item.route == Navigation.SCAN) {
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
                        } else if (item.route == Navigation.SEARCH && userRole != Role.ADMIN) {
                             // User search floating button
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
                        if (item.route != Navigation.SEARCH && item.route != Navigation.SCAN) {
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
