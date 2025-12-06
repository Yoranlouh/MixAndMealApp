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
import com.example.mixandmealapp.ui.components.SettingsButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var notificationsEnabled by remember { mutableStateOf(true) }

    Column(modifier = modifier) {

        // Top App Bar
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
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

        // Account
        SettingsButton(
            title = stringResource(R.string.my_account),
            description = stringResource(R.string.my_account_support),
            onClick = {
                // TODO: Send to account page
            }
        )

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

        SettingsButton(
            title = stringResource(R.string.favourites),
            description = dynamicMessage,
            onClick = {
                // TODO
            }
        )

        // Allergies
        SettingsButton(
            title = stringResource(R.string.allergies),
            description = stringResource(R.string.allergies_support),
            onClick = {
                // TODO
            }
        )

        // Language
        val locales = AppCompatDelegate.getApplicationLocales()
        val currentLanguage by remember(locales) {
            mutableStateOf(
                when (val tag = locales.toLanguageTags()) {
                    null, "" -> "English"
                    else -> if (tag.startsWith("nl")) "Dutch" else "English"
                }
            )
        }

        SettingsButton(
            title = stringResource(R.string.language_choice),
            description = stringResource(R.string.language_choice_support) + currentLanguage,
            onClick = {
                navController.navigate(Navigation.LANGUAGE_CHOICE)
            }
        )

        // Privacy & Security
        SettingsButton(
            title = stringResource(R.string.p_and_s),
            onClick = {
                // TODO
            }
        )

        // Help/Support
        SettingsButton(
            title = stringResource(R.string.help_support),
            description = stringResource(R.string.help_support_desc),
            onClick = {
                // TODO
            }
        )

        // Notifications Toggle
        SettingsButton(
            title = stringResource(R.string.notifications_enable),
            description = stringResource(R.string.notifications_desc),
            onClick = {},
            trailingContent = {
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            }
        )

        // About
        SettingsButton(
            title = stringResource(R.string.about),
            description = stringResource(R.string.version),
            onClick = {}
        )

        // Logout
        SettingsButton(
            title = stringResource(R.string.logout),
            onClick = {
                // TODO logout
            },
            trailingContent = null // just a red title
        )
    }
}


//@Preview(showBackground = true)
//@Composable
//fun SettingsPreview() {
//    MixAndMealAppTheme {
//        SettingsScreen()
//    }
//}


