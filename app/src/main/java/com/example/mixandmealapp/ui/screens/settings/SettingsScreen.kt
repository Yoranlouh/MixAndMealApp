package com.example.mixandmealapp.ui.screens.settings

import android.app.Activity
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import com.example.mixandmealapp.ui.components.LogoutButton
import com.example.mixandmealapp.ui.components.SettingsButton
import org.koin.compose.koinInject
import com.example.mixandmealapp.ui.viewmodel.AccountViewModel
import com.example.mixandmealapp.ui.viewmodel.HomeViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    val homeViewModel: HomeViewModel = koinInject()

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

        // Allergies
        SettingsButton(
            title = stringResource(R.string.allergies),
            description = stringResource(R.string.allergies_support),
            onClick = {
                // TODO
            }
        )

        SettingsButton(
            title = stringResource(R.string.language_choice),
            description = stringResource(R.string.language_choice_support),
            onClick = {
                navController.navigate(Navigation.LANGUAGE_CHOICE)
            }
        )


        // Privacy & Security
        SettingsButton(
            title = stringResource(R.string.p_and_s),
            description = "Privacy & Security",
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


//         Logout
        LogoutButton(
            title = stringResource(R.string.logout),
            description = stringResource(R.string.logout_desc),
            onClick = {
                // 2. CLEAR DATA
//                accountViewModel.logout() // Clears token from SessionRepository
                homeViewModel.logout()    // Resets global Role to "Guest"

                // 3. NAVIGATE TO LOGIN
                navController.navigate(Navigation.HOME) {
//                    // Clear the backstack so the user can't press "Back" to return to settings
//                    popUpTo(0) { inclusive = true }
//                    launchSingleTop = true
                }
            }
        )
    }
}
