package com.example.mixandmealapp.ui.screens.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mixandmealapp.ui.screens.upload.FilterSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterBottomSheet(
    show: Boolean,
    selectedKitchenStyles: String,
    selectedMealTypes: String,
    selectedAllergens: List<String>,
    selectedDiets: List<String>,
    onToggleKitchen: (String) -> Unit,
    onToggleMealType: (String) -> Unit,
    onToggleAllergen: (String) -> Unit,
    onToggleDiet: (String) -> Unit,
    onApply: () -> Unit,
    onClearAll: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (!show) return

    var kitchenExpanded by remember { mutableStateOf(false) }
    var mealTypeExpanded by remember { mutableStateOf(false) }
    var allergensExpanded by remember { mutableStateOf(false) }
    var dietsExpanded by remember { mutableStateOf(false) }

    // Translated filter options
    val kitchenStylesStrings = FilterOptions.kitchenStyles.map { stringResource(it) }
    val mealTypeStrings = FilterOptions.mealTypes.map { stringResource(it) }
    val allergensStrings = FilterOptions.allergens.map { stringResource(it) }
    val dietsStrings = FilterOptions.diets.map { stringResource(it) }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Text(
            text = "Filters",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            // Kitchen Styles (multi-select)
            FilterSection(
                title = "Kitchen Styles",
                options = kitchenStylesStrings,
                selectedOptions = setOf(selectedKitchenStyles),
                expanded = kitchenExpanded,
                onHeaderToggle = { kitchenExpanded = !kitchenExpanded },
                onOptionToggle = { opt ->
                    onToggleKitchen(opt)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Meal Types (multi-select)
            FilterSection(
                title = "Meal Types",
                options = mealTypeStrings,
                selectedOptions = setOf(selectedMealTypes),
                expanded = mealTypeExpanded,
                onHeaderToggle = { mealTypeExpanded = !mealTypeExpanded },
                onOptionToggle = { opt ->
                    onToggleMealType(opt)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Allergens (multi-select)
            FilterSection(
                title = "Allergens",
                options = allergensStrings,
                selectedOptions = selectedAllergens.toSet(),
                expanded = allergensExpanded,
                onHeaderToggle = { allergensExpanded = !allergensExpanded },
                onOptionToggle = { opt ->
                    onToggleAllergen(opt)
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Diets (multi-select)
            FilterSection(
                title = "Diets",
                options = dietsStrings,
                selectedOptions = selectedDiets.toSet(),
                expanded = dietsExpanded,
                onHeaderToggle = { dietsExpanded = !dietsExpanded },
                onOptionToggle = { opt ->
                    onToggleDiet(opt)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()

        Button(
            onClick = onApply,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth()
        ) { Text("Apply filters") }

        Button(
            onClick = onClearAll,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 0.dp)
                .fillMaxWidth()
        ) { Text("Clear all") }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CompactFilterSummary(
    selectedKitchenStyles: String,
    selectedMealTypes: String,
    selectedAllergens: List<String>,
    selectedDiets: List<String>,
    onOpenFilters: () -> Unit,
    onClearAll: () -> Unit
) {
    val total = selectedAllergens.size + selectedDiets.size
    val expanded = remember { mutableStateOf(false) }

    androidx.compose.material3.OutlinedButton(
        onClick = { expanded.value = true },
        modifier = Modifier
            .padding(top = 8.dp)
    ) {
        Text(text = if (total > 0) "Filters ($total)" else "Filters")
    }

    DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
        if (total == 0) {
            DropdownMenuItem(text = { Text("No filters active") }, onClick = { expanded.value = false })
        } else {
            if (selectedKitchenStyles.isNotEmpty()) {
                DropdownMenuItem(
                    text = { Text("Kitchen: " + selectedKitchenStyles) },
                    onClick = { }
                )
            }
            if (selectedMealTypes.isNotEmpty()) {
                DropdownMenuItem(
                    text = { Text("Meal: " + selectedMealTypes) },
                    onClick = { }
                )
            }
            if (selectedAllergens.isNotEmpty()) {
                DropdownMenuItem(
                    text = { Text("Allergens: " + selectedAllergens.joinToString(limit = 3)) },
                    onClick = { }
                )
            }
            if (selectedDiets.isNotEmpty()) {
                DropdownMenuItem(
                    text = { Text("Diets: " + selectedDiets.joinToString(limit = 3)) },
                    onClick = { }
                )
            }
            Divider()
            DropdownMenuItem(text = { Text("Edit filters…") }, onClick = {
                expanded.value = false
                onOpenFilters()
            })
            DropdownMenuItem(text = { Text("Clear all") }, onClick = {
                expanded.value = false
                onClearAll()
            })
        }
    }
}
