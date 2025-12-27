package com.example.mixandmealapp.ui.screens.legal

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlin.system.exitProcess

@Composable
fun PrivacyDialog(
    onAccept: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Privacy & Cookie Preferences") },
        text = {
            Text("We use cookies to improve your experience. Please accept to continue using the app.")
        },
        confirmButton = {
            Button(onClick = onAccept) {
                Text("Accept")
            }
            Button(onClick = { exitProcess(0) }) {
                Text("Decline")
//                modifier = Modifier.background(color.BrandOrange)
            }
        }


    )
}