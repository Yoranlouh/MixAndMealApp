package com.example.mixandmealapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun AccountScreen(
    onLogout: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
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