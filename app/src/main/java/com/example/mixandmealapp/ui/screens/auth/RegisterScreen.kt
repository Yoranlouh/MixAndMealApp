package com.example.mixandmealapp.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.mixandmealapp.ui.components.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegister: (String, String, String) -> Unit = { _, _, _ -> },
    onGoToLogin: () -> Unit = {},
    navController: NavHostController
) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Track errors per field
    var usernameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BackButton(navController = navController, modifier = Modifier.padding(end = 8.dp))
                        Text(text = stringResource(id = com.example.mixandmealapp.R.string.register_title), style = MaterialTheme.typography.headlineSmall)
                    }
                }
            )
        }
    ) { padding ->
        val ctx = LocalContext.current
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            // Username field
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = "" // Clear error while typing
                },
                label = { Text(stringResource(id = com.example.mixandmealapp.R.string.username)) },
                modifier = Modifier.fillMaxWidth(),
                isError = usernameError.isNotEmpty()
            )
            if (usernameError.isNotEmpty()) {
                Text(usernameError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(12.dp))

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = ""
                },
                label = { Text(stringResource(id = com.example.mixandmealapp.R.string.email)) },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError.isNotEmpty()
            )
            if (emailError.isNotEmpty()) {
                Text(emailError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(12.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = ""
                },
                label = { Text(stringResource(id = com.example.mixandmealapp.R.string.password)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = passwordError.isNotEmpty()
            )
            if (passwordError.isNotEmpty()) {
                Text(passwordError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(24.dp))

            // Submit button
            Button(
                onClick = {
                    // Reset errors
                    usernameError = if (username.isBlank()) ctx.getString(com.example.mixandmealapp.R.string.username_empty_error) else ""
                    emailError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) ctx.getString(com.example.mixandmealapp.R.string.invalid_email) else ""
                    passwordError = if (password.length < 6) ctx.getString(com.example.mixandmealapp.R.string.password_min_error) else ""

                    // Only submit if all fields are valid
                    if (usernameError.isEmpty() && emailError.isEmpty() && passwordError.isEmpty()) {
                        onRegister(username, email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = com.example.mixandmealapp.R.string.create_account))
            }

            Spacer(Modifier.height(12.dp))

            TextButton(
                onClick = onGoToLogin,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(id = com.example.mixandmealapp.R.string.already_have_account_login))
            }
        }
    }
}
