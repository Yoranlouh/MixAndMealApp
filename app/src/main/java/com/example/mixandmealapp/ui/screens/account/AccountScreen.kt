package com.example.mixandmealapp.ui.screens.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mixandmealapp.ui.components.BackButton
import com.example.mixandmealapp.ui.components.PrimaryButton
import com.example.mixandmealapp.ui.components.TextLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onLogout: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    navController: NavHostController,
) {

    Scaffold(
        topBar = {
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
                            text = "Account",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Welcome, User!", style = MaterialTheme.typography.headlineMedium)

            PrimaryButton(
                text = "Login/Sign up",
                modifier = Modifier,
                onClick = onLoginClick
            )

            TextLabel(
                label = "Potatoes"
            )

            TextLabel(
                label = "Onion"
            )

        }
    }
}
