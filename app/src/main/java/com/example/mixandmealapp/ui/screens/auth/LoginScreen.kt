package com.example.mixandmealapp.ui.screens.auth

import android.R.attr.button
import android.R.attr.text
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mixandmealapp.R
import com.example.mixandmealapp.repository.UserRepository
import com.example.mixandmealapp.ui.components.BackButton
import com.example.mixandmealapp.ui.components.InputTextFieldLogin
import com.example.mixandmealapp.ui.components.MixAndMealColours
import com.example.mixandmealapp.ui.components.PrimaryButton
import com.example.mixandmealapp.ui.components.TestPrimaryButtonWithLogin
import com.example.mixandmealapp.ui.navigation.Navigation
import com.example.mixandmealapp.ui.theme.BrandGreen
import com.example.mixandmealapp.ui.viewmodel.LoginViewModel
import com.example.mixandmealapp.ui.viewmodel.TestLoginViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLogin: (String, String) -> Unit = { _, _ -> },
    onGoToRegister: () -> Unit = {},
    navController: NavHostController,
    viewModel: TestLoginViewModel = viewModel(),
    onLoginSucces: () -> Unit = {navController.navigate(Navigation.HOME)}
) {
    val state = viewModel.uiState
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


            TestPrimaryButtonWithLogin(
                text = "Loginnnn",
                email = email,
                password = password,
                userRepository = UserRepository(),
                modifier = Modifier,
                backgroundColor = BrandGreen
            )


//            PrimaryButton(
//                text = stringResource(id = R.string.login),
//                onClick = { viewModel.login(email, password) },
//            )

            TextButton(
                onClick = onGoToRegister,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(id = R.string.dont_have_account_register))
            }
        }
    }
}
