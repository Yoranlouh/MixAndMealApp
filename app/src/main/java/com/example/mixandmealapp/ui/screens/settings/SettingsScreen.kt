package com.example.mixandmealapp.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mixandmealapp.R
import com.example.mixandmealapp.ui.components.BackButton
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme
import com.example.mixandmealapp.ui.navigation.Navigation
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var notificationsEnabled by remember { mutableStateOf(true) }

    Column(modifier = modifier) {

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
                        text = stringResource(id = R.string.settings_title),
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
        // Compute the current language label every recomposition so it updates immediately after returning
        val currentTag = AppCompatDelegate.getApplicationLocales().toLanguageTags()
        val currentLanguage = if (!currentTag.isNullOrBlank() && currentTag.startsWith("nl")) {
            stringResource(id = R.string.dutch)
        } else {
            stringResource(id = R.string.english)
        }
        ListItem(
            headlineContent = {Text(stringResource(R.string.language_choice))},
            supportingContent = {Text(stringResource(R.string.language_choice_support) + currentLanguage)},
            modifier = Modifier
                .clickable {
                    navController.navigate(Navigation.LANGUAGE_CHOICE)
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
            headlineContent = {Text(stringResource(id = R.string.help_support))},
            supportingContent = {Text(stringResource(id = R.string.help_support_desc))},
            modifier = Modifier
                .clickable {
                    // TODO: Send to support page
                }
        )

        Divider()


        // Notification Toggle
        ListItem(
            headlineContent = { Text(stringResource(id = R.string.notifications_enable)) },
            supportingContent = { Text(stringResource(id = R.string.notifications_desc)) },
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
            headlineContent = { Text(stringResource(id = R.string.about)) },
            supportingContent = { Text(stringResource(id = R.string.version)) },
            modifier = Modifier
                .clickable {
                    // TODO: Navigate to "About" screen
                }
        )

        Divider()

        // Logout button
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(id = R.string.logout),
                    color = Color.Red
                )
            },
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


