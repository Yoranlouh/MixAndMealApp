package com.example.mixandmealapp.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FavoriteIcon(
    isFavorite: Boolean,
    onToggle: () -> Unit
) {
    val sizeAnim by animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1f,
        label = "favoriteSize"
    )

    Icon(
        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
        contentDescription = "Favorite",
        tint = if (isFavorite) Color.Red else Color.Gray,
        modifier = Modifier
            .size((28.dp * sizeAnim))
            .clickable { onToggle() }
    )
}

@Preview
@Composable
fun PreviewFavoriteIcon() {
    FavoriteIcon(isFavorite = true, onToggle = {})
}
