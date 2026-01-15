package com.example.mixandmealapp.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

//@Composable
//fun CameraApp() {
//    val context = LocalContext.current
//    var imageUri by remember { mutableStateOf<Uri?>(null) }
//
//    // Create a file Uri for the camera to save the image to
//    val file = remember { context.createImageFile() }
//    val uri = remember {
//        FileProvider.getUriForFile(
//            Objects.requireNonNull(context),
//            context.packageName + ".provider", file
//        )
//    }
//
//    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
//        if (success) {
//            imageUri = uri
//        }
//    }
//
//    val permissionLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        if (isGranted) {
//            cameraLauncher.launch(uri)
//        } else {
//            // Optionally, show a toast or dialog to inform the user that the permission is needed.
//        }
//    }
//
//    Column(
//        Modifier.fillMaxWidth(), // Use fillMaxWidth instead of fillMaxSize inside a scrollable column
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Button(onClick = {
//            val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
//            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
//                cameraLauncher.launch(uri)
//            } else {
//                // Request the camera permission
//                permissionLauncher.launch(Manifest.permission.CAMERA)
//            }
//        }) {
//            Text(text = "Open Camera")
//        }
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        imageUri?.let {
//            Image(
//                modifier = Modifier.size(200.dp),
//                painter = rememberAsyncImagePainter(it),
//                contentDescription = "Captured image"
//            )
//        }
//    }
//}
//
//// Utility function to create an image file
//fun Context.createImageFile(): File {
//    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//    val imageFileName = "JPEG_" + timeStamp + "_"
//    // Use the external cache directory, which doesn't require storage permissions.
//    return File.createTempFile(
//        imageFileName,
//        ".jpg",
//        externalCacheDir
//    )
//}
