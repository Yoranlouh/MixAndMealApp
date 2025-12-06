package com.example.mixandmealapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.sp
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
fun BottomNavBarUser(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    Box {
        // 1️⃣ Navigation Bar behind
        NavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(80.dp),
            containerColor = Color.White,
            tonalElevation = 8.dp
        ) {
            val items = listOf(
                BottomNavItem("Home", Icons.Filled.Home, Navigation.HOME),
                BottomNavItem("Profile", Icons.Filled.Person, Navigation.ACCOUNT)
            )

            items.forEach { item ->
                val selected =
                    currentDestination?.hierarchy?.any { it.route == item.route } == true

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                        }
                    },
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(item.title) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BrandGreen,
                        selectedTextColor = BrandGreen,
                        unselectedIconColor = BrandGrey,
                        unselectedTextColor = BrandGrey,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }

        // 2️⃣ Floating Search button above the navbar with label
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-24).dp), // lift above the navbar
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .shadow(8.dp, CircleShape)
                        .background(BrandGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = { navController.navigate(Navigation.SEARCH) }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Search",
                    color = BrandGrey,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}





//@Composable
//fun BottomNavBar(navController: NavHostController, currentDestination: NavDestination?) {
//
//    val items = listOf(
//        BottomNavItem("Home", Icons.Filled.Home, Navigation.HOME),
//        BottomNavItem("Upload", Icons.Filled.Edit, Navigation.UPLOAD),
//        BottomNavItem("Scan", Icons.Filled.DocumentScanner, Navigation.SCAN),
//        BottomNavItem("Search", Icons.Filled.Search, Navigation.SEARCH),
//        BottomNavItem("Profile", Icons.Filled.Person, Navigation.ACCOUNT)
//    )
//
//    NavigationBar(
//        modifier = Modifier.height(120.dp), // hoogte zodat de Scan-knop netjes binnen de bar valt
//        containerColor = Color.White,
//        tonalElevation = 8.dp
//    ) {
//        items.forEach { item ->
//            val isScanItem = item.route == "scan"
//            val selected =
//                currentDestination?.hierarchy?.any { it.route == item.route } == true
//
//            NavigationBarItem(
//                selected = selected,
//                enabled = true,
//                alwaysShowLabel = true,
//                onClick = {
//                    navController.navigate(item.route) {
//                        launchSingleTop = true
//                        restoreState = true
//                        popUpTo(navController.graph.startDestinationId) { saveState = true }
//                    }
//                },
//                icon = {
//                    if (isScanItem) {
//                        Box(
//                            modifier = Modifier
//                                .padding(top = 8.dp) // minder ruimte nodig
//                                .size(64.dp) // groot en prominent, maar passend
//                                .offset(y = (-8).dp) // licht omhoog, blijft binnen de bar
//                                .shadow(elevation = 8.dp, shape = CircleShape)
//                                .background(color = BrandGreen, shape = CircleShape),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Icon(
//                                imageVector = Icons.Filled.DocumentScanner,
//                                contentDescription = item.title,
//                                tint = Color.White
//                            )
//                        }
//                    } else {
//                        Icon(
//                            imageVector = item.icon,
//                            contentDescription = item.title
//                        )
//                    }
//                },
//                label = {
//                    if (isScanItem) {
//                        Text(item.title, modifier = Modifier.offset(y = (-2).dp)) // label iets dichter naar het icoon
//                    } else {
//                        Text(item.title)
//                    }
//                },
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = BrandGreen,
//                    selectedTextColor = BrandGreen,
//                    unselectedIconColor = BrandGrey,
//                    unselectedTextColor = BrandGrey,
//                    indicatorColor = Color.Transparent
//                )
//            )
//        }
//    }
//}


@Composable
fun BottomNavBarAdmin(
    navController: NavHostController,
    currentDestination: NavDestination?
) {

    val items = listOf(
        BottomNavItem("Home", Icons.Filled.Home, Navigation.HOME_ADMIN),

        // Upload — only available for admins
        BottomNavItem("Upload", Icons.Filled.Edit, Navigation.UPLOAD_ADMIN),

        BottomNavItem("Scan", Icons.Filled.DocumentScanner, Navigation.SCAN_ADMIN),

        // Analytics instead of Search
        BottomNavItem("Analytics", Icons.Filled.BarChart, Navigation.ANALYTICS),

        BottomNavItem("Profile", Icons.Filled.Person, Navigation.ACCOUNT_ADMIN)
    )

    NavigationBar(
        modifier = Modifier.height(120.dp),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->

            val isScanItem = item.title == "Scan"

            val selected = currentDestination?.hierarchy?.any {
                it.route == item.route
            } == true

            NavigationBarItem(
                selected = selected,
                enabled = true,
                alwaysShowLabel = true,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                },
                icon = {
                    if (isScanItem) {
                        // Same big scan button style as user version
                        Box(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .size(64.dp)
                                .offset(y = (-8).dp)
                                .shadow(8.dp, CircleShape)
                                .background(BrandGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.DocumentScanner,
                                contentDescription = item.title,
                                tint = Color.White
                            )
                        }
                    } else {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    }
                },
                label = {
                    if (isScanItem) {
                        Text(item.title, modifier = Modifier.offset(y = (-2).dp))
                    } else {
                        Text(item.title)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BrandGreen,
                    selectedTextColor = BrandGreen,
                    unselectedIconColor = BrandGrey,
                    unselectedTextColor = BrandGrey,
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
        val navController = rememberNavController()
        // Preview with "Home" selected to show the active state correctly
        val navDestination = NavDestination(Navigation.HOME).apply {
            this.route = Navigation.HOME
        }
        BottomNavBarUser(
            navController = navController,
            currentDestination = navDestination
        )
    }
}