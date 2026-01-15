package com.example.mixandmealapp.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
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
        text = { Text("We use cookies to improve your experience. Please accept to continue using the app.") },
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

@Composable
fun ErrorBanner(
    message: String,
    visible: Boolean,
    onDismiss: () -> Unit,
    durationMs: Int = 3000
) {
    // Auto-hide when visible becomes true
    LaunchedEffect(visible) {
        if (visible) {
            kotlinx.coroutines.delay(durationMs.toLong())
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFB00020))
                .padding(16.dp)
        ) {
            Text(
                text = message,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
