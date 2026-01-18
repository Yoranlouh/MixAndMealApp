//package com.example.mixandmealapp.ui.screens.search
//
//import android.R.attr.onClick
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import com.example.mixandmealapp.models.responses.RecipeCardResponse
//import com.example.mixandmealapp.ui.theme.BrandGrey
//import com.example.mixandmealapp.ui.theme.BrandOrange
//import com.example.mixandmealapp.ui.theme.DarkText
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import coil.compose.rememberAsyncImagePainter
//import com.example.mixandmealapp.ui.navigation.Navigation
//
//@Composable
//fun SearchResultItem(
//    navController: NavHostController,
//    recipe: RecipeCardResponse
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable {
//                // Navigate to the detail screen when the card is clicked
//                navController.navigate("${Navigation.RECIPE_DETAIL}/${recipe.recipeId}")
//            },
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
//    ) {
//        Row(
//            modifier = Modifier.height(120.dp), // Set a fixed height for each item
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Image on the left
//            Image(
//                painter = rememberAsyncImagePainter(model = recipe.image),
//                contentDescription = recipe.title,
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(120.dp)
//                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)),
//                contentScale = ContentScale.Crop
//            )
//
//            // Text content on the right
//            Column(
//                modifier = Modifier
//                    .padding(horizontal = 16.dp, vertical = 8.dp)
//                    .fillMaxHeight(),
//                verticalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = recipe.title,
//                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis
//                )
//                Text(
//                    text = "${recipe.readyInMinutes} min",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun Pill(text: String) {
//    Text(
//        text = text,
//        style = MaterialTheme.typography.labelSmall,
//        color = Color.White,
//        modifier = Modifier
//            .background(BrandOrange, RoundedCornerShape(50))
//            .padding(horizontal = 8.dp, vertical = 4.dp)
//    )
//}
