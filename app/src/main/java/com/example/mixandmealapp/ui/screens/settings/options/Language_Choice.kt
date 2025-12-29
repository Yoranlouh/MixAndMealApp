package com.example.mixandmealapp.ui.screens.settings.options

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mixandmealapp.ui.viewmodel.LocaleViewModel
import com.example.mixandmealapp.R
import com.example.mixandmealapp.ui.components.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageChoiceScreen(
    navController: NavController,
    localeViewModel: LocaleViewModel
) {
    var selected by remember { mutableStateOf("en") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BackButton(navController = navController, modifier = Modifier.padding(end = 8.dp))
                        Text(text = stringResource(R.string.my_language), style = MaterialTheme.typography.headlineSmall)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            LanguageOptionRow(
                title = stringResource(R.string.english),
                selected = selected == "en",
                onClick = { selected = "en" }
            )

            Divider()

            LanguageOptionRow(
                title = stringResource(R.string.dutch),
                selected = selected == "nl",
                onClick = { selected = "nl" }
            )

            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = stringResource(R.string.language_changes_applied))

            Spacer(modifier = Modifier.padding(12.dp))

            Button(
                onClick = {
                    navController.popBackStack()
                    localeViewModel.setLocale(selected)

                }
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}


@Composable
private fun LanguageOptionRow(title: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        RadioButton(selected = selected, onClick = onClick)
    }
}