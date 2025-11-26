package com.example.mixandmealapp.ui.screens.splash

import com.example.mixandmealapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mixandmealapp.ui.components.BackButton
import com.example.mixandmealapp.ui.components.PrimaryButton
import com.example.mixandmealapp.ui.components.TextOnlyButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginSplashScreen(
    navController: NavHostController,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    Text(
                        text = "Later",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- CENTERED IMAGE ---
            Image(
                painter = painterResource(id = R.drawable.mixandmealbanner),
                contentDescription = "Mix & Meal banner",
                modifier = Modifier
                    .fillMaxWidth(1f)
            )

            // --- BUTTONS AT BOTTOM ---
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                PrimaryButton(
                    text = "Login",
                    onClick = { }
                )

                TextOnlyButton(
                    text = "Don't have an account yet? Register here!",
                    onClick = { }
                )
            }
        }
    }
}