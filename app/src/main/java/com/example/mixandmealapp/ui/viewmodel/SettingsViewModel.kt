package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.AppSettings
import com.example.mixandmealapp.data.ServiceLocator
import com.example.mixandmealapp.data.SettingsRepository
import com.example.mixandmealapp.common.toggle
import kotlinx.coroutines.launch

data class SettingsUiState(
    val language: String = "nl",
    val theme: String = "system",
    val allergies: Set<String> = emptySet(),
    val privacyAccepted: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null
)

class SettingsViewModel(
    private val repo: SettingsRepository = ServiceLocator.settingsRepository
) : ViewModel() {
    var uiState by mutableStateOf(SettingsUiState())
        private set

    init { load() }

    private fun load() {
        viewModelScope.launch {
            try {
                val s = repo.getSettings()
                uiState = uiState.copy(
                    language = s.language,
                    theme = s.theme,
                    allergies = s.allergies,
                    privacyAccepted = s.privacyAccepted
                )
            } catch (t: Throwable) {
                uiState = uiState.copy(error = t.message)
            }
        }
    }

    fun setLanguage(code: String) { uiState = uiState.copy(language = code) }
    fun setTheme(mode: String) { uiState = uiState.copy(theme = mode) }
    fun toggleAllergy(a: String) { uiState = uiState.copy(allergies = uiState.allergies.toggle(a)) }
    fun setPrivacyAccepted(value: Boolean) { uiState = uiState.copy(privacyAccepted = value) }
    fun save() {
        uiState = uiState.copy(isSaving = true, error = null)
        viewModelScope.launch {
            try {
                val toSave = AppSettings(
                    language = uiState.language,
                    theme = uiState.theme,
                    allergies = uiState.allergies,
                    privacyAccepted = uiState.privacyAccepted
                )
                repo.saveSettings(toSave)
                uiState = uiState.copy(isSaving = false)
            } catch (t: Throwable) {
                uiState = uiState.copy(isSaving = false, error = t.message)
            }
        }
    }
}
