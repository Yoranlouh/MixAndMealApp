package com.example.mixandmealapp.ui.screens.allergen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.example.mixandmealapp.ui.components.BackButton
import com.example.mixandmealapp.ui.components.IngredientAutoCompleteField
import com.example.mixandmealapp.ui.components.Labels
import com.example.mixandmealapp.ui.viewmodel.AllergensViewModel
import com.example.mixandmealapp.ui.viewmodel.HomeViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllergensScreen(
    navController: NavHostController,
    viewModel: AllergensViewModel? = null,
    homeViewModel: HomeViewModel = koinInject(),
) {

    val vm : AllergensViewModel = koinViewModel()

    val uiState by vm.uiState.collectAsState()
    var newAllergen by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BackButton(
                            navController = navController,
                            modifier = Modifier.padding(end = 8.dp),
                            onClick = {
                                navController.navigate(com.example.mixandmealapp.ui.navigation.Navigation.HOME) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                        Text(
                            text = stringResource(id = com.example.mixandmealapp.R.string.allergens_title),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(text = stringResource(id = com.example.mixandmealapp.R.string.my_allergens), style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Text(
                        text = stringResource(id = com.example.mixandmealapp.R.string.items_count, uiState.items.size),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Allergenenlijst (use items from ViewModel)
            uiState.items.forEach { item ->
                Labels(
                    label = item.allergenName,
                    onRemove = { vm.removeItem(item) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


            val onAddItem = {
                if (newAllergen.isNotBlank()) {
                    vm.addItem(newAllergen)
                    newAllergen = ""
                }
            }

            IngredientAutoCompleteField(
                value = newAllergen,
                onValueChange = { newAllergen = it },
                onSelected = { selected -> newAllergen = selected },
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(id = com.example.mixandmealapp.R.string.allergens_enter_allergen),
            )

            Button(
                onClick = onAddItem,
                enabled = newAllergen.isNotBlank(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.height(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = com.example.mixandmealapp.R.string.allergens_add_allergen)
                )
            }
        }
    }
}
