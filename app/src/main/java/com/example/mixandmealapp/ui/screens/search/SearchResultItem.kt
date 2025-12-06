package com.example.mixandmealapp.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mixandmealapp.ui.theme.BrandGrey
import com.example.mixandmealapp.ui.theme.BrandOrange
import com.example.mixandmealapp.ui.theme.DarkText

@Composable
fun SearchResultItem(recipe: Recipe, onClick: (Recipe) -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(recipe) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            // Small thumbnail
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(BrandGrey, RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.size(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = recipe.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                if (recipe.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = recipe.description.take(80) + if (recipe.description.length > 80) "…" else "",
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkText
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Meta: duration and difficulty
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "${recipe.durationMinutes} min", style = MaterialTheme.typography.labelMedium, color = DarkText)
                    Text(text = "•", style = MaterialTheme.typography.labelMedium, color = BrandGrey)
                    Text(text = recipe.difficulty, style = MaterialTheme.typography.labelMedium, color = DarkText)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    recipe.kitchenStyle?.let { Pill(text = it) }
                    recipe.mealType?.let { Pill(text = it) }
                }
            }
        }
    }
}

@Composable
private fun Pill(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = Color.White,
        modifier = Modifier
            .background(BrandOrange, RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}
