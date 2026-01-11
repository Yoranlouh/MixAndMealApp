package com.example.mixandmealapp

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mixandmealapp.network.service.appModule
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mixandmealapp.ui.navigation.AppNavigation
import com.example.mixandmealapp.ui.theme.MixAndMealAppTheme
import com.example.mixandmealapp.ui.viewmodel.LocaleViewModel
import com.example.mixandmealapp.ui.viewmodel.ProvideLocalizedResources
import java.util.Locale
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


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
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        // existing camera code
    }

    private var _onPhotoPicked: ((Uri?) -> Unit)? = null

    private val photoPickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Handle photo URI - pass to UploadScreen via callback later
    }

    private fun openCamera() {
        // existing code
    }


    private fun openPhotoPicker(callback: (Uri?) -> Unit) {
        photoPickerLauncher.launch("image/*")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val localeViewModel: LocaleViewModel = viewModel()

            ProvideLocalizedResources(localeViewModel.locale) {
                MixAndMealAppTheme {
                    AppNavigation(
                        localeViewModel = localeViewModel,
                        onCameraClick = { openCamera() },
                        onPhotoPick = { callback -> openPhotoPicker(callback) }
                    )
                }
            }
        }
    }
}

