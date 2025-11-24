package com.example.mixandmealapp.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mixandmealapp.R
import com.example.mixandmealapp.ui.components.BackButton
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var notificationsEnabled by remember { mutableStateOf(true) }

    androidx.compose.foundation.layout.Column(modifier = modifier.padding(16.dp)) {

//TopAppBar is experimental
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BackButton(
                        navController = navController,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        // Account button
        ListItem(
            headlineContent = {Text(stringResource(R.string.my_account))},
            supportingContent = {Text(stringResource(R.string.my_account_support))},
            modifier = Modifier
                .clickable {
                    // TODO: Send to account page
                }
        )

        Divider()

        // Favorites
        val messages = listOf(
            stringResource(R.string.fav_msg_1),
            stringResource(R.string.fav_msg_2),
            stringResource(R.string.fav_msg_3),
            stringResource(R.string.fav_msg_4),
            stringResource(R.string.fav_msg_5),
            stringResource(R.string.fav_msg_6)
        )

        val dynamicMessage = messages.random()

        ListItem(
            headlineContent = {Text(stringResource(R.string.favourites))},
            supportingContent = {Text(dynamicMessage)},
            modifier = Modifier
                .clickable {
                    // TODO: Send to Favourites page
                }
        )

        Divider()

        // My Allergies
        ListItem(
            headlineContent = {Text(stringResource(R.string.allergies))},
            supportingContent = {Text(stringResource(R.string.allergies_support))},
            modifier = Modifier
                .clickable {
                    // TODO: Send to account page
                }
        )

        Divider()

        // Language Selection
        var currentLanguage by remember { mutableStateOf("English") }
        ListItem(
            headlineContent = {Text(stringResource(R.string.language_choice))},
            supportingContent = {Text(stringResource(R.string.language_choice_support) + currentLanguage)},
            modifier = Modifier
                .clickable {
                    // TODO: Send to P & S page
                }
        )


        Divider()

        // Privacy & Security
        ListItem(
            headlineContent = {Text(stringResource(R.string.p_and_s))},
            modifier = Modifier
                .clickable {
                    // TODO: Send to P & S page
                }
        )

        Divider()

        // Help/Support
        ListItem(
            headlineContent = {Text("Help/Support")},
            supportingContent = {Text("Need some help?")},
            modifier = Modifier
                .clickable {
                    // TODO: Send to support page
                }
        )

        Divider()


        // Notification Toggle
        ListItem(
            headlineContent = { Text("Enable notifications") },
            supportingContent = { Text("Get updates about new recipes") },
            trailingContent = {
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            }
        )

        Divider()

        // About button
        ListItem(
            headlineContent = { Text("About Mix & Meal") },
            supportingContent = { Text("Version 1.0") },
            modifier = Modifier
                .clickable {
                    // TODO: Navigate to "About" screen
                }
        )

        Divider()
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SettingsPreview() {
//    MixAndMealAppTheme {
//        SettingsScreen()
//    }
//}


