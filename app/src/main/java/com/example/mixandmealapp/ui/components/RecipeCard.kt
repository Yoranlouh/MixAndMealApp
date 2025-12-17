package com.example.mixandmealapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mixandmealapp.models.responses.RecipeCardResponse


@Composable
fun PopularRecipeCard(recipe: RecipeCardResponse, onClick: () -> Unit = {}) {

    Card(
        modifier = Modifier
            .size(160.dp, 240.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween // Use arrangement to push items apart
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = recipe.title, style = MaterialTheme.typography.titleMedium, maxLines = 2)
            }
            Text("${recipe.cookingTime} min", style = MaterialTheme.typography.bodySmall)
        }
    }
}
