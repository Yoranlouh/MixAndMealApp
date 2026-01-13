package com.example.mixandmealapp.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.models.requests.RecipeUploadRequest
import com.example.mixandmealapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//@HiltViewModel
//class RecipeViewModel @Inject constructor(
//    private val recipeRepository: RecipeRepository
//) : ViewModel() {
//    private val _uploadState = MutableStateFlow<RecipeUploadState>(RecipeUploadState.Idle)
//    val uploadState = _uploadState.asStateFlow()
//
//    fun uploadRecipe(token: String?, imageUri: String?, request: RecipeUploadRequest) {
//        viewModelScope.launch {
//            _uploadState.value = RecipeUploadState.Loading
//            try {
//                val response = recipeRepository.uploadRecipe(token, imageUri, request)
//                _uploadState.value = RecipeUploadState.Success(response)
//            } catch (e: Exception) {
//                Log.e("RecipeViewModel", "Upload failed", e)
//                _uploadState.value = RecipeUploadState.Error(e.message ?: "Upload failed")
//            }
//        }
//    }
//}

