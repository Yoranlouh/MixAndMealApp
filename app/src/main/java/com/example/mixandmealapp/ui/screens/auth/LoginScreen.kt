package com.example.mixandmealapp.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mixandmealapp.ui.components.BackButton
import com.example.mixandmealapp.ui.components.ErrorBanner
import com.example.mixandmealapp.ui.components.InputTextFieldLogin
import com.example.mixandmealapp.ui.components.PrimaryButton
import com.example.mixandmealapp.ui.navigation.Navigation
import com.example.mixandmealapp.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import com.example.mixandmealapp.ui.viewmodel.AuthUiState
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit = { _, _ -> },
    onGoToRegister: () -> Unit = {},
    navController: NavHostController,
    viewModel: AuthViewModel = koinViewModel<AuthViewModel>()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val state by viewModel.uiState.collectAsState()
    var showBanner by remember { mutableStateOf(false) }
    var bannerMessage by remember { mutableStateOf("") }


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
                onClick = {
                    scope.launch{
                        onLogin(email, password)
                    } },
            )

            TextButton(
                onClick = onGoToRegister,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(id = com.example.mixandmealapp.R.string.dont_have_account_register))
            }

            ErrorBanner(
                message = bannerMessage,
                visible = showBanner,
                onDismiss = { showBanner = false }
            )

            when (state) {
                is AuthUiState.Error -> {
                    LaunchedEffect(state) {
                        bannerMessage = (state as AuthUiState.Error).message
                        showBanner = true
                    }
                }
                is AuthUiState.Success -> {
                    LaunchedEffect(state) {
                        navController.navigate(Navigation.HOME) {
                            // Correctly pop up to the Login screen route constant
                            popUpTo(Navigation.LOGIN) { inclusive = true }
                        }
                    }
                }
                else -> Unit
            }

        }
    }
}
