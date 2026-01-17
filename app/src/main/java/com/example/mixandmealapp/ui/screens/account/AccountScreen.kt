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
import androidx.compose.foundation.layout.FlowRow
import com.example.mixandmealapp.ui.components.LabelFridge
import androidx.compose.material3.SuggestionChip
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
import com.example.mixandmealapp.ui.components.PrimaryButton
import com.example.mixandmealapp.ui.theme.BrandOrange
import com.example.mixandmealapp.ui.components.PopularRecipeCard
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme
import com.example.mixandmealapp.ui.viewmodel.FridgeViewModel
import com.example.mixandmealapp.ui.viewmodel.FavouritesViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.example.mixandmealapp.ui.viewmodel.AccountViewModel
import com.example.mixandmealapp.models.entries.AllergenEntry
import com.example.mixandmealapp.models.entries.DietEntry
import com.example.mixandmealapp.models.requests.RecipeIDRequest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onLogout: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onGoToSettings: () -> Unit = {},
    onGoToLogin: () -> Unit = {},
    onNavigateToAllergens: () -> Unit = {},
    onNavigateToDiets: () -> Unit = {},
    fridgeViewModel: FridgeViewModel = koinViewModel(),
    favouritesViewModel: FavouritesViewModel = koinViewModel(),
    navController: NavHostController,
    isLoggedIn: Boolean = true, // Default to true to show logged-in state
    accountViewModel: AccountViewModel = koinViewModel()
) {
    val vm = fridgeViewModel
    val accountState by accountViewModel.uiState.collectAsState()

    LaunchedEffect(favouritesViewModel) { favouritesViewModel.load() }
    LaunchedEffect(Unit) { accountViewModel.load() }

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
                MyAllergensSection(
                    userAllergens = accountState.userAllergens,
                    onEdit = onNavigateToAllergens,
                    onRemove = { accountViewModel.removeAllergen(it) }
                )
                Spacer(modifier = Modifier.height(32.dp))

                MyDietsSection(
                    userDiets = accountState.userDiets,
                    onEdit = onNavigateToDiets,
                    onRemove = { accountViewModel.removeDiet(it) }
                )
                Spacer(modifier = Modifier.height(32.dp))

                MyFavoritesSection(
                    viewModel = favouritesViewModel,
                    onNavigateToFavourites = { navController.navigate(com.example.mixandmealapp.ui.navigation.Navigation.FAVOURITES) },
                    onRecipeClick = { recipeId ->
                        navController.navigate("${com.example.mixandmealapp.ui.navigation.Navigation.RECIPE_DETAIL}/$recipeId")
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))

                MyFridgeSection(
                    count = vm.uiState.items.size,
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
fun MyAllergensSection(
    userAllergens: List<AllergenEntry>,
    onEdit: () -> Unit,
    onRemove: (AllergenEntry) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "My Allergens", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Edit",
                color = BrandOrange,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onEdit() }
            )
        }

        if (userAllergens.isEmpty()) {
            Text(
                text = "Geen allergenen geselecteerd",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                userAllergens.forEach { allergen ->
                    LabelFridge(
                        label = allergen.displayName,
                        onRemove = { onRemove(allergen) }
                    )
                }
            }
        }
    }
}

@Composable
fun MyDietsSection(
    userDiets: List<DietEntry>,
    onEdit: () -> Unit,
    onRemove: (DietEntry) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "My Diets", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Edit",
                color = BrandOrange,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onEdit() }
            )
        }

        if (userDiets.isEmpty()) {
            Text(
                text = "Geen dieetvoorkeuren ingesteld",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                userDiets.forEach { diet ->
                    LabelFridge(
                        label = diet.displayName,
                        onRemove = { onRemove(diet) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyFavoritesSection(
    viewModel: FavouritesViewModel,
    onNavigateToFavourites: () -> Unit = {},
    onRecipeClick: (Int) -> Unit = {}
) {
    // Observe shared favourites and only show up to 4 on Account
    val favourites = viewModel.uiState.favourites.take(4)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "My Favourites", style = MaterialTheme.typography.titleLarge)
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
                    recipeId = row[0].recipeId,
                    title = row[0].title,
                    modifier = Modifier.weight(1f),
                    onClick = { onRecipeClick(row[0].recipeId) },
                    onToggleFavorite = {
                        viewModel.toggleFavourite(RecipeIDRequest(row[0].recipeId))
                    }
                )
                if (row.size > 1) {
                    FavoriteRecipeCard(
                        recipeId = row[1].recipeId,
                        title = row[1].title,
                        modifier = Modifier.weight(1f),
                        onClick = { onRecipeClick(row[1].recipeId) },
                        onToggleFavorite = {
                            viewModel.toggleFavourite(RecipeIDRequest(row[1].recipeId))
                        }
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun MyFridgeSection(
    count: Int,
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

        // Show only the count.
        // Individual items and the open fridge button are no longer shown here as per requirements.
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
    recipeId : Int,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onToggleFavorite: () -> Unit = {}
) {
    // Delegate to the unified recipe card component
    PopularRecipeCard(
        title = title,
        description = null,
        cookingTimeMinutes = null,
        imageUrl = null,
        onClick = onClick,
        modifier = modifier
            .height(240.dp),
        isFavorite = true,
        onToggleFavorite = onToggleFavorite
    )
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
