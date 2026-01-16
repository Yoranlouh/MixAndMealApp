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
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
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


class MainActivity : ComponentActivity() {
    // --- LAUNCHERS ---
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var photoPickerLauncher: ActivityResultLauncher<String>
    private lateinit var speechLauncher: ActivityResultLauncher<Intent>


    private var tempImageUri: Uri? = null
    private var onImagePicked: ((Uri?) -> Unit)? = null
    private var onSpeechResult: ((String?) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupLaunchers()
        setContent {
            val localeViewModel: LocaleViewModel = viewModel()
            ProvideLocalizedResources(localeViewModel.locale) {
                MixAndMealAppTheme {
                    AppNavigation(
                        localeViewModel = localeViewModel,
                        onPhotoPick = { callback -> openPhotoPicker(callback) },
                        onCameraClick = { callback -> launchCameraWithPermissionCheck(callback) },
                        onSpeechRecognize = { callback -> launchSpeechRecognizer(callback) }
                    )
                }
            }
        }
    }

    private fun setupLaunchers() {
        // Speech launcher
        speechLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val results = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                onSpeechResult?.invoke(results?.get(0))
            } else {
                onSpeechResult?.invoke(null) // Handle cancellation or error
            }
        }
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
                launchCamera()
            } else {
                Toast.makeText(this, "Camera permission is required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun launchSpeechRecognizer(callback: (String?) -> Unit) {
        onSpeechResult = callback
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "nl-NL") // Consider making this dynamic
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...") // Generic prompt
        }
        speechLauncher.launch(intent)
    }

    private fun openPhotoPicker(callback: (Uri?) -> Unit) {
        onImagePicked = callback
        photoPickerLauncher.launch("image/*")
    }
    private fun launchCameraWithPermissionCheck(callback: (Uri?) -> Unit) {
        onImagePicked = callback
        when (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
            PackageManager.PERMISSION_GRANTED -> {
                launchCamera()
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
    private fun launchCamera() {
        createImageFileUri().let { uri ->
            tempImageUri = uri
            cameraLauncher.launch(uri)
        }
    }
    private fun createImageFileUri(): Uri {
        val imageFile = File.createTempFile(
            "JPEG_${System.currentTimeMillis()}_",
            ".jpg",
            externalCacheDir
        )
        return FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            imageFile
        )
    }
}
