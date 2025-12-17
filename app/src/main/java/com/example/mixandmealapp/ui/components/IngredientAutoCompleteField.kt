package com.example.mixandmealapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mixandmealapp.R
import com.example.mixandmealapp.data.IngredientCatalogRepository
import com.example.mixandmealapp.data.ServiceLocator
import com.example.mixandmealapp.ui.theme.BrandGrey
import com.example.mixandmealapp.ui.theme.BrandOrange
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun IngredientAutoCompleteField(
    value: String,
    onValueChange: (String) -> Unit,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    repository: IngredientCatalogRepository = ServiceLocator.ingredientCatalogRepository,
    placeholder: String = stringResource(id = R.string.upload_enter_ingredient)
) {
    var expanded by remember { mutableStateOf(false) }
    val suggestions = remember { mutableStateListOf<String>() }
    val scope = rememberCoroutineScope()
    var searchJob by remember { mutableStateOf<Job?>(null) }
    val focusManager = LocalFocusManager.current

    // Dialog state for adding new ingredient
    var showAddDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }

    fun performSearch(query: String) {
        searchJob?.cancel()
        searchJob = scope.launch {
            delay(120) // debounce
            if (query.isBlank()) {
                suggestions.clear()
                expanded = false
            } else {
                val defs = repository.search(query)
                suggestions.clear()
                suggestions.addAll(defs.map { it.name })
                expanded = suggestions.isNotEmpty() || query.isNotBlank()
            }
        }
    }

    LaunchedEffect(value) {
        performSearch(value)
    }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(placeholder, color = Color.Gray) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = BrandGrey,
                focusedBorderColor = BrandOrange,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp),
            singleLine = true
        )

        // Dropdown suggestions below the field
        if (expanded) {
            Surface(
                tonalElevation = 2.dp,
                shadowElevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp)
            ) {
                Column(modifier = Modifier.background(Color.White)) {
                    if (suggestions.isEmpty()) {
                        val addLabel = stringResource(R.string.add_new_ingredient_option, value)
                        Text(
                            text = addLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            color = BrandOrange,
                            modifier = Modifier
                                .clickable {
                                    // Open add dialog
                                    newTitle = value
                                    newDescription = ""
                                    showAddDialog = true
                                }
                                .padding(12.dp)
                        )
                    } else {
                        suggestions.forEach { s ->
                            Text(
                                text = s,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .clickable {
                                        onSelected(s)
                                        expanded = false
                                        focusManager.clearFocus()
                                    }
                                    .padding(12.dp)
                            )
                        }

                        // If the typed text isn't exactly among suggestions, offer Add new at bottom
                        if (value.isNotBlank() && suggestions.none { it.equals(value, true) }) {
                            val addLabel = stringResource(R.string.add_new_ingredient_option, value)
                            Text(
                                text = addLabel,
                                style = MaterialTheme.typography.bodyMedium,
                                color = BrandOrange,
                                modifier = Modifier
                                    .clickable {
                                        newTitle = value
                                        newDescription = ""
                                        showAddDialog = true
                                    }
                                    .padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(text = stringResource(id = R.string.upload_new_ingredient)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text(stringResource(id = R.string.ingredient_title)) }
                    )
                    OutlinedTextField(
                        value = newDescription,
                        onValueChange = { newDescription = it },
                        label = { Text(stringResource(id = R.string.ingredient_description)) }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        val def = repository.add(newTitle.trim(), newDescription.trim())
                        onSelected(def.name)
                        onValueChange(def.name)
                        showAddDialog = false
                        expanded = false
                        focusManager.clearFocus()
                    }
                }) {
                    Text(text = stringResource(id = R.string.upload_add_ingredient))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        )
    }
}
