package com.example.mixandmealapp.Deprecated

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PopularRecipeScreen(
//    navController: NavHostController,
//    recipes: List<String> = listOf(
//        "Egg & Avocado Toast",
//        "Bowl of noodle with beef",
//        "Easy homemade beef burger",
//        "Half boiled egg sandwich",
//        "Veggie pasta primavera",
//        "Spicy chicken tacos",
//        "Pancakes with syrup",
//        "Taco salad"
//    )
//) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        BackButton(
//                            navController = navController,
//                            modifier = Modifier.padding(end = 8.dp)
//                        )
//                        Text(
//                            text = "Popular",
//                            style = MaterialTheme.typography.headlineSmall
//                        )
//                    }
//                }
//            )
//        }
//    ) { padding ->
//        LazyColumn(
//            modifier = Modifier
//                .padding(padding)
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            val rows = recipes.chunked(2)
//            items(count = rows.size) { rowIndex ->
//                val row = rows[rowIndex]
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    PopularCardItem(title = row[0], modifier = Modifier.weight(1f)) {
//                        navController.navigate(Navigation.RECIPE_DETAIL)
//                    }
//                    if (row.size > 1) {
//                        PopularCardItem(title = row[1], modifier = Modifier.weight(1f)) {
//                            navController.navigate(Navigation.RECIPE_DETAIL)
//                        }
//                    } else {
//                        Box(modifier = Modifier.weight(1f)) {}
//                    }
//                }
//            }
//        }
//    }
//}

//@Composable
//private fun PopularCardItem(title: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
//    Surface(
//        modifier = modifier
//            .height(180.dp)
//            .clickable { onClick() },
//        shape = MaterialTheme.shapes.large,
//        color = Color.White,
//        shadowElevation = 4.dp
//    ) {
//        androidx.compose.foundation.layout.Column(
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            androidx.compose.foundation.layout.Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//            ) {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = Color(0xFFF5F5F5)
//                ) {}
//
//                val fav = remember { mutableStateOf(false) }
//                Row(
//                    modifier = Modifier
//                        .align(Alignment.TopEnd)
//                        .padding(8.dp)
//                ) {
//                    FavoriteIcon(isFavorite = fav.value) { fav.value = !fav.value }
//                }
//            }
//
//            androidx.compose.foundation.layout.Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp)
//                    .padding(horizontal = 12.dp, vertical = 12.dp)
//            ) {
//                Text(
//                    text = title,
//                    style = MaterialTheme.typography.bodyMedium,
//                    fontWeight = FontWeight.SemiBold,
//                    color = Color(0xFF0A2533),
//                    maxLines = 2
//                )
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//private fun PopularRecipeScreenPreview() {
//    MixAndMealAppTheme {
//        PopularRecipeScreen(navController = rememberNavController())
//    }
//}
//
