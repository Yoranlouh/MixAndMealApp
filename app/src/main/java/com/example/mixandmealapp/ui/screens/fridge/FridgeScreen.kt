package com.example.mixandmealapp.ui.screens.fridge

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mixandmealapp.ui.components.LabelFridge
import com.example.mixandmealapp.ui.components.BackButton
import com.example.mixandmealapp.ui.theme.BrandGrey
import com.example.mixandmealapp.ui.theme.BrandOrange
import com.example.mixandmealapp.ui.theme.DarkText
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FridgeScreen(navController: NavHostController) {
    // Local state for demo purposes. In a real app, this would come from a ViewModel.
    val ingredients = remember { mutableStateListOf("Potatoes", "Onions", "Paprika") }
    var newIngredient by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BackButton(
                            navController = navController,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "Fridge",
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
                    Text(text = "My Fridge", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Text(
                        text = "${ingredients.size} Item",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Ingrediëntenlijst
            ingredients.forEach { name ->
                LabelFridge(
                    label = name,
                    onRemove = { ingredients.remove(name) }
                )
            }

            // Inputveld + knop direct onder de laatste ingrediëntkaart
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = newIngredient,
                onValueChange = { newIngredient = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter ingrediënt") },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = BrandGrey,
                    focusedBorderColor = BrandOrange,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (newIngredient.isNotBlank()) {
                        ingredients.add(newIngredient.trim())
                        newIngredient = ""
                    }
                })
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    if (newIngredient.isNotBlank()) {
                        ingredients.add(newIngredient.trim())
                        newIngredient = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = newIngredient.isNotBlank(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = DarkText
                ),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BrandGrey)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = DarkText)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add ingrediënt")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FridgeScreenPreview() {
    MixAndMealAppTheme {
        FridgeScreen(navController = rememberNavController())
    }
}
