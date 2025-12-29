package com.example.mixandmealapp.ui.viewmodel


import android.content.res.Configuration
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


val LocalAppLocale = compositionLocalOf { Locale("en") }

@Composable
fun ProvideLocalizedResources(
    locale: Locale,
    content: @Composable () -> Unit
) {
    val baseConfig = LocalConfiguration.current

    // Create a new configuration when locale changes
    val newConfig = remember(locale) {
        Configuration(baseConfig).apply { setLocale(locale) }
    }

    // Get the current context OUTSIDE remember
    val context = LocalContext.current

    // Create the localized context when locale changes
    val localizedContext = remember(locale) {
        context.createConfigurationContext(newConfig)
    }

    CompositionLocalProvider(
        LocalAppLocale provides locale,
        LocalContext provides localizedContext,
        content = content
    )
}


class LocaleViewModel : ViewModel() {
    var locale by mutableStateOf(Locale("en"))
        private set

    fun setLocale(tag: String) {
        locale = Locale(tag)
    }
}
