package com.example.mixandmealapp.ui.components

import android.app.Activity
import android.graphics.Color.red
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.system.exitProcess


fun String.truncate(maxLength: Int): String {
    return if (this.length > maxLength) {
        this.take(maxLength) + "..."
    } else {
        this
    }
}

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