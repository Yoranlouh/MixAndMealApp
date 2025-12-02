package com.example.mixandmealapp.ui.screens.recipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mixandmealapp.ui.components.BackButton
import com.example.mixandmealapp.ui.components.FavoriteIcon
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme

data class EditorsChoiceItem(val title: String, val author: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorsChoiceScreen(
    navController: NavHostController,
    items: List<EditorsChoiceItem> = listOf(
        EditorsChoiceItem("Easy homemade beef burger", "James Spader"),
        EditorsChoiceItem("Blueberry with egg for breakfast", "Alice Fala"),
        EditorsChoiceItem("Creamy tomato pasta", "John Doe"),
        EditorsChoiceItem("Healthy quinoa salad", "Jane Roe")
    )
) {
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
                            text = "Editor's Choice",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val rows = items.chunked(2)
            items(count = rows.size) { rowIndex ->
                val row = rows[rowIndex]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    EditorsChoiceCardItem(item = row[0], modifier = Modifier.weight(1f))
                    if (row.size > 1) {
                        EditorsChoiceCardItem(item = row[1], modifier = Modifier.weight(1f))
                    } else {
                        Box(modifier = Modifier.weight(1f)) {}
                    }
                }
            }
        }
    }
}

@Composable
private fun EditorsChoiceCardItem(item: EditorsChoiceItem, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(180.dp),
        shape = MaterialTheme.shapes.large,
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        androidx.compose.foundation.layout.Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF5F5F5)
                ) {}

                val fav = remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    FavoriteIcon(isFavorite = fav.value) { fav.value = !fav.value }
                }
            }

            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0A2533),
                    maxLines = 2
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditorsChoiceScreenPreview() {
    MixAndMealAppTheme {
        EditorsChoiceScreen(navController = rememberNavController())
    }
}

