package com.example.mixandmealapp.ui.screens.recipes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mixandmealapp.R
import com.example.mixandmealapp.models.responses.FullRecipeScreenResponse
import com.example.mixandmealapp.models.responses.RecipeCardResponse
import com.example.mixandmealapp.repository.RecipeRepository
import com.example.mixandmealapp.ui.components.PrimaryButton
import com.example.mixandmealapp.ui.theme.BrandGreen
import com.example.mixandmealapp.ui.theme.BrandGrey
import com.example.mixandmealapp.ui.theme.BrandOrange
import com.example.mixandmealapp.ui.theme.BrandYellow
import com.example.mixandmealapp.ui.theme.LightBackground
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme
import models.dto.IngredientUnitEntry
import coil.compose.AsyncImage


data class Ingredient(val name: String, val qty: String)

@Composable
fun RecipeDetailScreen(
    onBack: () -> Unit = {},
    onToggleFavorite: (Boolean) -> Unit = {},
    onSave: () -> Unit = {}
) {
    val recipeRepository = RecipeRepository()
    var recipe by remember { mutableStateOf<FullRecipeScreenResponse?>(null) }
    LaunchedEffect(Unit) {
        try {
            recipe = recipeRepository.getFullRecipeResponse(1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    var title: String = "Not found"
    var minutes = 0
    var difficulty = "Not found"
    var description = "Not found"
    var instructions = "Not found"
    var image = "https://dumpvanplaatjes.nl/mix-and-meal/default-image.jpg"
    var isFavorite by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) } // 0 = Ingredients, 1 = Instructions
    var descExpanded by remember { mutableStateOf(false) }
    var ingredients = listOf<IngredientUnitEntry>()
    if(recipe != null) {
        title = remember { recipe!!.title }
        minutes = remember { recipe!!.cookingTime }
        difficulty = remember { recipe!!.difficulty.name }
        description = remember { recipe!!.description }
        instructions = remember { recipe!!.instructions }
        image = remember { recipe!!.images[0].imageUrl }
        ingredients = remember {
            recipe!!.ingredients
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        // Header image with actions
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)) {
            AsyncImage(
                model = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                RoundIconButton(icon = Icons.Filled.KeyboardArrowLeft, onClick = onBack)
                RoundIconButton(
                    icon = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    tint = if (isFavorite) BrandOrange else Color.White
                ) {
                    isFavorite = !isFavorite
                    onToggleFavorite(isFavorite)
                }
            }
        }

        // Foreground sheet content
        Surface(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            tonalElevation = 2.dp,
            modifier = Modifier
                .fillMaxSize()
                .background(LightBackground)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            // Title row with inline duration and difficulty on the right of the title
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AccessTime,
                                        contentDescription = null,
                                        tint = BrandGrey,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "${minutes} Min", fontSize = 12.sp, color = BrandGrey)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    DifficultyLabel(text = difficulty)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            // Description with inline green "View More" that expands/collapses
                            val label = if (descExpanded) " View Less" else " View More"
                            val annotated: AnnotatedString = buildAnnotatedString {
                                append(description)
                                append(label)
                                addStringAnnotation(tag = "action", annotation = "toggle", start = description.length, end = description.length + label.length)
                                addStyle(
                                    style = SpanStyle(color = BrandGreen, fontWeight = FontWeight.Medium),
                                    start = description.length,
                                    end = description.length + label.length
                                )
                            }
                            ClickableText(
                                text = annotated,
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)),
                                maxLines = if (descExpanded) Int.MAX_VALUE else 2,
                                overflow = if (descExpanded) TextOverflow.Clip else TextOverflow.Ellipsis,
                                onClick = { offset ->
                                    annotated.getStringAnnotations(tag = "action", start = offset, end = offset)
                                        .firstOrNull()?.let { descExpanded = !descExpanded }
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Nutrition chips
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        NutrientChip(Icons.Filled.Grain, "65g carbs")
                        NutrientChip(Icons.Filled.WaterDrop, "27g proteins")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        NutrientChip(Icons.Filled.Grain, "120 Kcal")
                        NutrientChip(Icons.Filled.WaterDrop, "91g fats")
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    SegmentedTabs(selected = selectedTab, onSelected = { selectedTab = it })

                    Spacer(modifier = Modifier.height(12.dp))

                    if (selectedTab == 0) {
                        Text(stringResource(id = com.example.mixandmealapp.R.string.ingredients), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(stringResource(id = com.example.mixandmealapp.R.string.six_item), color = BrandGrey, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                    } else {
                        Text(stringResource(id = com.example.mixandmealapp.R.string.instructions), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(stringResource(id = com.example.mixandmealapp.R.string.four_steps), color = BrandGrey, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                if (selectedTab == 0) {
                    items(ingredients) { item ->
                        IngredientRow(item)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                } else {
                    // Simple placeholder instruction steps styling
                    items(listOf(instructions
                    )) { step ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(2.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(BrandGreen.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = BrandGreen, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(step, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    PrimaryButton(text = "Save Recipe", onClick = onSave)
                }
            }
        }
    }
}

@Composable
private fun RoundIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color = Color.White,
    onClick: () -> Unit
) {
    Surface(shape = CircleShape, color = Color.Black.copy(alpha = 0.35f)) {
        IconButton(onClick = onClick, modifier = Modifier.size(40.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = tint)
        }
    }
}

@Composable
private fun NutrientChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(icon, contentDescription = null, tint = BrandGreen, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun DifficultyLabel(text: String) {
    val (bg, fg) = when (text.lowercase()) {
        "easy" -> BrandGreen.copy(alpha = 0.12f) to BrandGreen
        "medium" -> BrandYellow.copy(alpha = 0.18f) to BrandYellow
        "hard" -> BrandOrange.copy(alpha = 0.15f) to BrandOrange
        else -> LightBackground to BrandGrey
    }
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bg,
        tonalElevation = 0.dp
    ) {
        Text(
            text = text,
            color = fg,
            fontSize = 12.sp,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun SegmentedTabs(selected: Int, onSelected: (Int) -> Unit) {
    // Light, slightly bluish background to match the visual from the screenshot
    val containerColor = Color(0xFFEAF1F7)
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = containerColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SegmentTab(text = "Ingredients", selected = selected == 0) { onSelected(0) }
            }
            Box(modifier = Modifier.weight(1f)) {
                SegmentTab(text = "Instructions", selected = selected == 1) { onSelected(1) }
            }
        }
    }
}

@Composable
private fun SegmentTab(text: String, selected: Boolean, onClick: () -> Unit) {
    // Each tab takes 50% of the width. We center the content and give the
    // selected chip a fixed height and generous rounding to match the spec.
    val chipShape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Surface(
                color = BrandGreen,
                shape = chipShape,
                shadowElevation = 0.dp,
                modifier = Modifier
                    .matchParentSize()
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {}
        }
        Text(
            text = text,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}

@Composable
private fun IngredientRow(ingredient: IngredientUnitEntry) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(ingredient.ingredientName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Text("${ingredient.amount} ${ingredient.unitType}", color = BrandGrey)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailScreenPreview() {
    MixAndMealAppTheme {
        RecipeDetailScreen()
    }
}
