package com.example.mixandmealapp.Deprecated

//data class EditorsChoiceItem(val title: String, val author: String)
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EditorsChoiceScreen(
//    navController: NavHostController,
//    items: List<EditorsChoiceItem> = listOf(
//        EditorsChoiceItem("Easy homemade beef burger", "James Spader"),
//        EditorsChoiceItem("Blueberry with egg for breakfast", "Alice Fala"),
//        EditorsChoiceItem("Creamy tomato pasta", "John Doe"),
//        EditorsChoiceItem("Healthy quinoa salad", "Jane Roe")
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
//                            text = stringResource(id = com.example.mixandmealapp.R.string.editors_choice),
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
//            val rows = items.chunked(2)
//            items(count = rows.size) { rowIndex ->
//                val row = rows[rowIndex]
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    EditorsChoiceCardItem(item = row[0], modifier = Modifier.weight(1f))
//                    if (row.size > 1) {
//                        EditorsChoiceCardItem(item = row[1], modifier = Modifier.weight(1f))
//                    } else {
//                        Box(modifier = Modifier.weight(1f)) {}
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun EditorsChoiceCardItem(item: EditorsChoiceItem, modifier: Modifier = Modifier) {
//    Surface(
//        modifier = modifier.height(180.dp),
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
//                    text = item.title,
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
//private fun EditorsChoiceScreenPreview() {
//    MixAndMealAppTheme {
//        EditorsChoiceScreen(navController = rememberNavController())
//    }
//}
//
