package com.example.mixandmealapp.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mixandmealapp.ui.components.BackButton
import com.example.mixandmealapp.ui.components.InputTextFieldLogin
import com.example.mixandmealapp.ui.components.PrimaryButton
import com.example.mixandmealapp.ui.navigation.Navigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit = { _, _ -> },
    onGoToRegister: () -> Unit = {},
    navController: NavHostController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
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
                            text = "Login",
                            style = MaterialTheme.typography.headlineSmall
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
            verticalArrangement = Arrangement.Center
        ) {
            InputTextFieldLogin(
                value = email,
                onValueChange = { email = it },
                label = "Email"
            )

            Spacer(Modifier.height(12.dp))

            InputTextFieldLogin(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                isPassword = true
            )

            Spacer(Modifier.height(24.dp))

            PrimaryButton(
                text = stringResource(id = com.example.mixandmealapp.R.string.login),
                onClick = { onLogin(email, password) },
            )

            TextButton(
                onClick = onGoToRegister,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(id = com.example.mixandmealapp.R.string.dont_have_account_register))
            }
        }
    }
}
