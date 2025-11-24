package com.example.mixandmealapp.ui.screens.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mixandmealapp.ui.components.MixAndMealColours
import com.example.mixandmealapp.ui.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onLogout: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Account") })
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
                text = "Home",
                modifier = Modifier,
                onClick = onHomeClick
            )

            PrimaryButton(
                text = "Edit Profile",
                modifier = Modifier,
                onClick = { }
            )

            PrimaryButton(
                text = "Login",
                modifier = Modifier,
                onClick = onLoginClick
            )

            PrimaryButton(
                text = "Settings",
                modifier = Modifier,
                onClick = onSettingsClick
            )

            PrimaryButton(
                text = "Logout",
                modifier = Modifier,
                backgroundColor = Color.Red,
                onClick = onLoginClick
            )


        }
    }
}
