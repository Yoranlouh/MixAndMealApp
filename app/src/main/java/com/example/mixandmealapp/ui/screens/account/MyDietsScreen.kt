package com.example.mixandmealapp.ui.screens.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mixandmealapp.ui.viewmodel.AccountViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDietsScreen(
    onBack: () -> Unit,
    accountViewModel: AccountViewModel = koinViewModel()
) {
    val state by accountViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        accountViewModel.load()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mijn Diëten") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Terug")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (state.error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Fout bij laden: ${state.error}", color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { accountViewModel.load() }) {
                            Text("Opnieuw proberen")
                        }
                    }
                }
            } else if (state.allAvailableDiets.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Selecteer je dieetvoorkeuren. We zullen recepten tonen die passen bij jouw keuzes.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(state.allAvailableDiets) { diet ->
                            val isSelected = state.userDiets.any { it.id == diet.id }
                            Surface(
                                onClick = {
                                    if (isSelected) accountViewModel.removeDiet(diet)
                                    else accountViewModel.addDiet(diet)
                                },
                                shape = MaterialTheme.shapes.medium,
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.outlineVariant
                                ),
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
                                        text = diet.displayName,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = null // Handled by Surface onClick
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
