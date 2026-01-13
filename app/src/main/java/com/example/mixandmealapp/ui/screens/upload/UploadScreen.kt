package com.example.mixandmealapp.ui.screens.upload

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.mixandmealapp.network.ApiService
import coil.compose.AsyncImage
import com.example.mixandmealapp.R
import com.example.mixandmealapp.models.entries.AllergenEntry
import com.example.mixandmealapp.models.entries.DietEntry
import com.example.mixandmealapp.models.entries.IngredientUnitEntry
import com.example.mixandmealapp.models.entries.RecipeImageEntry
import com.example.mixandmealapp.models.enums.Difficulty
import com.example.mixandmealapp.models.enums.KitchenStyle
import com.example.mixandmealapp.models.enums.MealType
import com.example.mixandmealapp.models.requests.RecipeUploadRequest
import com.example.mixandmealapp.ui.components.BackButton
import com.example.mixandmealapp.ui.components.InputFieldSmall
import com.example.mixandmealapp.ui.components.InputFieldTextBox
import com.example.mixandmealapp.ui.components.PrimaryButton
import com.example.mixandmealapp.ui.components.IngredientAutoCompleteField
import com.example.mixandmealapp.ui.navigation.Navigation
import com.example.mixandmealapp.ui.screens.search.FilterOptions
import com.example.mixandmealapp.ui.theme.BrandGreen
import com.example.mixandmealapp.ui.theme.BrandGrey
import com.example.mixandmealapp.ui.theme.BrandOrange
import com.example.mixandmealapp.ui.theme.BrandYellow
import com.example.mixandmealapp.ui.theme.DarkText

data class Ingredient(
    val name: String,
    var amount: Double? = null,
    var unitType: String = "",
    var isConfirmed: Boolean = false
)

@Composable
fun UploadScreen(
    navController: NavHostController,
    token: String?,
    onPhotoPick: (callback: (Uri?) -> Unit) -> Unit) {
    // Camera variables
    var coverPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val coroutineScope = rememberCoroutineScope()
    var recipeName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedDifficulty by remember { mutableStateOf("") }
    // Discrete cooking time options (in minutes) for slider steps
    // index 0 represents "<10"; show labels only for first, middle (30), and last
    val durationOptions = listOf(9, 15, 30, 45, 60)
    var cookingDurationIndex by remember { mutableIntStateOf(2) } // default 30 min
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

    // Expand/collapse states for filter sections
    var kitchenExpanded by remember { mutableStateOf(true) }
    var mealTypeExpanded by remember { mutableStateOf(true) }
    var allergensExpanded by remember { mutableStateOf(true) }
    var dietExpanded by remember { mutableStateOf(true) }

    var showSuccessDialog by remember { mutableStateOf(false) }

    // Translated filter options
    val kitchenStylesStrings = FilterOptions.kitchenStyles.map { stringResource(it) }
    val mealTypeStrings = FilterOptions.mealTypes.map { stringResource(it) }
    val allergensStrings = FilterOptions.allergens.map { stringResource(it) }
    val dietsStrings = FilterOptions.diets.map { stringResource(it) }



    if (showSuccessDialog) {
        UploadSuccessDialog(
            onDismiss = { showSuccessDialog = false },
            onBackToHome = {
                showSuccessDialog = false
                navController.navigate(Navigation.HOME) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        )
    }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Cancel button
        BackButton(
            navController = navController,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Add Cover Photo Section
        PhotoPicker(
            coverPhotoUri = coverPhotoUri,
            onPhotoClick = { onPhotoPick { uri -> coverPhotoUri = uri } },
            onPhotoRemove = { coverPhotoUri = null }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Recipe Name
        var recipeName by remember { mutableStateOf("") }

        InputFieldSmall(
            value = recipeName,
            onValueChange = { recipeName = it },
            label = stringResource(R.string.upload_enter_recipe_name_title),
            placeholder = stringResource(R.string.upload_enter_recipe_name),
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Description
        InputFieldTextBox(
            value = description,
            onValueChange = { description = it },
            label = stringResource(R.string.upload_tell_about_recipe_title),
            placeholder = stringResource(R.string.upload_tell_about_recipe)
        )


        Spacer(modifier = Modifier.height(20.dp))

        // Difficulty
        Text(
            text = stringResource(R.string.upload_difficulty_title),
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.upload_enter_cooking_duration),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
            Spacer(modifier = Modifier.width(8.dp))
            val durationText = if (cookingDurationIndex == 0) "(<10 minutes)" else "(${durationOptions[cookingDurationIndex]} minutes)"
            Text(
                text = durationText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        CookingDurationSlider(
            index = cookingDurationIndex,
            onIndexChange = { cookingDurationIndex = it },
            options = durationOptions
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Ingredients Section
        Text(
            text = stringResource(R.string.upload_enter_ingredient_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = DarkText
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Ingredient List
        ingredients.forEach { ingredient ->
            SimpleIngredientItem(
                ingredient = ingredient,
                onNameChange = { newName ->
                    val index = ingredients.indexOf(ingredient)
                    if (index != -1) {
                        ingredients[index] = ingredient.copy(name = newName)
                    }
                },
                onAmountChange = { newAmount ->
                    val index = ingredients.indexOf(ingredient)
                    if (index != -1) {
                        val parsed = newAmount.replace(",", ".").toDoubleOrNull()
                        ingredients[index] = ingredient.copy(amount = parsed)
                    }
                },
                onUnitTypeChange = { newUnit ->
                    val index = ingredients.indexOf(ingredient)
                    if (index != -1) {
                        ingredients[index] = ingredient.copy(unitType = newUnit)
                    }
                },
                onConfirm = {
                    // Bevestig alleen als er een hoeveelheid is ingevuld
                    val idx = ingredients.indexOf(ingredient)
                    if (idx != -1 && ingredients[idx].amount != null) {
                        ingredients[idx] = ingredient.copy(isConfirmed = true)
                        // Verwijder focus zodat de cursor verdwijnt en het veld niet actief blijft
                        focusManager.clearFocus(force = true)
                    }
                },
                onUnlock = {
                    // Maak deze rij opnieuw bewerkbaar
                    val idx = ingredients.indexOf(ingredient)
                    if (idx != -1) {
                        ingredients[idx] = ingredient.copy(isConfirmed = false)
                    }
                },
                onRemove = { ingredients.remove(ingredient) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Typen van ingrediëntnaam -> daarna via "+ toevoegen" om te bevestigen
        OutlinedTextField(
            value = newIngredientName,
            onValueChange = { newIngredientName = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(id = R.string.upload_enter_ingredient)) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = BrandGrey,
                focusedBorderColor = BrandOrange,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Duidelijke optie om het getypte ingrediënt toe te voegen als label + amount
        if (newIngredientName.isNotBlank()) {
            OutlinedButton(
                onClick = {
                    ingredients.add(Ingredient(name = newIngredientName.trim(), amount = null, unitType = "", isConfirmed = false))
                    newIngredientName = ""
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = DarkText
                ),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BrandGrey)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = DarkText)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(id = R.string.upload_add_ingredient))
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Add Ingredient Button (toon alleen als er geen leeg veld bestaat én er geen naam getypt is)
        val hasEmptyRow = ingredients.any { it.name.isBlank() && it.amount == null && it.unitType.isBlank() }
        if (!hasEmptyRow && newIngredientName.isBlank()) {
            OutlinedButton(
                onClick = {
                    // Voeg een leeg ingrediëntveld toe
                    ingredients.add(Ingredient(name = "", amount = null, unitType = "", isConfirmed = false))
                    newIngredientName = ""
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = DarkText
                ),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BrandGrey)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add",
                    tint = DarkText
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(id = R.string.upload_new_ingredient))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))


        // Kitchen Style Section
        FilterSection(
            title = stringResource(id = R.string.upload_kitchen_style),
            options = kitchenStylesStrings,
            selectedOptions = selectedKitchenStyles,
            expanded = kitchenExpanded,
            onHeaderToggle = { kitchenExpanded = !kitchenExpanded },
            onOptionToggle = { option ->
                // single-select gedrag
                selectedKitchenStyles = if (selectedKitchenStyles.contains(option)) emptySet() else setOf(option)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Meal Type Section
        FilterSection(
            title = stringResource(R.string.upload_meal_type),
            options = mealTypeStrings,
            selectedOptions = selectedMealTypes,
            expanded = mealTypeExpanded,
            onHeaderToggle = { mealTypeExpanded = !mealTypeExpanded },
            onOptionToggle = { option ->
                // single-select gedrag
                selectedMealTypes = if (selectedMealTypes.contains(option)) emptySet() else setOf(option)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Allergens Section
        FilterSection(
            title = stringResource(R.string.upload_allergens),
            options = allergensStrings,
            selectedOptions = selectedAllergens,
            expanded = allergensExpanded,
            onHeaderToggle = { allergensExpanded = !allergensExpanded },
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
            title = stringResource(R.string.upload_diets),
            options = dietsStrings,
            selectedOptions = selectedDiets,
            expanded = dietExpanded,
            onHeaderToggle = { dietExpanded = !dietExpanded },
            onOptionToggle = { option ->
                // single-select gedrag
                selectedDiets = if (selectedDiets.contains(option)) emptySet() else setOf(option)
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Upload Button (uses app-wide PrimaryButton style)
        PrimaryButton(
            text = stringResource(R.string.upload_button),
            onClick = {
                // ✅ Launch coroutine for API call
                coroutineScope.launch {
                    if (token.isNullOrBlank()) {
                        println("No token available - user not logged in")
                        return@launch  // Exit early if no token
                    }
                    try {
                        val request = RecipeUploadRequest(
                            title = recipeName,
                            description = description,
                            instructions = "Test",
                            prepTime = 0,
                            cookingTime = durationOptions[cookingDurationIndex],
                            difficulty = Difficulty.valueOf(selectedDifficulty.uppercase()),
                            images = if (coverPhotoUri != null) {
                                listOf(RecipeImageEntry(
                                    id = 0,
                                    recipeId = 0,
                                    imageUrl = coverPhotoUri.toString()
                                ))
                            } else emptyList(),
                            mealType = MealType.valueOf((selectedMealTypes.firstOrNull() ?: "BREAKFAST").uppercase()),
                            kitchenStyle = KitchenStyle.valueOf((selectedKitchenStyles.firstOrNull() ?: "ITALIAN").uppercase()),
                            diets = selectedDiets.map {
                                DietEntry(id = 0, displayName = it, description = "")
                            },
                            allergens = selectedAllergens.map {
                                AllergenEntry(name = it, id = 0, displayName = it, description = "")
                            },
                            ingredients = ingredients.mapNotNull { ingredient ->
                                if (ingredient.isConfirmed && ingredient.name.isNotBlank() && ingredient.amount != null) {
                                    IngredientUnitEntry(
                                        recipeId = 0,
                                        ingredientName = ingredient.name,
                                        amount = ingredient.amount!!,
                                        unitType = ingredient.unitType
                                    )
                                } else null
                            }
                        )

                        ApiService.uploadRecipe(token, coverPhotoUri?.toString(), request)
                        showSuccessDialog = true

                    } catch (e: Exception) {
                        println("Upload failed: ${e.message}")
                        // TODO: Show error dialog/snackbar
                    }
                }
            }
        )
    }
        Spacer(modifier = Modifier.height(32.dp))
}



@Composable
fun UploadSuccessDialog(onDismiss: () -> Unit, onBackToHome: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🥳",
                    fontSize = 80.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    stringResource(R.string.upload_succes_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    stringResource(R.string.upload_succes_text),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = DarkText.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onBackToHome,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(stringResource(R.string.upload_button_home),
                        color = Color.White)
                }
            }
        }
    }
}

@Composable
fun AddCoverPhotoSection(
    onClick: () -> Unit  // Changed parameter
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .border(2.dp, BrandGrey, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
            .clickable { onClick() }  // Call parent launcher
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "Add cover photo",
                modifier = Modifier.size(64.dp),
                tint = Color(0xFF9E9E9E)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.upload_cover_photo),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF5B6B7C),
            )
            Text(
                text = stringResource(R.string.upload_cover_photo_text),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun PhotoPicker(
    coverPhotoUri: Uri?,
    onPhotoClick: () -> Unit,
    onPhotoRemove: () -> Unit
) {
    if (coverPhotoUri != null) {
        // SHOW PREVIEW
        Box(modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp))) {
            AsyncImage(
                model = coverPhotoUri,
                contentDescription = "Cover photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = onPhotoRemove,
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
            ) {
                Icon(Icons.Default.Close, "Remove photo", tint = Color.White)
            }
        }
    } else {
        // SHOW ADD BUTTON
        AddCoverPhotoSection(onClick = onPhotoClick)
    }
}

@Composable
fun DifficultySelector(
    selectedDifficulty: String,
    onDifficultySelected: (String) -> Unit
) {
    val difficulties = listOf(
        stringResource(R.string.upload_difficulty_easy),
        stringResource(R.string.upload_difficulty_medium),
        stringResource(R.string.upload_difficulty_hard))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        difficulties.forEach { difficulty ->
            val isSelected = difficulty == selectedDifficulty
            val selectedColor = when (difficulty) {
                "Easy" -> BrandGreen
                "Medium" -> BrandYellow
                "Hard" -> Color.Red
                else -> BrandOrange
            }
            FilterChip(
                selected = isSelected,
                onClick = {
                    if (isSelected) onDifficultySelected("") else onDifficultySelected(difficulty)
                },
                label = { Text(difficulty) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = selectedColor,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = DarkText
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = BrandGrey,
                    selectedBorderColor = selectedColor,
                    borderWidth = 1.dp,
                    selectedBorderWidth = 1.dp
                )
            )
        }
    }
}

@Composable
fun CookingDurationSlider(
    index: Int,
    onIndexChange: (Int) -> Unit,
    options: List<Int>
) {
    val min = 0f
    val max = (options.size - 1).toFloat()

    // Only show labels at <10, 30, and 60
    val labelRow = List(options.size) { i ->
        when (i) {
            0 -> "<10 min"
            options.indexOf(30) -> "30 min"
            options.lastIndex -> "60 min"
            else -> ""
        }
    }

    Column {
        // Labels row aligned with slider positions
        Row(modifier = Modifier.fillMaxWidth()) {
            labelRow.forEach { label ->
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    if (label.isNotEmpty()) {
                        Text(label, color = BrandOrange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Slider zonder extra tick marks; oranje track en groene thumb
        Slider(
            value = index.toFloat(),
            onValueChange = { raw ->
                val snapped = raw.coerceIn(min, max).toInt()
                onIndexChange(snapped)
            },
            valueRange = min..max,
            steps = (options.size - 2).coerceAtLeast(0),
            colors = SliderDefaults.colors(
                thumbColor = BrandGreen,
                activeTrackColor = BrandOrange,
                inactiveTrackColor = BrandOrange.copy(alpha = 0.25f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        )
    }
}

@Composable
fun SimpleIngredientItem(
    ingredient: Ingredient,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onUnitTypeChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onUnlock: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Name: toon label (chip) als er een naam is, anders inputveld
        if (ingredient.name.isNotBlank()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(BrandOrange)
                    .clickable { onUnlock() }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = ingredient.name, color = Color.White)
            }
        } else {
            IngredientAutoCompleteField(
                value = ingredient.name,
                onValueChange = onNameChange,
                onSelected = { selected -> onNameChange(selected) },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                placeholder = stringResource(id = R.string.upload_enter_ingredient)
            )
        }

        // Amount Input (Double) + Unit Type (String)
        val amountText = ingredient.amount?.toString() ?: ""
        Box(modifier = Modifier.weight(0.5f)) {
            OutlinedTextField(
                value = amountText,
                onValueChange = onAmountChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = { Text(stringResource(id = R.string.qty), color = DarkText.copy(alpha = 0.6f)) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = BrandGrey,
                    focusedBorderColor = BrandOrange,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedTextColor = DarkText,
                    focusedTextColor = DarkText,
                    cursorColor = BrandOrange,
                    disabledBorderColor = BrandOrange,
                    disabledContainerColor = BrandOrange,
                    disabledTextColor = Color.White,
                    disabledPlaceholderColor = Color.White.copy(alpha = 0.7f)
                ),
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                enabled = !ingredient.isConfirmed,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Number),
            )

            // Wanneer bevestigd (disabled), laat het hele veld klikbaar om te ontgrendelen
            if (ingredient.isConfirmed) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { onUnlock() }
                )
            }
        }

        // Unit type input
        Box(modifier = Modifier.weight(0.5f)) {
            OutlinedTextField(
                value = ingredient.unitType,
                onValueChange = onUnitTypeChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = { Text(stringResource(id = R.string.unit), color = DarkText.copy(alpha = 0.6f)) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = BrandGrey,
                    focusedBorderColor = BrandOrange,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedTextColor = DarkText,
                    focusedTextColor = DarkText,
                    cursorColor = BrandOrange,
                    disabledBorderColor = BrandOrange,
                    disabledContainerColor = BrandOrange,
                    disabledTextColor = Color.White,
                    disabledPlaceholderColor = Color.White.copy(alpha = 0.7f)
                ),
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                enabled = !ingredient.isConfirmed,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onConfirm() })
            )

            if (ingredient.isConfirmed) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { onUnlock() }
                )
            }
        }

        // Remove Button
        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Remove",
                tint = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSection(
    title: String,
    options: List<String>,
    selectedOptions: Set<String>,
    expanded: Boolean = true,
    onHeaderToggle: () -> Unit = {},
    onOptionToggle: (String) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onHeaderToggle() }
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (expanded) "▼" else "▶",
                color = BrandYellow,
                fontSize = 12.sp
            )
        }

        if (expanded) {
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
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

//@Preview(
//    name = "Upload – Full Screen Scroll Preview",
//    showBackground = true,
//    device = Devices.PIXEL_5,
//    showSystemUi = false,
//    widthDp = 411,   // Pixel 5 width in dp
//    heightDp = 2000  // Large height so the Preview panel becomes scrollable
//)
//@Composable
//fun UploadScreenPreview() {
//    MixAndMealAppTheme {
//        UploadScreen(
//            navController = rememberNavController(),
//            onCameraClick = {},
//            onPhotoPick = TODO()
//        )
//    }
//}
//@Preview(name = "Upload Success Dialog", showBackground = true)
//@Composable
//fun UploadSuccessDialogPreview() {
//    MixAndMealAppTheme {
//        UploadSuccessDialog(onDismiss = {}, onBackToHome = {})
//    }
//}
