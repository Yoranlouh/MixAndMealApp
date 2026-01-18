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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.mixandmealapp.ui.components.BackButton
import com.example.mixandmealapp.ui.components.IngredientAutoCompleteField
import com.example.mixandmealapp.ui.components.Labels
import com.example.mixandmealapp.ui.viewmodel.HomeViewModel
import com.example.mixandmealapp.ui.viewmodel.MyDietViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietScreen(
    navController: NavHostController,
    viewModel: MyDietViewModel? = null,
    homeViewModel: HomeViewModel = koinInject()
) {

    val vm : MyDietViewModel = koinViewModel()

    val uiState by vm.uiState.collectAsState()
    var newDiet by remember { mutableStateOf("") }

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
                            text = stringResource(id = com.example.mixandmealapp.R.string.diet_title),
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
                    Text(text = stringResource(id = com.example.mixandmealapp.R.string.my_diets), style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Text(
                        text = stringResource(id = com.example.mixandmealapp.R.string.items_count, uiState.items.size),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Diet lijst (use items from ViewModel)
            uiState.items.forEach { item ->
                Labels(
                    label = item.dietName,
                    onRemove = { vm.removeItem(item) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


            val onAddItem = {
                if (newDiet.isNotBlank()) {
                    vm.addItem(newDiet.trim())
                    newDiet = ""
                }
            }

            IngredientAutoCompleteField(
                value = newDiet,
                onValueChange = { newDiet = it },
                onSelected = { selected -> newDiet = selected },
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(id = com.example.mixandmealapp.R.string.diet_enter_diet),
            )

            Button(
                onClick = onAddItem,
                enabled = newDiet.isNotBlank(),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.height(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = com.example.mixandmealapp.R.string.diet_add_diet)
                )
            }
        }
    }
}

