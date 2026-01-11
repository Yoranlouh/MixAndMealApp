package com.example.mixandmealapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.mixandmealapp.models.responses.RecipeCardResponse
import com.example.mixandmealapp.repository.RecipeRepository
import com.example.mixandmealapp.ui.components.PopularRecipeCard
import com.example.mixandmealapp.ui.components.PrivacyDialog
import com.example.mixandmealapp.ui.navigation.Navigation
import com.example.mixandmealapp.ui.theme.BrandOrange
import com.example.mixandmealapp.ui.theme.BrandYellow
import com.example.mixandmealapp.ui.theme.DarkText
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme
import com.example.mixandmealapp.ui.viewmodel.HomeViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.material.icons.filled.AccessTime

@Composable
fun HomeScreen(
    navController: NavController,
    showPrivacy: Boolean,
    onAcceptPrivacy: () -> Unit = {},
    viewModel: HomeViewModel = koinViewModel()
) {
    var openDialog by rememberSaveable { mutableStateOf(showPrivacy) }


    if (openDialog) {
        PrivacyDialog(
            onAccept = {
                openDialog = false
                onAcceptPrivacy()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Header(viewModel)
        Spacer(modifier = Modifier.height(24.dp))
        FeaturedSection(onRecipeClick = { navController.navigate(Navigation.RECIPE_DETAIL) })
        Spacer(modifier = Modifier.height(24.dp))
        CategorySection(navController = navController)
        Spacer(modifier = Modifier.height(24.dp))
        PopularRecipesSection(onRecipeClick = { navController.navigate(Navigation.RECIPE_DETAIL) })
        QuickRecipesSection(onRecipeClick = { navController.navigate(Navigation.RECIPE_DETAIL) })
        EasyRecipesSection(onRecipeClick = { navController.navigate(Navigation.RECIPE_DETAIL) })
    }
}

@Composable
fun Header(
    viewModel: HomeViewModel = koinViewModel()
) {
    var user = viewModel.role.collectAsState()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.WbSunny, contentDescription = "Sun icon", tint = BrandYellow)
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = stringResource(id = com.example.mixandmealapp.R.string.hello), style = MaterialTheme.typography.bodyLarge)
            }
            Text(text = user.value.userName , style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FeaturedSection(onRecipeClick: () -> Unit = {}) {
    val recipeRepository = RecipeRepository()
    var recipeCard by remember { mutableStateOf<RecipeCardResponse?>(null) }
    LaunchedEffect(Unit) {
        try {
            recipeCard = recipeRepository.getFeaturedRecipeCard()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column {
        Text(
            text = stringResource(id = com.example.mixandmealapp.R.string.featured),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Placeholder for the large featured card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { onRecipeClick() },
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                val imageUrl = recipeCard?.imageUrl?.firstOrNull()?.imageUrl
                    ?: "https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg"
                
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient overlay for text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                startY = 300f
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    if (recipeCard != null) {
                        Text(
                            text = recipeCard!!.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${recipeCard!!.cookingTime} min",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun CategorySection(navController: NavController) {
    val categories = listOf(
        stringResource(id = com.example.mixandmealapp.R.string.search_filter_breakfast),
        stringResource(id = com.example.mixandmealapp.R.string.search_filter_lunch),
        stringResource(id = com.example.mixandmealapp.R.string.search_filter_dinner),
        stringResource(id = com.example.mixandmealapp.R.string.search_filter_dessert)
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = com.example.mixandmealapp.R.string.category),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                Button(
                    onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("mealTypes", listOf(category))
                        navController.navigate(Navigation.SEARCH_RESULTS)
                    },
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandOrange,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(category, fontSize = 12.sp)
                }
            }
        }
    }
}


@Composable
fun PopularRecipesSection(onRecipeClick: () -> Unit = {}) {
    val recipeRepository = RecipeRepository()
    var recipes by remember { mutableStateOf<List<RecipeCardResponse>>(listOf<RecipeCardResponse>()) }
    LaunchedEffect(Unit) {
        try {
            recipes = recipeRepository.getPopularRecipesResponse(2)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = com.example.mixandmealapp.R.string.popular_recipes),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(text = stringResource(id = com.example.mixandmealapp.R.string.see_all), style = MaterialTheme.typography.bodyMedium, color = BrandOrange)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(
                16.dp,
                alignment = Alignment.CenterHorizontally
            ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(recipes) { recipe ->
                PopularRecipeCard(recipe, onClick = onRecipeClick)
            }
        }
    }
}

@Composable
fun QuickRecipesSection(onRecipeClick: () -> Unit = {}) {
    val recipeRepository = RecipeRepository()
    var recipes by remember { mutableStateOf<List<RecipeCardResponse>>(listOf<RecipeCardResponse>()) }
    LaunchedEffect(Unit) {
        try {
            recipes = recipeRepository.getQuickRecipesResponse(2)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Spacer(Modifier.height(16.dp))
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = com.example.mixandmealapp.R.string.quick_recipes),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(text = stringResource(id = com.example.mixandmealapp.R.string.see_all), style = MaterialTheme.typography.bodyMedium, color = BrandOrange)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(
                16.dp,
                alignment = Alignment.CenterHorizontally
            ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(recipes) { recipe ->
                PopularRecipeCard(recipe, onClick = onRecipeClick)
            }
        }
    }
}

@Composable
fun EasyRecipesSection(onRecipeClick: () -> Unit = {}) {
    val recipeRepository = RecipeRepository()
    var recipes by remember { mutableStateOf<List<RecipeCardResponse>>(listOf<RecipeCardResponse>()) }
    LaunchedEffect(Unit) {
        try {
            recipes = recipeRepository.getEasyRecipesResponse(2)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    Spacer(Modifier.height(16.dp))
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = com.example.mixandmealapp.R.string.easy_recipes),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(text = stringResource(id = com.example.mixandmealapp.R.string.see_all), style = MaterialTheme.typography.bodyMedium, color = BrandOrange)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(
                16.dp,
                alignment = Alignment.CenterHorizontally
            ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(recipes) { recipe ->
                PopularRecipeCard(recipe, onClick = onRecipeClick)
            }
        }
    }
}
