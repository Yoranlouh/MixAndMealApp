package com.example.mixandmealapp.ui.screens.upload

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mixandmealapp.ui.theme.BrandGreen
import com.example.mixandmealapp.ui.theme.BrandGrey
import com.example.mixandmealapp.ui.theme.BrandOrange
import com.example.mixandmealapp.ui.theme.DarkText
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme

data class Ingredient(
    val name: String,
    var quantity: String = "",
    var unit: String = ""
)

@Composable
fun UploadScreen(navController: NavHostController) {
    var recipeName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDifficulty by remember { mutableStateOf("Easy") }
    var cookingDuration by remember { mutableFloatStateOf(30f) }
    val ingredients = remember { mutableStateListOf<Ingredient>() }
    var newIngredientName by remember { mutableStateOf("") }
    
    // Kitchen Style states
    var selectedKitchenStyles by remember { mutableStateOf(setOf<String>()) }
    
    // Meal Type states
    var selectedMealTypes by remember { mutableStateOf(setOf<String>()) }
    
    // Allergens states
    var selectedAllergens by remember { mutableStateOf(setOf<String>()) }
    
    // Diet states
    var selectedDiets by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Cancel button
        Text(
            text = "Cancel",
            style = MaterialTheme.typography.bodyLarge,
            color = BrandOrange,
            modifier = Modifier.clickable { /* TODO: Handle cancel */ }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Add Cover Photo Section
        AddCoverPhotoSection()
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Recipe Name
        Text(
            text = "Recipe Name",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = DarkText
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = recipeName,
            onValueChange = { recipeName = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter recipe name") },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = BrandGrey,
                focusedBorderColor = BrandOrange
            ),
            shape = RoundedCornerShape(12.dp)
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Description
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = DarkText
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text("Tell a little about your recipe") },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = BrandGrey,
                focusedBorderColor = BrandOrange
            ),
            shape = RoundedCornerShape(12.dp),
            maxLines = 5
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Difficulty
        Text(
            text = "Difficulty",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = DarkText
        )
        Spacer(modifier = Modifier.height(12.dp))
        DifficultySelector(
            selectedDifficulty = selectedDifficulty,
            onDifficultySelected = { selectedDifficulty = it }
        )
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // Cooking Duration
        Text(
            text = "Cooking Duration",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = DarkText
        )
        Text(
            text = "(in minutes)",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(12.dp))
        CookingDurationSlider(
            duration = cookingDuration,
            onDurationChange = { cookingDuration = it }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Ingredients Section
        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = DarkText
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        // Ingredient List
        ingredients.forEach { ingredient ->
            IngredientItem(
                ingredient = ingredient,
                onQuantityChange = { newQuantity -> 
                    val index = ingredients.indexOf(ingredient)
                    if (index != -1) {
                        ingredients[index] = ingredient.copy(quantity = newQuantity)
                    }
                },
                onUnitChange = { newUnit -> 
                    val index = ingredients.indexOf(ingredient)
                    if (index != -1) {
                        ingredients[index] = ingredient.copy(unit = newUnit)
                    }
                },
                onRemove = { ingredients.remove(ingredient) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        
        // Add new ingredient field
        OutlinedTextField(
            value = newIngredientName,
            onValueChange = { newIngredientName = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter ingredient") },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = BrandGrey,
                focusedBorderColor = BrandOrange
            ),
            shape = RoundedCornerShape(12.dp)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Add Ingredient Button
        OutlinedButton(
            onClick = {
                if (newIngredientName.isNotBlank()) {
                    ingredients.add(Ingredient(newIngredientName))
                    newIngredientName = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = BrandOrange
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, BrandOrange)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ingredient")
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Kitchen Style Section
        FilterSection(
            title = "Kitchen Style",
            options = listOf(
                "Asian", "Chinese", "Dutch", "East-europe", "French", "Greek",
                "Indian", "Italian", "Japanese", "Korean", "Mediterranean",
                "Mexican", "Spanish", "Thai", "Turkish", "Vietnamese"
            ),
            selectedOptions = selectedKitchenStyles,
            onOptionToggle = { option ->
                selectedKitchenStyles = if (selectedKitchenStyles.contains(option)) {
                    selectedKitchenStyles - option
                } else {
                    selectedKitchenStyles + option
                }
            }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Meal Type Section
        FilterSection(
            title = "Meal Type",
            options = listOf("Breakfast", "Lunch", "Dinner", "Dessert"),
            selectedOptions = selectedMealTypes,
            onOptionToggle = { option ->
                selectedMealTypes = if (selectedMealTypes.contains(option)) {
                    selectedMealTypes - option
                } else {
                    selectedMealTypes + option
                }
            }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Allergens Section
        FilterSection(
            title = "Allergens",
            options = listOf(
                "Gluten", "Shellfish", "Eggs", "Fish", "Peanuts", "Soy",
                "Milk", "Nuts", "Celery", "Mustard", "Sesame", "Lupin",
                "Sulphites", "Molluscs"
            ),
            selectedOptions = selectedAllergens,
            onOptionToggle = { option ->
                selectedAllergens = if (selectedAllergens.contains(option)) {
                    selectedAllergens - option
                } else {
                    selectedAllergens + option
                }
            }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Diet Section
        FilterSection(
            title = "Diet",
            options = listOf(
                "Vegan", "Vegetarian", "Gluten free", "Lactose free",
                "Nut free", "Diary free", "Low sugar", "Low salt", "Halal",
                "Kosher", "Paleo", "Flexitarian", "Raw food", "Keto"
            ),
            selectedOptions = selectedDiets,
            onOptionToggle = { option ->
                selectedDiets = if (selectedDiets.contains(option)) {
                    selectedDiets - option
                } else {
                    selectedDiets + option
                }
            }
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Upload Button
        Button(
            onClick = { /* TODO: Handle upload */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandGreen),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Upload",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun AddCoverPhotoSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .border(2.dp, BrandGrey, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
            .clickable { /* TODO: Handle photo selection */ },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "Add photo",
                modifier = Modifier.size(64.dp),
                tint = Color(0xFF9E9E9E)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Add Cover Photo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF5B6B7C)
            )
            Text(
                text = "(up to 12 Mb)",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    // Camera icon below
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF6B7C8E))
                .clickable { /* TODO: Handle camera */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CameraAlt,
                contentDescription = "Camera",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun DifficultySelector(
    selectedDifficulty: String,
    onDifficultySelected: (String) -> Unit
) {
    val difficulties = listOf("Easy", "Medium", "Hard")
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        difficulties.forEach { difficulty ->
            val isSelected = difficulty == selectedDifficulty
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) BrandGreen else BrandGrey,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) Color.White else Color.Transparent)
                    .clickable { onDifficultySelected(difficulty) },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            tint = BrandGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = difficulty,
                        color = if (isSelected) BrandGreen else DarkText,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
fun CookingDurationSlider(
    duration: Float,
    onDurationChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("<10", color = BrandOrange, fontWeight = FontWeight.Bold)
            Text("30", color = BrandOrange, fontWeight = FontWeight.Bold)
            Text(">60", color = BrandOrange, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Slider(
            value = duration,
            onValueChange = onDurationChange,
            valueRange = 10f..60f,
            colors = SliderDefaults.colors(
                thumbColor = BrandOrange,
                activeTrackColor = BrandOrange,
                inactiveTrackColor = BrandGrey
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun IngredientItem(
    ingredient: Ingredient,
    onQuantityChange: (String) -> Unit,
    onUnitChange: (String) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Quantity input with +/- buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (ingredient.unit.isNotEmpty()) {
                    Text(
                        text = ingredient.unit,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
                
                IconButton(
                    onClick = {
                        val currentQty = ingredient.quantity.toIntOrNull() ?: 0
                        if (currentQty > 0) {
                            onQuantityChange((currentQty - 1).toString())
                        }
                    },
                    modifier = Modifier
                        .size(32.dp)
                        .border(1.dp, BrandOrange, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = "Decrease",
                        tint = BrandOrange,
                        modifier = Modifier.size(16.dp)
                    )
                }
                
                Text(
                    text = ingredient.quantity.ifEmpty { "0" },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.widthIn(min = 24.dp),
                    textAlign = TextAlign.Center
                )
                
                IconButton(
                    onClick = {
                        val currentQty = ingredient.quantity.toIntOrNull() ?: 0
                        onQuantityChange((currentQty + 1).toString())
                    },
                    modifier = Modifier
                        .size(32.dp)
                        .background(BrandOrange, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Increase",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            // Remove button
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Remove",
                    tint = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSection(
    title: String,
    options: List<String>,
    selectedOptions: Set<String>,
    onOptionToggle: (String) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "▼",
                color = BrandOrange,
                fontSize = 12.sp
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Create rows with chips
        val rows = options.chunked(3)
        rows.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { option ->
                    val isSelected = selectedOptions.contains(option)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onOptionToggle(option) },
                        label = { Text(option) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandOrange,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = DarkText
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = BrandGrey,
                            selectedBorderColor = BrandOrange,
                            borderWidth = 1.dp,
                            selectedBorderWidth = 1.dp
                        ),
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
                // Add empty space for remaining slots in row
                repeat(3 - row.size) {
                    Spacer(modifier = Modifier.weight(1f, fill = false))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true, heightDp = 2000)
@Composable
fun UploadScreenPreview() {
    MixAndMealAppTheme {
        UploadScreen(navController = rememberNavController())
    }
}