package com.example.mixandmealapp.ui.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavBar(navController: NavHostController, currentDestination: NavDestination?) {

    val items = listOf(
        BottomNavItem("Home", Icons.Filled.Home, Navigation.HOME),
        BottomNavItem("Upload", Icons.Filled.Edit, "upload"),
        BottomNavItem("Scan", Icons.Filled.DocumentScanner, "scan"),
        BottomNavItem("Search", Icons.Filled.Search, "search"),
        BottomNavItem("Profile", Icons.Filled.Person, "profile")
    )

    BottomAppBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        cutoutShape = CircleShape
    ) {
        items.forEach { item ->
            val isScanItem = item.route == "scan"
            val selected =
                currentDestination?.hierarchy?.any { it.route == item.route } == true && !isScanItem

            NavigationBarItem(
                selected = selected,
                enabled = !isScanItem, // The Scan item is not clickable here
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(Navigation.HOME) { saveState = true }
                    }
                },
                icon = {
                     // The icon for the scan item is handled by the FAB
                    if (!isScanItem) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    }
                },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BrandGreen,
                    selectedTextColor = BrandGreen,
                    unselectedIconColor = BrandGrey,
                    unselectedTextColor = BrandGrey,
                    // Style the disabled "Scan" item to only show its label
                    disabledTextColor = BrandGrey,
                    disabledIconColor = Color.Transparent,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    MixAndMealAppTheme {
        BottomNavBar(
            navController = rememberNavController(),
            currentDestination = null
        )
    }
}