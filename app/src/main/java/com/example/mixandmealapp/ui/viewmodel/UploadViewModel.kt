package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.ServiceLocator
import com.example.mixandmealapp.data.UploadRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class UploadUiState(
    val title: String = "",
    val description: String = "",
    val tags: List<String> = emptyList(),
    val mediaUris: List<String> = emptyList(),
    val isUploading: Boolean = false,
    val progress: Int = 0,
    val error: String? = null,
    val success: Boolean = false
)

class UploadViewModel(
    private val repo: UploadRepository = ServiceLocator.uploadRepository
) : ViewModel() {
    var uiState by mutableStateOf(UploadUiState())
        private set

    fun onTitleChange(value: String) { uiState = uiState.copy(title = value) }
    fun onDescriptionChange(value: String) { uiState = uiState.copy(description = value) }
    fun addTag(tag: String) { if (tag.isNotBlank()) uiState = uiState.copy(tags = uiState.tags + tag) }
    fun removeTag(tag: String) { uiState = uiState.copy(tags = uiState.tags - tag) }
    fun addMedia(uri: String) { uiState = uiState.copy(mediaUris = uiState.mediaUris + uri) }
    fun removeMedia(uri: String) { uiState = uiState.copy(mediaUris = uiState.mediaUris - uri) }

    fun upload() {
        uiState = uiState.copy(isUploading = true, error = null, success = false, progress = 0)
        viewModelScope.launch {
            try {
                // fake incremental progress updates
                for (p in listOf(10, 35, 60, 85)) {
                    delay(120)
                    uiState = uiState.copy(progress = p)
                }
                val ok = repo.upload(
                    title = uiState.title,
                    description = uiState.description,
                    tags = uiState.tags,
                    mediaUris = uiState.mediaUris
                )
                uiState = if (ok) {
                    uiState.copy(isUploading = false, progress = 100, success = true)
                } else {
                    uiState.copy(isUploading = false, error = "Upload failed", success = false)
                }
            } catch (t: Throwable) {
                uiState = uiState.copy(isUploading = false, error = t.message, success = false)
            }
        }
    }
}
