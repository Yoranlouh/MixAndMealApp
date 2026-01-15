package com.example.mixandmealapp

import android.app.Activity
import android.app.Application
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mixandmealapp.network.service.appModule
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mixandmealapp.ui.navigation.AppNavigation
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme
import com.example.mixandmealapp.ui.viewmodel.LocaleViewModel
import com.example.mixandmealapp.ui.viewmodel.ProvideLocalizedResources
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}


// ... (Your Application class remains the same)
class MainActivity : ComponentActivity() {
    // --- LAUNCHERS ---
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var photoPickerLauncher: ActivityResultLauncher<String>
    // --- STATE & CALLBACKS ---
    private var tempImageUri: Uri? = null
    private var onImagePicked: ((Uri?) -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Initialize all launchers
        setupLaunchers()
        setContent {
            val localeViewModel: LocaleViewModel = viewModel()
            ProvideLocalizedResources(localeViewModel.locale) {
                MixAndMealAppTheme {
                    AppNavigation(
                        localeViewModel = localeViewModel,
                        // Pass the functions to the navigation graph
                        onPhotoPick = { callback -> openPhotoPicker(callback) },
                        onCameraClick = { callback -> launchCameraWithPermissionCheck(callback) } // <<< FIX THIS LINE
                    )
                }
            }
        }
    }
    private fun setupLaunchers() {
        // Photo Picker launcher
        photoPickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            onImagePicked?.invoke(uri)
        }
        // Camera launcher
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                onImagePicked?.invoke(tempImageUri)
            }
        }
        // Permission launcher
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, now launch the camera
                launchCamera()
            } else {
                // Handle permission denial
                Toast.makeText(this, "Camera permission is required.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun openPhotoPicker(callback: (Uri?) -> Unit) {
        onImagePicked = callback
        photoPickerLauncher.launch("image/*")
    }
    private fun launchCameraWithPermissionCheck(callback: (Uri?) -> Unit) {
        onImagePicked = callback
        when (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
            PackageManager.PERMISSION_GRANTED -> {
                // Permission is already granted
                launchCamera()
            }
            else -> {
                // Request permission
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
    private fun launchCamera() {
        // Create a temporary file and get its URI
        createImageFileUri().let { uri ->
            tempImageUri = uri
            cameraLauncher.launch(uri)
        }
    }
    private fun createImageFileUri(): Uri {
        val imageFile = File.createTempFile(
            "JPEG_${System.currentTimeMillis()}_",
            ".jpg",
            externalCacheDir // App's private external cache dir
        )
        return FileProvider.getUriForFile(
            this,
            "${packageName}.provider", // Your application's authority
            imageFile
        )
    }
}
