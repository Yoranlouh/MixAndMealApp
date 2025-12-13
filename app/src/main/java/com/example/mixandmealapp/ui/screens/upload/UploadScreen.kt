package com.example.mixandmealapp.ui.screens.upload

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme

data class Ingredient(
    val name: String,
    var amount: Double? = null,
    var unitType: String = "",
    var isConfirmed: Boolean = false
)

@Composable
fun UploadScreen(navController: NavHostController) {
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
        AddCoverPhotoSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Recipe Name
        var recipeName by remember { mutableStateOf("") }

        InputFieldSmall(
            value = recipeName,
            onValueChange = { recipeName = it },
            modifier = Modifier.fillMaxWidth(),
            label = "Recipe Name",
            placeholder = "Enter Recipe Name",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Description
        InputFieldTextBox(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier.fillMaxWidth(),
            label = "Description",
            placeholder = "Tell us a little about your recipe",
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Normal
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Cooking Duration",
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
            text = "Ingredients",
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
            placeholder = { Text(stringResource(id = com.example.mixandmealapp.R.string.upload_enter_ingredient)) },
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
                Text(stringResource(id = com.example.mixandmealapp.R.string.upload_add_ingredient))
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
                Text(stringResource(id = com.example.mixandmealapp.R.string.upload_new_ingredient))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Kitchen Style Section
        FilterSection(
            title = stringResource(id = com.example.mixandmealapp.R.string.upload_kitchen_style),
            options = FilterOptions.kitchenStyles,
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
            title = "Meal Type",
            options = FilterOptions.mealTypes,
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
            title = "Allergens",
            options = FilterOptions.allergens,
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
            title = "Diet",
            options = FilterOptions.diets,
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
            text = "Upload",
            onClick = { showSuccessDialog = true }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
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
                    "Upload Success",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Your recipe has been uploaded,\nyou can see it on your profile",
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
                    Text("Back to Home", color = Color.White)
                }
            }
        }
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
                placeholder = stringResource(id = com.example.mixandmealapp.R.string.upload_enter_ingredient)
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
                placeholder = { Text(stringResource(id = com.example.mixandmealapp.R.string.qty), color = DarkText.copy(alpha = 0.6f)) },
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
                placeholder = { Text(stringResource(id = com.example.mixandmealapp.R.string.unit), color = DarkText.copy(alpha = 0.6f)) },
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

@Preview(
    name = "Upload – Full Screen Scroll Preview",
    showBackground = true,
    device = Devices.PIXEL_5,
    showSystemUi = false,
    widthDp = 411,   // Pixel 5 width in dp
    heightDp = 2000  // Large height so the Preview panel becomes scrollable
)
@Composable
fun UploadScreenPreview() {
    MixAndMealAppTheme {
        UploadScreen(navController = rememberNavController())
    }
}
@Preview(name = "Upload Success Dialog", showBackground = true)
@Composable
fun UploadSuccessDialogPreview() {
    MixAndMealAppTheme {
        UploadSuccessDialog(onDismiss = {}, onBackToHome = {})
    }
}
