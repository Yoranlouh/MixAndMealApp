package com.example.mixandmealapp.ui.screens.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mixandmealapp.ui.components.FavoriteIcon
import com.example.mixandmealapp.ui.components.LabelFridge
import com.example.mixandmealapp.ui.components.OpenFridgeButton
import com.example.mixandmealapp.ui.components.PrimaryButton
import com.example.mixandmealapp.ui.theme.BrandOrange
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme
import com.example.mixandmealapp.ui.viewmodel.FridgeViewModel
import com.example.mixandmealapp.ui.viewmodel.FavouritesViewModel
import androidx.compose.runtime.LaunchedEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onLogout: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onGoToSettings: () -> Unit = {},
    onGoToLogin: () -> Unit = {},
    fridgeViewModel: FridgeViewModel? = null,
    favouritesViewModel: FavouritesViewModel? = null,
    navController: NavHostController,
    isLoggedIn: Boolean = true // Default to true to show logged-in state
) {
    val vm = fridgeViewModel ?: remember { FridgeViewModel() }
    val favVm = favouritesViewModel ?: remember { FavouritesViewModel() }
    LaunchedEffect(favVm) { favVm.load() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Account",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    IconButton(onClick = onGoToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoggedIn) {
                ProfileCard(name = "Richard Balke", onEditProfile = onEditProfile)
                Spacer(modifier = Modifier.height(32.dp))
                MyFavoritesSection(
                    viewModel = favVm,
                    onNavigateToFavourites = { navController.navigate(com.example.mixandmealapp.ui.navigation.Navigation.FAVOURITES) },
                    onRecipeClick = { navController.navigate(com.example.mixandmealapp.ui.navigation.Navigation.RECIPE_DETAIL) }
                )
                Spacer(modifier = Modifier.height(32.dp))
                MyFridgeSection(
                    items = vm.uiState.items.map { it.name },
                    count = vm.uiState.items.size,
                    onRemove = { name ->
                        // find by name (demo). In real app use id; here we map first match
                        val item = vm.uiState.items.firstOrNull { it.name == name }
                        item?.let { vm.removeItem(it.id) }
                    },
                    onNavigateToFridge = { navController.navigate(com.example.mixandmealapp.ui.navigation.Navigation.FRIDGE) }
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(id = com.example.mixandmealapp.R.string.welcome_user), style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(
                        text = stringResource(id = com.example.mixandmealapp.R.string.login_or_signup),
                        modifier = Modifier.fillMaxWidth(0.8f),
                        onClick = onGoToLogin
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileCard(name: String, onEditProfile: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEditProfile),
        shape = MaterialTheme.shapes.large,
        shadowElevation = 4.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Edit Profile",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun MyFavoritesSection(
    viewModel: FavouritesViewModel,
    onNavigateToFavourites: () -> Unit = {},
    onRecipeClick: (String) -> Unit = {}
) {
    // Observe shared favourites and only show up to 4 on Account
    val favourites = viewModel.uiState.favourites.take(4)
    var pendingDeleteTitle by remember { mutableStateOf<String?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "My Favorites", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "View All",
                color = BrandOrange,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onNavigateToFavourites() }
            )
        }

        // Display in two columns
        for (row in favourites.chunked(2)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FavoriteRecipeCard(
                    title = row.getOrNull(0) ?: "",
                    modifier = Modifier.weight(1f),
                    onClick = { onRecipeClick(row.getOrNull(0) ?: "") },
                    onUnfavoriteRequested = {
                        row.getOrNull(0)?.let { pendingDeleteTitle = it }
                    }
                )
                if (row.size > 1) {
                    FavoriteRecipeCard(
                        title = row[1],
                        modifier = Modifier.weight(1f),
                        onClick = { onRecipeClick(row[1]) },
                        onUnfavoriteRequested = { pendingDeleteTitle = row[1] }
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // Confirmation dialog for removing from favourites (same as FavouritesScreen)
        val confirmMessage = stringResource(id = com.example.mixandmealapp.R.string.confirm_remove_favourite_message)
        val yes = stringResource(id = com.example.mixandmealapp.R.string.yes)
        val no = stringResource(id = com.example.mixandmealapp.R.string.no)
        if (pendingDeleteTitle != null) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { pendingDeleteTitle = null },
                title = { Text(text = confirmMessage) },
                confirmButton = {
                    androidx.compose.material3.TextButton(onClick = {
                        pendingDeleteTitle?.let { title -> viewModel.remove(title) }
                        pendingDeleteTitle = null
                    }) {
                        Text(text = yes)
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { pendingDeleteTitle = null }) {
                        Text(text = no)
                    }
                }
            )
        }
    }
}

@Composable
private fun MyFridgeSection(
    items: List<String>,
    count: Int,
    onRemove: (String) -> Unit,
    onNavigateToFridge: () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = stringResource(id = com.example.mixandmealapp.R.string.my_fridge), style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.width(8.dp))
                Text(text = stringResource(id = com.example.mixandmealapp.R.string.items_count, count), style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
            Text(
                text = stringResource(id = com.example.mixandmealapp.R.string.view_all),
                color = BrandOrange,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onNavigateToFridge() }
            )
        }

        // Show maximum 4 items as labels with only a trash icon.
        items.take(4).forEach { name ->
            LabelFridge(label = name, onRemove = { onRemove(name) })
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Big brand orange button
        OpenFridgeButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigateToFridge
        )
    }
}


@Composable
private fun FridgeItem(name: String, quantity: Int) {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = name, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val iconButtonSize = 28.dp
                Surface(
                    onClick = { /* TODO: decrease quantity */ },
                    shape = CircleShape,
                    border = BorderStroke(1.dp, BrandOrange),
                    modifier = Modifier.size(iconButtonSize)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease quantity",
                            tint = BrandOrange,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Text(
                    text = quantity.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    onClick = { /* TODO: increase quantity */ },
                    shape = CircleShape,
                    border = BorderStroke(1.dp, BrandOrange),
                    modifier = Modifier.size(iconButtonSize)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase quantity",
                            tint = BrandOrange,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun FavoriteRecipeCard(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onUnfavoriteRequested: () -> Unit = {}
) {
    Surface(
        modifier = modifier.height(180.dp),
        shape = MaterialTheme.shapes.large,
        color = Color.White,
        shadowElevation = 4.dp,
        onClick = onClick
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF5F5F5)
                ) {}

                val fav = remember { mutableStateOf(true) }
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    FavoriteIcon(isFavorite = fav.value) {
                        // Use the same UX as FavouritesScreen: ask confirmation via parent
                        onUnfavoriteRequested()
                    }
                }
            }

            // Fixed-height title bar so that short titles reserve the
            // same vertical space as long (2-line) titles. This keeps
            // all favorite cards with identical proportions.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // fits up to 2 lines of bodyMedium comfortably
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0A2533),
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.TopStart)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Account Screen (Logged In)")
@Composable
fun AccountScreenLoggedInPreview() {
    MixAndMealAppTheme {
        AccountScreen(
            navController = rememberNavController(),
            isLoggedIn = true
        )
    }
}

@Preview(showBackground = true, name = "Account Screen (Logged Out)")
@Composable
fun AccountScreenLoggedOutPreview() {
    MixAndMealAppTheme {
        AccountScreen(
            navController = rememberNavController(),
            isLoggedIn = false
        )
    }
}
