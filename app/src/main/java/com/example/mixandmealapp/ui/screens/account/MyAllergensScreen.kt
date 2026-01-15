package com.example.mixandmealapp.ui.screens.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mixandmealapp.ui.theme.BrandOrange
import com.example.mixandmealapp.ui.viewmodel.AccountViewModel
import org.koin.androidx.compose.koinViewModel

import androidx.compose.material.icons.filled.Add
import com.example.mixandmealapp.ui.components.LabelFridge
import com.example.mixandmealapp.ui.theme.DarkText
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAllergensScreen(
    onBack: () -> Unit,
    accountViewModel: AccountViewModel = koinViewModel()
) {
    val state by accountViewModel.uiState.collectAsState()
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        accountViewModel.load()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mijn Allergenen", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Terug")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when {
                state.error != null -> {
                    AllergenErrorState(
                        message = state.error ?: "Onbekende fout",
                        onRetry = { accountViewModel.load() }
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Beheer je allergenen. We zullen recepten filteren die deze bevatten.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        // Actieve allergenen lijst
                        state.userAllergens.sortedBy { it.displayName }.forEach { allergen ->
                            LabelFridge(
                                label = allergen.displayName,
                                onRemove = { accountViewModel.removeAllergen(allergen) }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Autocomplete veld voor nieuwe allergenen
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = query,
                                onValueChange = { 
                                    query = it
                                    expanded = it.isNotBlank()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Voeg een allergeen toe...", color = Color.Gray) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color.LightGray,
                                    focusedBorderColor = BrandOrange,
                                    unfocusedContainerColor = Color.White,
                                    focusedContainerColor = Color.White
                                ),
                                shape = RoundedCornerShape(24.dp),
                                singleLine = true
                            )

                            if (expanded) {
                                val suggestions = state.allAvailableAllergens
                                    .filter { it.displayName.contains(query, ignoreCase = true) }
                                    .filter { allergen -> state.userAllergens.none { it.id == allergen.id } }
                                    .sortedBy { it.displayName }

                                if (suggestions.isNotEmpty()) {
                                    Surface(
                                        tonalElevation = 2.dp,
                                        shadowElevation = 4.dp,
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 56.dp)
                                    ) {
                                        Column(modifier = Modifier.background(Color.White)) {
                                            suggestions.forEach { suggestion ->
                                                Text(
                                                    text = suggestion.displayName,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            accountViewModel.addAllergen(suggestion)
                                                            query = ""
                                                            expanded = false
                                                        }
                                                        .padding(12.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (query.isNotBlank() && expanded) {
                             // Optioneel: toon melding als er geen suggesties zijn
                             val suggestions = state.allAvailableAllergens
                                .filter { it.displayName.contains(query, ignoreCase = true) }
                                .filter { allergen -> state.userAllergens.none { it.id == allergen.id } }
                             
                             if (suggestions.isEmpty()) {
                                 Text(
                                     text = "Geen beschikbare allergenen gevonden voor \"$query\"",
                                     style = MaterialTheme.typography.bodySmall,
                                     color = Color.Gray,
                                     modifier = Modifier.padding(horizontal = 12.dp)
                                 )
                             }
                        }
                    }
                }
            }
            
            if (state.isSaving) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BrandOrange)
                }
            }
        }
    }
}

@Composable
fun AllergenPreferenceItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        tonalElevation = if (isSelected) 2.dp else 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
            )
            Checkbox(
                checked = isSelected,
                onCheckedChange = null,
                colors = CheckboxDefaults.colors(
                    checkedColor = BrandOrange,
                    checkmarkColor = Color.White
                )
            )
        }
    }
}

@Composable
fun AllergenLoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = BrandOrange)
    }
}

@Composable
fun AllergenErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Oeps! Er ging iets mis.",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
        ) {
            Text("Opnieuw proberen")
        }
    }
}
