package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class ScanUiState(
    val hasCameraPermission: Boolean = false,
    val isScanning: Boolean = false,
    val result: String? = null,
    val error: String? = null
)

class ScanViewModel : ViewModel() {
    var uiState by mutableStateOf(ScanUiState())
        private set

    fun setPermission(granted: Boolean) { uiState = uiState.copy(hasCameraPermission = granted) }
    fun onScanStart() { uiState = uiState.copy(isScanning = true, error = null, result = null) }
    fun onScanResult(value: String) { uiState = uiState.copy(isScanning = false, result = value) }
    fun onScanError(message: String) { uiState = uiState.copy(isScanning = false, error = message) }
}
