package com.example.mixandmealapp.ui.screens.search

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.mixandmealapp.R
import com.example.mixandmealapp.models.entries.AllergenEntry
import com.example.mixandmealapp.models.entries.DietEntry
import com.example.mixandmealapp.models.enums.Difficulty
import com.example.mixandmealapp.models.enums.KitchenStyle
import com.example.mixandmealapp.models.enums.MealType
import com.example.mixandmealapp.models.requests.RecipeSearchRequest
import com.example.mixandmealapp.ui.components.PopularRecipeCard
import com.example.mixandmealapp.ui.screens.upload.FilterSection
import com.example.mixandmealapp.ui.theme.BrandOrange
import com.example.mixandmealapp.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun SearchScreen(
    onSearch: (RecipeSearchRequest) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    navController: NavController,
    onSpeechRecognize: (callback: (String?) -> Unit) -> Unit
) {
    val context = LocalContext.current
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    val noInstructionsMessage = stringResource(id = R.string.no_instructions_message)

    DisposableEffect(Unit) {
        val ttsInstance = TextToSpeech(context) { status ->
            // TTS initialization callback
        }
        tts = ttsInstance
        onDispose {
            ttsInstance.stop()
            ttsInstance.shutdown()
        }
    }

    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var maxCookingTime: Int? by remember { mutableStateOf(null) }

    // Filter states
    var selectedDifficulty by remember { mutableStateOf<String?>(null) }
    var selectedKitchenStyle by remember { mutableStateOf<String?>(null) }
    var selectedMealType by remember { mutableStateOf<String?>(null) }

    // Allergen states - multi-select
    var selectedAllergens by remember { mutableStateOf<List<Int>>(emptyList()) }

    // Diet states - multi-select
    var selectedDiets by remember { mutableStateOf<List<Int>>(emptyList()) }

    // Allergen list
    val allergenList = remember {
        listOf(
            AllergenEntry(1, "gluten", "Gluten", ""),
            AllergenEntry(2, "crustaceans", "Crustaceans", ""),
            AllergenEntry(3, "eggs", "Eggs", ""),
            AllergenEntry(4, "fish", "Fish", ""),
            AllergenEntry(5, "peanuts", "Peanuts", ""),
            AllergenEntry(6, "soy", "Soy", ""),
            AllergenEntry(7, "milk", "Milk", ""),
            AllergenEntry(8, "treenuts", "Tree Nuts", ""),
            AllergenEntry(9, "celery", "Celery", ""),
            AllergenEntry(10, "mustard", "Mustard", ""),
            AllergenEntry(11, "sesame", "Sesame", ""),
            AllergenEntry(12, "sulphites", "Sulphites", ""),
            AllergenEntry(13, "lupin", "Lupin", ""),
            AllergenEntry(14, "molluscs", "Molluscs", ""),
            AllergenEntry(15, "corn", "Corn", "")
        )
    }

    // Diet list
    val dietList = remember {
        listOf(
            DietEntry(1, "Veganistisch", ""),
            DietEntry(2, "Vegetarisch", ""),
            DietEntry(3, "Glutenvrij", ""),
            DietEntry(4, "Lactosevrij", ""),
            DietEntry(5, "Notenvrij", ""),
            DietEntry(6, "Zuivelvrij", ""),
            DietEntry(7, "Suikerarm", ""),
            DietEntry(8, "Zoutarm", ""),
            DietEntry(9, "Halal", ""),
            DietEntry(10, "Kosher", ""),
            DietEntry(11, "Paleo", ""),
            DietEntry(12, "Keto", ""),
            DietEntry(13, "Raw food", ""),
            DietEntry(14, "Flexitarisch", "")
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search + Voice row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search recipes") },
                modifier = Modifier.weight(1f),
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onSpeechRecognize { result ->
                                if (result != null) {
                                    searchQuery = result
                                } else {
                                    Toast.makeText(context, "No speech detected", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Search"
                        )
                    }
                }
            )

            Button(
                onClick = {
                    scope.launch {
                        val difficultyToSend = selectedDifficulty?.uppercase()
                        onSearch(
                            RecipeSearchRequest(
                                partialTitle = searchQuery,
                                difficulty = difficultyToSend,
                                mealType = selectedMealType,
                                kitchenStyle = selectedKitchenStyle,
                                maxCookingTime = maxCookingTime,
                                diets = selectedDiets,
                                allergens = selectedAllergens,
                                ingredients = emptyList()
                            )
                        )
                    }
                },
                modifier = Modifier.height(56.dp)
            ) {
                Text("Search")
            }
        }

        // Max cooking time input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = maxCookingTime?.toString() ?: "",
                onValueChange = { value ->
                    maxCookingTime = value.toIntOrNull()
                },
                label = { Text("Max cooking time (min)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        // Difficulty section
        FilterSection(
            title = "Difficulty",
            chips = Difficulty.entries.map { it.difficultyName },
            selected = selectedDifficulty,
            onSelect = { selectedDifficulty = it }
        )

        // Kitchen style section
        FilterSection(
            title = "Kitchen Style",
            chips = KitchenStyle.entries.map { it.kitchenName },
            selected = selectedKitchenStyle,
            onSelect = { selectedKitchenStyle = it }
        )

        // Meal type section
        FilterSection(
            title = "Meal Type",
            chips = MealType.entries.map { it.mealTypeName },
            selected = selectedMealType,
            onSelect = { selectedMealType = it }
        )

        // Allergen section
        MultiSelectFilterSection(
            title = "Exclude Allergens (${selectedAllergens.size})",
            entries = allergenList.map { it.id to it.displayName },
            selected = selectedAllergens,
            onToggle = { id ->
                selectedAllergens = if (selectedAllergens.contains(id)) {
                    selectedAllergens - id
                } else {
                    selectedAllergens + id
                }
            }
        )

        // Diet section
        MultiSelectFilterSection(
            title = "Diets (${selectedDiets.size})",
            entries = dietList.map { it.id to it.displayName },
            selected = selectedDiets,
            onToggle = { id ->
                selectedDiets = if (selectedDiets.contains(id)) {
                    selectedDiets - id
                } else {
                    selectedDiets + id
                }
            }
        )
    }
}

@Composable
private fun FilterSection(
    title: String,
    chips: List<String>,
    selected: String?,
    onSelect: (String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(chips) { name ->
                FilterChip(
                    selected = selected == name,
                    onClick = { onSelect(name) },
                    label = { Text(name.replaceFirstChar { it.uppercase() }) }
                )
            }
        }
    }
}

@Composable
private fun MultiSelectFilterSection(
    title: String,
    entries: List<Pair<Int, String>>,
    selected: List<Int>,
    onToggle: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(entries) { (id, name) ->
                FilterChip(
                    selected = id in selected,
                    onClick = { onToggle(id) },
                    label = {
                        Text(
                            text = name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
    }
}